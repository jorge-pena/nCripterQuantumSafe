package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.port.in.DigitalSignatureUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.CryptoEngine;
import org.springframework.stereotype.Service;

@Service
public class DigitalSignatureService implements DigitalSignatureUseCase {

    private final CryptoEngine cryptoEngine;

    public DigitalSignatureService(CryptoEngine cryptoEngine) {
        this.cryptoEngine = cryptoEngine;
    }

    @Override
    public void generateMLDSAKeyPair(String keyLabel, String parameterSet) {
        cryptoEngine.generateMLDSAKeyPair(keyLabel, parameterSet);
    }

    @Override
    public byte[] signMLDSA(String keyLabel, byte[] data) {
        return cryptoEngine.signMLDSA(keyLabel, data);
    }

    @Override
    public boolean verifyMLDSA(String keyLabel, byte[] data, byte[] signature) {
        return cryptoEngine.verifyMLDSA(keyLabel, data, signature);
    }
}
