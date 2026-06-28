package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.DigitalSignatureUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.MlDsaSignaturePort;
import org.springframework.stereotype.Service;

@Service
public class DigitalSignatureService implements DigitalSignatureUseCase {

    private final MlDsaSignaturePort mlDsaSignaturePort;

    public DigitalSignatureService(MlDsaSignaturePort mlDsaSignaturePort) {
        this.mlDsaSignaturePort = mlDsaSignaturePort;
    }

    @Override
    public void generateMLDSAKeyPair(String keyLabel, String parameterSet) {
        mlDsaSignaturePort.generateMLDSAKeyPair(keyLabel, parameterSet);
    }

    @Override
    public byte[] signMLDSA(String keyLabel, byte[] data) {
        return mlDsaSignaturePort.signMLDSA(keyLabel, data);
    }

    @Override
    public boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature) {
        return mlDsaSignaturePort.verifyMLDSA(keyLabel, data, signature);
    }
}
