package com.eruditsioon.ncripterquantumsafe.domain.model;

public class KmacVerifyResponse {
    private boolean verified;
    private String status;

    public KmacVerifyResponse() {
    }

    public KmacVerifyResponse(boolean verified, String status) {
        this.verified = verified;
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
