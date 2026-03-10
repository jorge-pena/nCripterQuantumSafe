package com.eruditsioon.ncripterquantumsafe.domain.model;

public class PublicKeyResponse {
    private String encodedPublicKey;

    public PublicKeyResponse() {
    }

    public PublicKeyResponse(String encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public String getEncodedPublicKey() {
        return encodedPublicKey;
    }

    public void setEncodedPublicKey(String encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }
}
