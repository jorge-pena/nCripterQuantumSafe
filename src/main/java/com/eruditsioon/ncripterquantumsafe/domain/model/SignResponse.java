package com.eruditsioon.ncripterquantumsafe.domain.model;

public class SignResponse {
    private String keyLabel;
    private byte[] signature;
    private String status;

    public SignResponse() {
    }

    public SignResponse(String keyLabel, byte[] signature, String status) {
        this.keyLabel = keyLabel;
        this.signature = signature;
        this.status = status;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
