package com.example.customvault.services.impl;

import com.example.customvault.dto.Secret.*;
import com.example.customvault.services.SecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {
    // TODO:
    // Написать сервис мастер-ключа, который генерируется из дочерних
    // Мастер-ключ перегенерируется из дочерних каждый раз перед действиями подписи, где необходим мастер-ключ


    @Override
    public SecretIdResponse addSecret(AddSecretModel addSecretModel) {
        // TODO:
        //  1) Принять секрет из модели (имя + значение)
        // 2) Зашифровать значение секрета через мастер-ключ
        // 3) Мастер ключ получить из дочерних
        // 4) Сгенерировать id для имени секрета (хеш/случаный uuid...)
        // 5) В БД положить пару (secretId, wrappedSecret) + владельца
        // 6) Вернуть клиенту secretId

        return null;
    }

    @Override
    public SecretTokenResponse wrapSecret(WrapSecretModel wrapSecretModel) {
        // TODO:
        //  1) Принять id секрета из модели
        // 2) Проверить доступ клиента к секрету + существование секрета в бд
        // 3) Сгенерировать токен с временем жизни
        // 4) Токен хранит id секрета, время жизни, владельца, подпись мастер-ключом
        // 5) Вернуть клиенту токен
        return null;
    }

    @Override
    public SecretDTO unwrapSecret(UnwrapSecretModel unwrapSecretModel) {
        // TODO:
        //  1) Принять токен секрета из модели
        // 2) Проверить подпись токена и время жизни
        // 3) Извлечь секрет по зашифрованному id, извлеченному из токена
        // 4) Расшифровать извлеченный wrappedSecret с помощью мастер-ключа
        // 5) Расшифрованный секрет вернуть клиентуу
        return null;
    }
}
