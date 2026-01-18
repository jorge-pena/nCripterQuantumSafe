package com.eruditsioon.ncripterquantumsafe.entity;

public class DecapsulateEncryptionRequest {
    private String keyLabel;
    private byte[] initializationVector;
    private byte[] cryptogram;
    private byte[] encapsulation;

    public DecapsulateEncryptionRequest() {
    }

    public DecapsulateEncryptionRequest(String keyLabel, byte[] initializationVector, byte[] cryptogram, byte[] encapsulation) {
        this.keyLabel = keyLabel;
        this.initializationVector = initializationVector;
        this.cryptogram = cryptogram;
        this.encapsulation = encapsulation;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public byte[] getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(byte[] initializationVector) {
        this.initializationVector = initializationVector;
    }

    public byte[] getCryptogram() {
        return cryptogram;
    }

    public void setCryptogram(byte[] cryptogram) {
        this.cryptogram = cryptogram;
    }

    public byte[] getEncapsulation() {
        return encapsulation;
    }

    public void setEncapsulation(byte[] encapsulation) {
        this.encapsulation = encapsulation;
    }
}
