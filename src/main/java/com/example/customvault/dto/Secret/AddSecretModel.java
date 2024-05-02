package com.example.customvault.dto.Secret;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSecretModel {
    private String secret;
    private String value;
}
