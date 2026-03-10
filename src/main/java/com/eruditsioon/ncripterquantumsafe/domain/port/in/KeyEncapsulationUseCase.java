package com.eruditsioon.ncripterquantumsafe.domain.port.in;

public interface KeyEncapsulationUseCase {
    String requestKyberPublicKey(String keyLabel, String outFormat);

    com.eruditsioon.ncripterquantumsafe.domain.model.EncapsulationResult encapsulateKyber(String keyLabel);

    byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel);

    String generateMLKEMKeyPair(String keyLabel, String parameterSet, String outFormat);
}
