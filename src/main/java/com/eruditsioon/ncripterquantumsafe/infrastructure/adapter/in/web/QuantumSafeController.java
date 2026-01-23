package com.eruditsioon.ncripterquantumsafe.infrastructure.adapter.in.web;

import com.eruditsioon.ncripterquantumsafe.domain.model.DecapsulateEncryptionRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.DecapsulateEncryptionResponse;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.PublicKeyResponse;
import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLDSAKeyPairRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLDSAKeyPairResponse;
import com.eruditsioon.ncripterquantumsafe.domain.port.in.DigitalSignatureUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.port.in.KeyEncapsulationUseCase;
import com.eruditsioon.ncripterquantumsafe.domain.exception.nCripterException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qs-crypto")
public class QuantumSafeController {

    private final KeyEncapsulationUseCase keyEncapsulationUseCase;
    private final DigitalSignatureUseCase digitalSignatureUseCase;

    @Autowired
    public QuantumSafeController(KeyEncapsulationUseCase keyEncapsulationUseCase,
            DigitalSignatureUseCase digitalSignatureUseCase) {
        this.keyEncapsulationUseCase = keyEncapsulationUseCase;
        this.digitalSignatureUseCase = digitalSignatureUseCase;
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

    @PostMapping("/generate-ml-dsa-key-pair")
    public GenerateMLDSAKeyPairResponse generateMLDSAKeyPair(@RequestBody GenerateMLDSAKeyPairRequest request) {
        // Validate Key Label
        if (!request.getKeyLabel().matches("^[a-z0-9-]+$")) {
            throw new nCripterException("Failed to Create Key Pair:" + request.getKeyLabel() + " "
                    + request.getParameterSet() + ". Invalid key label format.");
        }

        // Validate Parameter Set
        Set<String> validParams = Set.of("ML_DSA_44", "ML_DSA_65", "ML_DSA_87");
        if (!validParams.contains(request.getParameterSet())) {
            throw new nCripterException("Failed to Create Key Pair:" + request.getKeyLabel() + " "
                    + request.getParameterSet() + ". Invalid parameter set.");
        }

        try {
            return digitalSignatureUseCase.generateMLDSAKeyPair(request.getKeyLabel(), request.getParameterSet());
        } catch (Exception e) {
            throw new nCripterException(
                    "Failed to Create Key Pair:" + request.getKeyLabel() + " " + request.getParameterSet(), e);
        }
    }

}
