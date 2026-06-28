package com.eruditsioon.ncripterquantumsafe.domain.model;

public class KmacGenerateRequest {
    private String keyLabel;
    private String algorithm = "KMAC128";
    private int keySizeBytes = 16;

    public KmacGenerateRequest() {
    }

    public KmacGenerateRequest(String keyLabel, String algorithm, int keySizeBytes) {
        this.keyLabel = keyLabel;
        this.algorithm = algorithm != null ? algorithm : "KMAC128";
        this.keySizeBytes = keySizeBytes > 0 ? keySizeBytes : 16;
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

    public int getKeySizeBytes() {
        return keySizeBytes;
    }

    public void setKeySizeBytes(int keySizeBytes) {
        this.keySizeBytes = keySizeBytes;
    }
}
