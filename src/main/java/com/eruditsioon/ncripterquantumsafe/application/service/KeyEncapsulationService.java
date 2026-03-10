package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.MlKemKeyExchangePort;
import org.springframework.stereotype.Service;

@Service
public class KeyEncapsulationService implements KeyEncapsulationUseCase {
    private final MlKemKeyExchangePort mlKemKeyExchangePort;

    public KeyEncapsulationService(MlKemKeyExchangePort mlKemKeyExchangePort) {
        this.mlKemKeyExchangePort = mlKemKeyExchangePort;
    }

    @Override
    public String requestKyberPublicKey(String keyLabel, String outFormat) {
        return mlKemKeyExchangePort.getPublicKey(keyLabel, "nCripter", outFormat)
                .map(result -> result.publicKey())
                .orElseThrow(() -> new RuntimeException("Failed to get public key for label: " + keyLabel));
    }

    @Override
    public com.eruditsioon.ncripterquantumsafe.domain.model.EncapsulationResult encapsulateKyber(String keyLabel) {
        return mlKemKeyExchangePort.encapsulate(keyLabel)
                .orElseThrow(() -> new RuntimeException("Failed to encapsulate against public key label: " + keyLabel));
    }

    @Override
    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel) {
        System.out.println("Decapsulating SERVICE AES GCM Key Label " + keyLabel);
        return mlKemKeyExchangePort.decapsulateAndDecrypt(keyLabel, encapsulation, initializationVector, cryptogram)
                .orElseThrow(() -> new RuntimeException("Failed to decapsulate and decrypt"));
    }

    @Override
    public String generateMLKEMKeyPair(String keyLabel, String parameterSet, String outFormat) {
        return mlKemKeyExchangePort.generateKeyPair(keyLabel, "nCripter", outFormat)
                .map(result -> result.publicKey())
                .orElseThrow(() -> new RuntimeException("Failed to generate ML-KEM key pair"));
    }
}
