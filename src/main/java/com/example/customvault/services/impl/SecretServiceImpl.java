package com.example.customvault.services.impl;

import com.example.customvault.dto.Secret.*;
import com.example.customvault.exceptions.AccessDeniedException;
import com.example.customvault.exceptions.NotSecretOwnerException;
import com.example.customvault.models.Secret;
import com.example.customvault.repository.SecretRepository;
import com.example.customvault.security.JwtTokenProvider;
import com.example.customvault.security.JwtUser;
import com.example.customvault.services.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {

    private final KeyServiceImpl keyService;
    private final SecretRepository repository;
    //private final Ecc eccService;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    @Transactional
    public SecretIdResponse addSecret(AddSecretModel addSecretModel, String username) {

        Secret secret = repository.findBySecretIdAndOwnerUsername(generateId(addSecretModel.getSecret()), username)
                .orElse(new Secret());

        secret.setSecretId(generateId(addSecretModel.getSecret())); // id секрета через SHA-256
        try {
            secret.setWrappedSecretValue(seal(addSecretModel.getValue()));
            log.info("Добавили " + secret.getWrappedSecretValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        secret.setOwnerUsername(username);

        repository.save(secret);

        return new SecretIdResponse(secret.getSecretId());
    }

    @Override
    public SecretTokenResponse wrapSecret(WrapSecretModel wrapSecretModel, JwtUser jwtUser) {
        // TODO:
        //  1) Принять id секрета из модели
        //  2) Проверить доступ клиента к секрету + существование секрета в бд
        //  3) Сгенерировать токен с временем жизни
        //  4) Токен хранит id секрета, время жизни, владельца, подпись мастер-ключом
        //  5) Вернуть клиенту токен


        Secret secret = repository.findBySecretIdAndOwnerUsername(wrapSecretModel.getSecretId(), jwtUser.getUsername())
                .orElseThrow(() -> new NotSecretOwnerException("Вы не владеете этим секретом"));

        byte[] masterKeyBytes = keyService.getCurrentMasterKey().getBytes();

        //log.info(keyService.getCurrentMasterKey());

        String ttlToken = jwtTokenProvider.createTtlToken(jwtUser.getId(),
                jwtUser.getUsername(),
                secret.getSecretId(),
                masterKeyBytes
                );

        return new SecretTokenResponse(ttlToken);
    }

    @Override
    public SecretDTO unwrapSecret(UnwrapSecretModel unwrapSecretModel, JwtUser jwtUser) {
        // TODO:
        //  1) Принять токен секрета из модели
        //  2) Проверить подпись токена и время жизни
        //  3) Извлечь секрет по зашифрованному id, извлеченному из токена
        //  4) Расшифровать извлеченный wrappedSecret с помощью мастер-ключа
        //  5) Расшифрованный секрет вернуть клиенту
        byte[] masterKeyBytes = keyService.getCurrentMasterKey().getBytes();
        //log.info(keyService.getCurrentMasterKey());
        if (!jwtTokenProvider.validateTtlToken(unwrapSecretModel.getSecretToken(), masterKeyBytes)) {
            throw new AccessDeniedException();
        }



        String secretId = jwtTokenProvider.getSecretId(
                unwrapSecretModel.getSecretToken(),
                masterKeyBytes,
                jwtUser.getUsername());

        Secret secret = repository.findBySecretIdAndOwnerUsername(secretId, jwtUser.getUsername())
                .orElseThrow(() -> new NotSecretOwnerException("Вы не владеете этим секретом"));

        String unwrappedSecret = "";
        log.info("Взяли " + secret.getWrappedSecretValue());
        try {
            unwrappedSecret = unseal(secret.getWrappedSecretValue());
        } catch (Exception e) {
            log.info("Here is error");
            throw new RuntimeException(e);
        }


        return new SecretDTO(unwrappedSecret);
    }


    public static String generateId(String secretName) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(secretName.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Произошла ошибка при шифровании секрета");
        }
    }

    public String seal(String secretValue) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyService.getCurrentMasterKey().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(secretValue.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String unseal(String wrappedSecretValue) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyService.getCurrentMasterKey().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(wrappedSecretValue);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
