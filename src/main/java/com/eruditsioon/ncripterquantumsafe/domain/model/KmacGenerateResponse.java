package com.eruditsioon.ncripterquantumsafe.domain.model;

public class KmacGenerateResponse {
    private String keyLabel;
    private String algorithm;
    private String status;

    public KmacGenerateResponse() {
    }

    public KmacGenerateResponse(String keyLabel, String algorithm, String status) {
        this.keyLabel = keyLabel;
        this.algorithm = algorithm;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
