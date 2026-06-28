package com.eruditsioon.ncripterquantumsafe.domain.model;

public class KmacVerifyRequest {
    private String keyLabel;
    private String algorithm = "KMAC128";
    private String customizationString = "";
    private int outputLenBits = 256;
    private byte[] payload;
    private byte[] tag;

    public KmacVerifyRequest() {
    }

    public KmacVerifyRequest(String keyLabel, String algorithm, String customizationString, int outputLenBits, byte[] payload, byte[] tag) {
        this.keyLabel = keyLabel;
        this.algorithm = algorithm != null ? algorithm : "KMAC128";
        this.customizationString = customizationString != null ? customizationString : "";
        this.outputLenBits = outputLenBits > 0 ? outputLenBits : 256;
        this.payload = payload;
        this.tag = tag;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCustomizationString() {
        return customizationString;
    }

    public void setCustomizationString(String customizationString) {
        this.customizationString = customizationString;
    }

    public int getOutputLenBits() {
        return outputLenBits;
    }

    public void setOutputLenBits(int outputLenBits) {
        this.outputLenBits = outputLenBits;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getTag() {
        return tag;
    }

    public void setTag(byte[] tag) {
        this.tag = tag;
    }
}
