package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.CryptoEngine;
import org.springframework.stereotype.Service;

@Service
public class KeyEncapsulationService implements KeyEncapsulationUseCase {
    private final CryptoEngine cryptoEngine;

    public KeyEncapsulationService(CryptoEngine cryptoEngine) {
        this.cryptoEngine = cryptoEngine;
    }

    @Override
    public byte[] requestKyberPublicKey(String keyLabel) {
        return cryptoEngine.getKyberPublicKey(keyLabel);
    }

    @Override
    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel) {
        System.out.println("Decapsulating SERVICE AES GCM Key Label " + keyLabel);
        return cryptoEngine.decapsulateEncryptionAESGCM(encapsulation, initializationVector, cryptogram, keyLabel);
    }

    @Override
    public void generateMLKEMKeyPair(String keyLabel, String parameterSet) {
        cryptoEngine.generateMLKEMKeyPair(keyLabel, parameterSet);
    }
}
