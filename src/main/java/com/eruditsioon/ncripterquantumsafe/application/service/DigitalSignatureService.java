package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLDSAKeyPairResponse;
import com.eruditsioon.ncripterquantumsafe.domain.port.in.DigitalSignatureUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.CryptoEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DigitalSignatureService implements DigitalSignatureUseCase {

    private final CryptoEngine cryptoEngine;
    private final String keyVaultPath;

    public DigitalSignatureService(CryptoEngine cryptoEngine,
            @Value("${crypto.keyvault:./KeyVault}") String keyVaultPath) {
        this.cryptoEngine = cryptoEngine;
        this.keyVaultPath = keyVaultPath;
    }

    @Override
    public GenerateMLDSAKeyPairResponse generateMLDSAKeyPair(String keyLabel, String parameterSet) {
        cryptoEngine.generateMLDSAKeyPair(keyLabel, parameterSet, keyVaultPath);
        return new GenerateMLDSAKeyPairResponse(keyLabel, parameterSet, "Success");
    }
}
