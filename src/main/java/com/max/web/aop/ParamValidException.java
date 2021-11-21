package com.max.web.aop;

import org.springframework.validation.FieldError;

import java.util.List;

public class ParamValidException extends Exception {
    private String msg;
    private List<FieldError> errorList;
    public ParamValidException(String msg, List<FieldError> errorList) {
        this.msg = msg;
        this.errorList = errorList;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public List<FieldError> getErrorList() {
        return errorList;
    }
    public void setErrorList(List<FieldError> errorList) {
        this.errorList = errorList;
    }
}
