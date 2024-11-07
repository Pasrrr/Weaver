package com.api.wx.vo;

public class InfoVO {
    private ExtendVO extend;

    public InfoVO() {
    }

    public InfoVO(ExtendVO extend) {
        this.extend = extend;
    }

    public ExtendVO getExtend() {
        return extend;
    }

    public void setExtend(ExtendVO extend) {
        this.extend = extend;
    }
}
