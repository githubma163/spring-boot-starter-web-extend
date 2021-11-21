package com.max.web.aop;

public enum ErrorCodeEnum {

    SUCCESS("A00000", "success"),
    PARAM_ERROR("A00001", "parameter error"),
    SYSTEM_ERROR("A00002", "system error"),
    RECORD_NOT_EXISTS("A00003", "record not exists");
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ErrorCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
