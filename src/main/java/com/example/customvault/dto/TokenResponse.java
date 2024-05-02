package com.example.customvault.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private UUID id;

    private String username;

    private String accessToken;

    private String refreshToken;
}
















