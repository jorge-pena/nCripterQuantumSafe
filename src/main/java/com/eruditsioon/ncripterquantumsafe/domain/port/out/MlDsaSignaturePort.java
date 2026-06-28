package com.eruditsioon.ncripterquantumsafe.domain.port.out;

public interface MlDsaSignaturePort {
    void generateMLDSAKeyPair(String keyLabel, String parameterSet);

    byte[] signMLDSA(String keyLabel, byte[] data);

    boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature);
}
