package com.example.customvault.services;

import com.example.customvault.dto.Secret.*;
import com.example.customvault.security.JwtUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface SecretService {
    SecretIdResponse addSecret(AddSecretModel addSecretModel, String username);

    SecretTokenResponse wrapSecret(WrapSecretModel wrapSecretModel, JwtUser jwtUser);

    SecretDTO unwrapSecret(UnwrapSecretModel unwrapSecretModel, JwtUser jwtUser);
}
