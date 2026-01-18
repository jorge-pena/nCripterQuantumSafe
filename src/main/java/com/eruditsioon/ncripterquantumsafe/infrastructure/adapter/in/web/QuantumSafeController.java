package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.in.web;

import com.eruditsioon.ncripterquantumsafe.domain.model.DecapsulateEncryptionRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.DecapsulateEncryptionResponse;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyResponse;
import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qs-crypto")
public class QuantumSafeController {

    private final KeyEncapsulationUseCase keyEncapsulationUseCase;

    @Autowired
    public QuantumSafeController(KeyEncapsulationUseCase keyEncapsulationUseCase) {
        this.keyEncapsulationUseCase = keyEncapsulationUseCase;
    }

    @PostMapping("/request-kyber-public-key")
    public PublicKeyResponse RequestKyberPublicKey(@RequestBody PublicKeyRequest publicKeyRequest) {
        return new PublicKeyResponse(keyEncapsulationUseCase.requestKyberPublicKey(publicKeyRequest.getKeyLabel()));
    }

    @PostMapping("/decapsulate-encryption-aes-gcm")
    public DecapsulateEncryptionResponse DecapsulateEncryptionAESGCM(
            @RequestBody DecapsulateEncryptionRequest request) {
        return new DecapsulateEncryptionResponse(
                keyEncapsulationUseCase.decapsulateEncryptionAESGCM(request.getEncapsulation(),
                        request.getInitializationVector(), request.getCryptogram(), request.getKeyLabel()));
    }

}
