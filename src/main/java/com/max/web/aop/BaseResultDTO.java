package com.max.web.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
public class BaseResultDTO {
    @JsonInclude(Include.NON_NULL)
    private String code;
    @JsonInclude(Include.NON_NULL)
    private Object data;
    @JsonInclude(Include.NON_NULL)
    private Object msg;
    private String traceId;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public Object getMsg() {
        return msg;
    }
    public void setMsg(Object msg) {
        this.msg = msg;
    }
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
