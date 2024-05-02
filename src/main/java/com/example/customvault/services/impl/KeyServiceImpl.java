package com.example.customvault.services.impl;

import com.example.customvault.services.KeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final String[] CHILD_KEYS = {
            "take a moment to think",
            "of just",
            "flexability love and trust"
    };
    private final int KEY_LENGTH = 25;
    private String masterKey = null;
    @Override
    public String generateMasterKey() {
        masterKey = getRandomString(KEY_LENGTH);

        return masterKey;
    }

    @Override
    public void regenerateMasterKey() {
        masterKey = null;
        generateMasterKey();
    }

    @Override
    public String getCurrentMasterKey() {
        return (masterKey == null) ? generateMasterKey(): masterKey;
    }

    private String getRandomString(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }
}
