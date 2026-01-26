package com.eruditsioon.ncripterquantumsafe.domain.port.in;

public interface DigitalSignatureUseCase {
    void generateMLDSAKeyPair(String keyLabel, String parameterSet);

    byte[] signMLDSA(String keyLabel, byte[] data);

    boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature);
}
