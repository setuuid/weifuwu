package com.bit.soft.gateway.model;

/**
 * Created by lyj on 2018/9/4.
 */
public class ErrorModel {

    private  int code;

    private String errorMsg;

    private Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public ErrorModel(int code, String errorMsg, Object obj) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.obj = obj;
    }
}
