package com.api.wx.vo;

public class ResponseVO {
    private boolean success;
    private String code;
    private String errMsg;
    private Object data;

    public ResponseVO(boolean success, String code, String errMsg, Object data) {
        this.success = success;
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public ResponseVO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
