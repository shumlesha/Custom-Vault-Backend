package com.example.customvault.controller;


import com.example.customvault.dto.Secret.*;
import com.example.customvault.services.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/secrets")
@RequiredArgsConstructor
public class SecretController {
    private final SecretService secretService;

    @PostMapping("/add")
    public ResponseEntity<?> addSecret(@Validated @RequestBody AddSecretModel addSecretModel) {
        SecretIdResponse secretId = secretService.addSecret(addSecretModel);
        return ResponseEntity.ok(secretId);
    }

    @PostMapping("/wrap")
    public ResponseEntity<?> wrapSecret(@Validated @RequestBody WrapSecretModel wrapSecretModel) {
        SecretTokenResponse secretToken = secretService.wrapSecret(wrapSecretModel);
        return ResponseEntity.ok(secretToken);
    }


    @PostMapping("/unwrap")
    public ResponseEntity<?> unwrapSecret(@Validated @RequestBody UnwrapSecretModel unwrapSecretModel) {
        SecretDTO secretValue = secretService.unwrapSecret(unwrapSecretModel);
        return ResponseEntity.ok(secretValue);
    }
}
