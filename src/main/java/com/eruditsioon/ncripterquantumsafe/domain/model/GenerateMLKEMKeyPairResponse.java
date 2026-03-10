package com.eruditsioon.ncripterquantumsafe.domain.model;

public class GenerateMLKEMKeyPairResponse {
    private String keyLabel;
    private String parameterSet;
    private String status;
    private String publicKey;

    public GenerateMLKEMKeyPairResponse() {
    }

    public GenerateMLKEMKeyPairResponse(String keyLabel, String parameterSet, String status, String publicKey) {
        this.keyLabel = keyLabel;
        this.parameterSet = parameterSet;
        this.status = status;
        this.publicKey = publicKey;
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        this.keyLabel = keyLabel;
    }

    public String getParameterSet() {
        return parameterSet;
    }

    public void setParameterSet(String parameterSet) {
        this.parameterSet = parameterSet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
