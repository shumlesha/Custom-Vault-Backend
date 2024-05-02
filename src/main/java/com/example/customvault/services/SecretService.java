package com.example.customvault.services;

import com.example.customvault.dto.Secret.AddSecretModel;
import com.example.customvault.dto.Secret.SecretIdResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface SecretService {
    SecretIdResponse addSecret(AddSecretModel addSecretModel);
}
