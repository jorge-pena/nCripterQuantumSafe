package com.eruditsioon.ncripterquantumsafe.domain.model;

public class PublicKeyResponse {
    private byte[] encodedPublicKey;

    public PublicKeyResponse() {
    }

    public PublicKeyResponse(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

    public void setEncodedPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }
}
