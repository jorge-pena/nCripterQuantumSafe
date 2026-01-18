package com.eruditsioon.ncripterquantumsafe.domain.port.out;

public interface CryptoEngine {
    byte[] getKyberPublicKey(String keyLabel);

    byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram,
            String keyLabel);
}
