package esteem.jun.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @className: Result
 * @author: jun
 * @date: 2022-05-20 14:38
 * @Depiction:
 **/
public class Result {
    public static final String SUCCESS = "操作成功!";
    public static final String FAILURE = "操作失败!";
    private boolean result;
    private String code;
    private String msg;

    private String bak1;

    private List<Map<String,Object>> items =new ArrayList<>();

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Result ok() {
        return Result.ok(SUCCESS);
    }

    public static Result ok(String msg) {
        Result result = new Result();
        result.setResult(true);
        result.setMsg(msg);
        return result;
    }

    public static Result fail() {
        return Result.fail(FAILURE);
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setResult(false);
        result.setMsg(msg);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Map<String,Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String,Object>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", bak1='" + bak1 + '\'' +
                ", items=" + items +
                '}';
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }
}
