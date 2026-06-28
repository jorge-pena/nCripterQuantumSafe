package com.eruditsioon.ncripterquantumsafe.domain.model;

public class KmacSignResponse {
    private byte[] tag;
    private String status;

    public KmacSignResponse() {
    }

    public KmacSignResponse(byte[] tag, String status) {
        this.tag = tag;
        this.status = status;
    }

    public byte[] getTag() {
        return tag;
    }

    public void setTag(byte[] tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
