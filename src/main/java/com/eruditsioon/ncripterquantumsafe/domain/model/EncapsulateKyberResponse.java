package com.eruditsioon.ncripterquantumsafe.domain.model;

public class EncapsulateKyberResponse {
    private byte[] encapsulation;
    private byte[] sharedSecret;

    public EncapsulateKyberResponse() {
    }

    public EncapsulateKyberResponse(byte[] encapsulation, byte[] sharedSecret) {
        this.encapsulation = encapsulation;
        this.sharedSecret = sharedSecret;
    }

    public byte[] getEncapsulation() {
        return encapsulation;
    }

    public void setEncapsulation(byte[] encapsulation) {
        this.encapsulation = encapsulation;
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
}
