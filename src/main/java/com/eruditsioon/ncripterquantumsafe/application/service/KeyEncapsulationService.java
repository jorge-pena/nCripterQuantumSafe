package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.MlKemKeyExchangePort;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class KeyEncapsulationService implements KeyEncapsulationUseCase {
    private final MlKemKeyExchangePort mlKemKeyExchangePort;

    public KeyEncapsulationService(MlKemKeyExchangePort mlKemKeyExchangePort) {
        this.mlKemKeyExchangePort = mlKemKeyExchangePort;
    }

    @Override
    public byte[] requestKyberPublicKey(String keyLabel) {
        return mlKemKeyExchangePort.getPublicKey(keyLabel, null, null)
                .map(result -> Base64.getDecoder().decode(result.publicKey()))
                .orElseThrow(() -> new RuntimeException("Failed to get public key for label: " + keyLabel));
    }

    @Override
    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel) {
        System.out.println("Decapsulating SERVICE AES GCM Key Label " + keyLabel);
        return mlKemKeyExchangePort.decapsulateAndDecrypt(keyLabel, encapsulation, initializationVector, cryptogram)
                .orElseThrow(() -> new RuntimeException("Failed to decapsulate and decrypt"));
    }

    @Override
    public void generateMLKEMKeyPair(String keyLabel, String parameterSet) {
        mlKemKeyExchangePort.generateKeyPair(keyLabel, null, null)
                .orElseThrow(() -> new RuntimeException("Failed to generate ML-KEM key pair"));
    }
}
