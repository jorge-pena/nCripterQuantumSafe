package com.eruditsioon.ncripterquantumsafe.domain.port.in;

import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLDSAKeyPairResponse;

public interface DigitalSignatureUseCase {
    GenerateMLDSAKeyPairResponse generateMLDSAKeyPair(String keyLabel, String parameterSet);
}
