package com.api.wx.vo;

public class ExtendVO {
    private String swingTime;
    private String personCode;

    public ExtendVO() {
    }

    public String getSwingTime() {
        return swingTime;
    }

    public void setSwingTime(String swingTime) {
        this.swingTime = swingTime;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public ExtendVO(String swingTime, String personCode) {
        this.swingTime = swingTime;
        this.personCode = personCode;
    }

    @Override
    public String toString() {
        return "ExtendVO{" +
                "swingTime='" + swingTime + '\'' +
                ", personCode='" + personCode + '\'' +
                '}';
    }
}
