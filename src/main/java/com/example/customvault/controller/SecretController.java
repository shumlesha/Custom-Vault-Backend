package com.example.customvault.controller;


import com.example.customvault.dto.Secret.*;
import com.example.customvault.security.JwtUser;
import com.example.customvault.services.SecretService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/secrets")
@RequiredArgsConstructor
@Tag(name = "Secrets")
public class SecretController {
    private final SecretService secretService;

    @PostMapping("/add")
    @Operation(summary = "Add new secret")
    public ResponseEntity<?> addSecret(@Validated @RequestBody AddSecretModel addSecretModel,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        SecretIdResponse secretId = secretService.addSecret(addSecretModel, jwtUser.getUsername());
        return ResponseEntity.ok(secretId);
    }

    @PostMapping("/wrap")
    @Operation(summary = "Wrap secret by id and get token to access it")
    public ResponseEntity<?> wrapSecret(@Validated @RequestBody WrapSecretModel wrapSecretModel,
                                        @AuthenticationPrincipal JwtUser jwtUser) {
        SecretTokenResponse secretToken = secretService.wrapSecret(wrapSecretModel, jwtUser);
        return ResponseEntity.ok(secretToken);
    }


    @PostMapping("/unwrap")
    @Operation(summary = "Unwrap secret by token and get its value")
    public ResponseEntity<?> unwrapSecret(@Validated @RequestBody UnwrapSecretModel unwrapSecretModel,
                                          @AuthenticationPrincipal JwtUser jwtUser) {
        SecretDTO secretValue = secretService.unwrapSecret(unwrapSecretModel, jwtUser);
        return ResponseEntity.ok(secretValue);
    }
}
