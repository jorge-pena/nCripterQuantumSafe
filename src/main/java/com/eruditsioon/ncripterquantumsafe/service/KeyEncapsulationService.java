package com.eruditsioon.ncripterquantumsafe.service;

public interface KeyEncapsulationService {
    byte[] requestKyberPublicKey(String keyLabel);
    byte[] decapsulateEncryptionAESGCM (byte[] encapsulation, byte[]initializationVector, byte[] cryptogram, String keyLabel);
}
