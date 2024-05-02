package com.example.customvault.services;

import java.math.BigInteger;

public interface KeyService {
    String generateMasterKey();
    void regenerateMasterKey();
    String getCurrentMasterKey();
}
