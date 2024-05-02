package com.example.customvault.services.impl;

import com.example.customvault.dto.Secret.*;
import com.example.customvault.models.Secret;
import com.example.customvault.services.SecretService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {

    private final KeyServiceImpl keyService;
    //private final Ecc eccService;


    @Override
    public SecretIdResponse addSecret(AddSecretModel addSecretModel, String username) {
        // TODO:
        //  1) Принять секрет из модели (имя + значение)
        //  2) Зашифровать значение секрета через мастер-ключ
        //  3) Мастер ключ получить из дочерних
        //  4) Сгенерировать id для имени секрета (хеш/случаный uuid...)

        Secret secret = new Secret();
        secret.setSecretId(generateId(addSecretModel.getSecret())); // id секрета через SHA-256
        //secret.setWrappedSecretValue(seal(addSecretModel.getValue())); // TODO: подписать и зашифровать мастер-ключом
        secret.setOwnerUsername(username);

        // TODO:
        //  5) В БД положить пару (secretId, wrappedSecret) + владельца
        //  6) Вернуть клиенту secretId

        return null;
    }

    @Override
    public SecretTokenResponse wrapSecret(WrapSecretModel wrapSecretModel) {
        // TODO:
        //  1) Принять id секрета из модели
        //  2) Проверить доступ клиента к секрету + существование секрета в бд
        //  3) Сгенерировать токен с временем жизни
        //  4) Токен хранит id секрета, время жизни, владельца, подпись мастер-ключом
        //  5) Вернуть клиенту токен
        return null;
    }

    @Override
    public SecretDTO unwrapSecret(UnwrapSecretModel unwrapSecretModel) {
        // TODO:
        //  1) Принять токен секрета из модели
        //  2) Проверить подпись токена и время жизни
        //  3) Извлечь секрет по зашифрованному id, извлеченному из токена
        //  4) Расшифровать извлеченный wrappedSecret с помощью мастер-ключа
        //  5) Расшифрованный секрет вернуть клиентуу
        return null;
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
        return new String(cipher.doFinal(secretValue.getBytes()));
    }

    public String unseal(String wrappedSecretValue) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyService.getCurrentMasterKey().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(wrappedSecretValue.getBytes()));
    }
}
