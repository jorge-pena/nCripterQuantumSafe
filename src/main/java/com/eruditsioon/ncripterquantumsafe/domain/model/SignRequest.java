package com.eruditsioon.ncripterquantumsafe.domain.model;

public class SignRequest {
    private String keyLabel;
    private byte[] data;

    public SignRequest() {
    }

    public SignRequest(String keyLabel, byte[] data) {
        this.keyLabel = keyLabel;
        this.data = data;
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
}
