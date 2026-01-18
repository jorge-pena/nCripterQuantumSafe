package com.eruditsioon.ncripterquantumsafe.entity;

public class PublicKeyRequest {
    private String keyLabel;

    public PublicKeyRequest() {
    }

    public PublicKeyRequest(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

}
