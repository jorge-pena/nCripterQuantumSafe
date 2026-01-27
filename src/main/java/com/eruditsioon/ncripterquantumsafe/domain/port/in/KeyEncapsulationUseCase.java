package com.eruditsioon.ncripterquantumsafe.domain.port.in;

public interface KeyEncapsulationUseCase {
    byte[] requestKyberPublicKey(String keyLabel);

    byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel);

    void generateMLKEMKeyPair(String keyLabel, String parameterSet);
}
