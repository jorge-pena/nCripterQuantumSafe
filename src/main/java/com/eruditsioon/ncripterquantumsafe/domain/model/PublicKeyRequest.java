package com.eruditsioon.ncripterquantumsafe.domain.model;

public class PublicKeyRequest {
    private String keyLabel;
    private String outFormat = "x509-pem";

    public PublicKeyRequest() {
    }

    public PublicKeyRequest(String keyLabel, String outFormat) {
        this.keyLabel = keyLabel;
        this.outFormat = outFormat != null ? outFormat : "x509-pem";
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(String outFormat) {
        this.outFormat = outFormat;
    }

}
