package com.eruditsioon.ncripterquantumsafe.domain.model;

public class GenerateMLDSAKeyPairRequest {
    private String keyLabel;
    private String parameterSet;

    public GenerateMLDSAKeyPairRequest() {
    }

    public GenerateMLDSAKeyPairRequest(String keyLabel, String parameterSet) {
        this.keyLabel = keyLabel;
        this.parameterSet = parameterSet;
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
}
