package com.example.customvault.controller;


import com.example.customvault.dto.Secret.AddSecretModel;
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


    @PostMapping("/add")
    public ResponseEntity<?> addSecret(@Validated @RequestBody AddSecretModel addSecretModel) {

    }
}
