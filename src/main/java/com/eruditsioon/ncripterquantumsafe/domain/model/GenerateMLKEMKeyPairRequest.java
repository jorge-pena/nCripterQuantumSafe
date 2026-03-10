package com.eruditsioon.ncripterquantumsafe.domain.model;

public class GenerateMLKEMKeyPairRequest {
    private String keyLabel;
    private String parameterSet;
    private String outFormat = "x509-pem";

    public GenerateMLKEMKeyPairRequest() {
    }

    public GenerateMLKEMKeyPairRequest(String keyLabel, String parameterSet, String outFormat) {
        this.keyLabel = keyLabel;
        this.parameterSet = parameterSet;
        this.outFormat = outFormat != null ? outFormat : "x509-pem";
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

    public String getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(String outFormat) {
        this.outFormat = outFormat;
    }
}
