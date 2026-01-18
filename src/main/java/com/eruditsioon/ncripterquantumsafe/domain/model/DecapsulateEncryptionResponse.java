package com.eruditsioon.ncripterquantumsafe.domain.model;

public class DecapsulateEncryptionResponse {

    private byte[] clearData;

    public DecapsulateEncryptionResponse() {
    }
    public DecapsulateEncryptionResponse(byte[] clearData) {
        this.clearData = clearData;
    }

    public byte[] getClearData() {
        return clearData;
    }

    public void setClearData(byte[] clearData) {
        this.clearData = clearData;
    }
}
