package com.eruditsioon.ncripterquantumsafe.domain.model;

public class EncapsulateKyberRequest {
    private String keyLabel;

    public EncapsulateKyberRequest() {
    }

    public EncapsulateKyberRequest(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }
}
