package esteem.jun.wanxiang.req;

import java.util.List;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-03 9:24
 * @Description:主数据审批流程
 */
public class MasterDateReq {
    /**用户名	*/
    private String 	USNAM;
    /**AgileEc号	*/
    private String AgileEc;
    /**新建说明	*/
    private String ReasonForChange;

    public String getReasonForChange() {
        return ReasonForChange;
    }

    public void setReasonForChange(String reasonForChange) {
        ReasonForChange = reasonForChange;
    }

    private List<MasterDateDetailReq> item;

    public String getUSNAM() {
        return USNAM;
    }

    public void setUSNAM(String USNAM) {
        this.USNAM = USNAM;
    }

    public String getAgileEc() {
        return AgileEc;
    }

    public void setAgileEc(String agileEc) {
        AgileEc = agileEc;
    }

    public List<MasterDateDetailReq> getItem() {
        return item;
    }

    public void setItem(List<MasterDateDetailReq> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "MasterDateReq{" +
                "USNAM='" + USNAM + '\'' +
                ", AgileEc='" + AgileEc + '\'' +
                ", ReasonForChange='" + ReasonForChange + '\'' +
                ", item=" + item +
                '}';
    }
}
