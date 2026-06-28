package com.eruditsioon.ncripterquantumsafe.application.service;

import com.eruditsioon.ncripterquantumsafe.domain.model.*;
import com.eruditsioon.ncripterquantumsafe.domain.port.in.KmacUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.out.KmacPort;
import org.springframework.stereotype.Service;

@Service
public class KmacService implements KmacUseCase {
    private final KmacPort kmacPort;

    public KmacService(KmacPort kmacPort) {
        this.kmacPort = kmacPort;
    }

    @Override
    public KmacGenerateResponse generateKey(KmacGenerateRequest request) {
        return kmacPort.generateKey(request.getKeyLabel(), request.getAlgorithm(), request.getKeySizeBytes(), "simple")
                .orElseThrow(() -> new RuntimeException("Failed to generate KMAC key for label: " + request.getKeyLabel()));
    }

    @Override
    public KmacSignResponse sign(KmacSignRequest request) {
        byte[] tag = kmacPort.sign(request.getKeyLabel(), request.getAlgorithm(), request.getCustomizationString(), request.getOutputLenBits(), request.getPayload(), "simple")
                .orElseThrow(() -> new RuntimeException("KMAC signing failed for label: " + request.getKeyLabel()));
        return new KmacSignResponse(tag, "Success");
    }

    @Override
    public KmacVerifyResponse verify(KmacVerifyRequest request) {
        boolean verified = kmacPort.verify(request.getKeyLabel(), request.getAlgorithm(), request.getCustomizationString(), request.getOutputLenBits(), request.getPayload(), request.getTag(), "simple")
                .orElseThrow(() -> new RuntimeException("KMAC verification failed for label: " + request.getKeyLabel()));
        return new KmacVerifyResponse(verified, verified ? "Signature is valid" : "Signature is invalid");
    }
}
