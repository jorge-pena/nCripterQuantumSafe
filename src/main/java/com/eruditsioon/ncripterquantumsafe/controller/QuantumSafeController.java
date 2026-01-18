package com.eruditsioon.ncripterquantumsafe.controller;


import com.eruditsioon.ncripterquantumsafe.entity.DecapsulateEncryptionRequest;
import com.eruditsioon.ncripterquantumsafe.entity.DecapsulateEncryptionResponse;
import com.eruditsioon.ncripterquantumsafe.entity.PublicKeyRequest;
import com.eruditsioon.ncripterquantumsafe.entity.PublicKeyResponse;
import com.eruditsioon.ncripterquantumsafe.service.KeyEncapsulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qs-crypto")
public class QuantumSafeController {
    @Autowired
    private KeyEncapsulationService keyEncapsulationService;

    @PostMapping("/request-kyber-public-key")
    public PublicKeyResponse RequestKyberPublicKey(@RequestBody PublicKeyRequest publicKeyRequest){
        return new PublicKeyResponse(keyEncapsulationService.requestKyberPublicKey(publicKeyRequest.getKeyLabel()));
    }
    @PostMapping("/decapsulate-encryption-aes-gcm")
    public DecapsulateEncryptionResponse DecapsulateEncryptionAESGCM(@RequestBody DecapsulateEncryptionRequest request){
        return new DecapsulateEncryptionResponse(keyEncapsulationService.decapsulateEncryptionAESGCM(request.getEncapsulation(), request.getInitializationVector(), request.getCryptogram(), request.getKeyLabel()));
    }

}
