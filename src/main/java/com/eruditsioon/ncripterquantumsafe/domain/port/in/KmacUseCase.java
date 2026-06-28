package com.eruditsioon.ncripterquantumsafe.domain.port.in;

import com.eruditsioon.ncripterquantumsafe.domain.model.*;

public interface KmacUseCase {
    KmacGenerateResponse generateKey(KmacGenerateRequest request);

    KmacSignResponse sign(KmacSignRequest request);

    KmacVerifyResponse verify(KmacVerifyRequest request);
}
