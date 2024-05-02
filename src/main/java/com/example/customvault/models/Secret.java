package com.example.customvault.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "secrets")
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "secret_id", nullable = false)
    private String secretId;

    @Column(name = "secret_value", nullable = false)
    private String wrappedSecretValue;

    @Column(name = "owner_username", nullable = false)
    private String ownerUsername;
}
