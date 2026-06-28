package com.eruditsioon.ncripterquantumsafe.domain.port.out;

import java.util.Optional;
import com.eruditsioon.ncripterquantumsafe.domain.model.*;

public interface KmacPort {
    Optional<KmacGenerateResponse> generateKey(String ident, String algorithm, int keySizeBytes, String appName);

    Optional<byte[]> sign(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, String appName);

    Optional<Boolean> verify(String keyIdentifier, String algorithm, String customizationString, int outputLenBits, byte[] payload, byte[] tag, String appName);
}
