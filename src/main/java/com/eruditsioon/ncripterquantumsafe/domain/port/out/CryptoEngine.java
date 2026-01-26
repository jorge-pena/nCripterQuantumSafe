package com.eruditsioon.ncripterquantumsafe.domain.port.out;

public interface CryptoEngine {
    byte[] getKyberPublicKey(String keyLabel);

    byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel);

    void generateMLDSAKeyPair(String keyLabel, String parameterSet);

    byte[] signMLDSA(String keyLabel, byte[] data);

    boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature);
}
