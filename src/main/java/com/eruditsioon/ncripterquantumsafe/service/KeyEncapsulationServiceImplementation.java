package com.eruditsioon.ncripterquantumsafe.service;

import com.eruditsioon.ncripterquantumsafe.engine.KyberProvider;
import org.springframework.stereotype.Service;

@Service
public class KeyEncapsulationServiceImplementation implements KeyEncapsulationService{
    private  KyberProvider kyberProvider= new KyberProvider();

    public KeyEncapsulationServiceImplementation(KyberProvider kyberProvider) {
        this.kyberProvider = kyberProvider;
    }

    @Override
    public byte[] requestKyberPublicKey(String keyLabel) {
        return kyberProvider.getKyberPublicKey(keyLabel);
    }

    @Override
    public byte[] decapsulateEncryptionAESGCM(byte[] encapsulation, byte[] initializationVector, byte[] cryptogram, String keyLabel) {
        System.out.println("Decapsulating SERVICE AES GCM Key Label "+keyLabel);
        return kyberProvider.decapsulateEncryptionAESGCM(encapsulation, initializationVector, cryptogram, keyLabel);
    }
}
