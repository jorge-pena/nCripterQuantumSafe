package com.eruditsioon.ncripterquantumsafe.domain.model;

public class VerifyRequest {
    private String keyLabel;
    private byte[] data;
    private byte[] signature;

    public VerifyRequest() {
    }

    public VerifyRequest(String keyLabel, byte[] data, byte[] signature) {
        this.keyLabel = keyLabel;
        this.data = data;
        this.signature = signature;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
