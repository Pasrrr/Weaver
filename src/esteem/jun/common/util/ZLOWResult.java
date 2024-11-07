package esteem.jun.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className: Result
 * @author: jun
 * @date: 2022-05-20 14:38
 * @Depiction:
 **/
public class ZLOWResult {
    /**最低订单单价日期*/
    private String ZLASTDT;
    /**最低订单单价日期*/
    private String ZLASTPR;
    /**最近订单单价日期*/
    private String ZLOWESTDT;
    /**最近订单单价*/
    private String ZLOWESTPR;

    @Override
    public String toString() {
        return "{" +
                "\"ZLASTDT\":\"" + ZLASTDT + '\"' +
                ", \"ZLASTPR\":\"" + ZLASTPR + '\"' +
                ", \"ZLOWESTDT\":\"" + ZLOWESTDT + '\"' +
                ", \"ZLOWESTPR\":\"" + ZLOWESTPR + '\"' +
                '}';
    }

    public String getZLASTDT() {
        return ZLASTDT;
    }

    public void setZLASTDT(String ZLASTDT) {
        this.ZLASTDT = ZLASTDT;
    }

    public String getZLASTPR() {
        return ZLASTPR;
    }

    public void setZLASTPR(String ZLASTPR) {
        this.ZLASTPR = ZLASTPR;
    }

    public String getZLOWESTDT() {
        return ZLOWESTDT;
    }

    public void setZLOWESTDT(String ZLOWESTDT) {
        this.ZLOWESTDT = ZLOWESTDT;
    }

    public String getZLOWESTPR() {
        return ZLOWESTPR;
    }

    public void setZLOWESTPR(String ZLOWESTPR) {
        this.ZLOWESTPR = ZLOWESTPR;
    }

}
