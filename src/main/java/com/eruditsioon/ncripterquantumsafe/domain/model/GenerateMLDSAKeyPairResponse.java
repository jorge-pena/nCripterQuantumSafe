package com.eruditsioon.ncripterquantumsafe.domain.model;

public class GenerateMLDSAKeyPairResponse {
    private String keyLabel;
    private String parameterSet;
    private String status;

    public GenerateMLDSAKeyPairResponse() {
    }

    public GenerateMLDSAKeyPairResponse(String keyLabel, String parameterSet, String status) {
        this.keyLabel = keyLabel;
        this.parameterSet = parameterSet;
        this.status = status;
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
}
