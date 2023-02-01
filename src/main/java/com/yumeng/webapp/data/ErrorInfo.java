package com.yumeng.webapp.data;

public class ErrorInfo {
    private int code;
    private String errorMessage;

    public void setCode(int code) {
        this.code = code;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorInfo(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
