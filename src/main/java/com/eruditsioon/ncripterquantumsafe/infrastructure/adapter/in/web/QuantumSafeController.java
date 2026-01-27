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
import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLKEMKeyPairRequest;
import com.eruditsioon.ncripterquantumsafe.domain.model.GenerateMLKEMKeyPairResponse;

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
        if (request.getKeyLabel() == null || !request.getKeyLabel().matches("^[a-z0-9-]+$")) {
            throw new nCripterException("Invalid key label format.");
        }

        // Validate Parameter Set
        Set<String> validParams = Set.of("ML_DSA_44", "ML_DSA_65", "ML_DSA_87");
        if (request.getParameterSet() == null || !validParams.contains(request.getParameterSet())) {
            throw new nCripterException("Invalid parameter set.");
        }

        try {
            digitalSignatureUseCase.generateMLDSAKeyPair(request.getKeyLabel(), request.getParameterSet());
            return new GenerateMLDSAKeyPairResponse(request.getKeyLabel(), request.getParameterSet(), "Success");
        } catch (Exception e) {
            throw new nCripterException(
                    "Failed to Create Key Pair:" + request.getKeyLabel() + " " + request.getParameterSet(), e);
        }
    }

    @PostMapping("/generate-ml-kem-key-pair")
    public GenerateMLKEMKeyPairResponse generateMLKEMKeyPair(@RequestBody GenerateMLKEMKeyPairRequest request) {
        // Validate Key Label
        if (request.getKeyLabel() == null || !request.getKeyLabel().matches("^[a-z0-9-]+$")) {
            throw new nCripterException("Invalid key label format.");
        }

        // Validate Parameter Set
        Set<String> validParams = Set.of("ML_KEM_512", "ML_KEM_768", "ML_KEM_1024");
        if (request.getParameterSet() == null || !validParams.contains(request.getParameterSet())) {
            throw new nCripterException("Invalid parameter set.");
        }

        try {
            keyEncapsulationUseCase.generateMLKEMKeyPair(request.getKeyLabel(), request.getParameterSet());
            return new GenerateMLKEMKeyPairResponse(request.getKeyLabel(), request.getParameterSet(), "Success");
        } catch (Exception e) {
            throw new nCripterException(
                    "Failed to Create Key Pair:" + request.getKeyLabel() + " " + request.getParameterSet(), e);
        }
    }

    @PostMapping("/sign-ml-dsa")
    public com.eruditsioon.ncripterquantumsafe.domain.model.SignResponse sign(
            @RequestBody com.eruditsioon.ncripterquantumsafe.domain.model.SignRequest request) {
        if (request.getKeyLabel() == null || request.getData() == null) {
            throw new IllegalArgumentException("Key label and data must not be null.");
        }
        try {
            byte[] signature = digitalSignatureUseCase.signMLDSA(request.getKeyLabel(), request.getData());
            return new com.eruditsioon.ncripterquantumsafe.domain.model.SignResponse(request.getKeyLabel(), signature,
                    "Success");
        } catch (Exception e) {
            throw new nCripterException("Signing failed", e);
        }
    }

    @PostMapping("/verify-ml-dsa")
    public com.eruditsioon.ncripterquantumsafe.domain.model.VerifyResponse verify(
            @RequestBody com.eruditsioon.ncripterquantumsafe.domain.model.VerifyRequest request) {
        if (request.getKeyLabel() == null || request.getData() == null || request.getSignature() == null) {
            throw new IllegalArgumentException("Key label, data, and signature must not be null.");
        }
        try {
            boolean isValid = digitalSignatureUseCase.verifyMLDSA(request.getKeyLabel(), request.getData(),
                    request.getSignature());
            return new com.eruditsioon.ncripterquantumsafe.domain.model.VerifyResponse(isValid,
                    isValid ? "Signature is valid" : "Signature is invalid");
        } catch (Exception e) {
            throw new nCripterException("Verification failed", e);
        }
    }

}
