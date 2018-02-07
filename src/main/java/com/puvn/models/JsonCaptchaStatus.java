package com.puvn.models;

public class JsonCaptchaStatus {
    private boolean success;
    private String[] error_codes;

    public String[] getError_codes() {
        return error_codes;
    }

    public void setError_codes(String[] error_codes) {
        this.error_codes = error_codes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
