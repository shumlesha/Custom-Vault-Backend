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


}
