package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-02 8:40
 * @Description:合同评审流程（SAP）
 */
public class ContractReviewReq{

    private String KNTTP;
    /**采购凭证类型*/
    private String  BSART;
    /**供应商描述*/
    private String  NAME_ORG1;
    /**创建对象的人员名称*/
    private String  ERNAM;
    /**采购组织*/
    private String  EKORG;
    /**采购组*/
    private String  BEDAT;
    /**付款条款*/
    private String  VTEXT;
    /**创建日期*/
    private String  AEDAT;
    /**币种*/
    private String  WAERS;
    /**采购凭证编号*/
    private String  EBELN;
    /**有效起始日期*/
    private String  KDATB;
    /**有效截止日期*/
    private String  KDATE;

    private List<ContractReviewDetailReq> item;

    public String getKDATB() {
        return KDATB;
    }

    public void setKDATB(String KDATB) {
        this.KDATB = KDATB;
    }

    public String getKDATE() {
        return KDATE;
    }

    public void setKDATE(String KDATE) {
        this.KDATE = KDATE;
    }

    public String getBSART() {
        return BSART;
    }

    public void setBSART(String BSART) {
        this.BSART = BSART;
    }

    public String getNAME_ORG1() {
        return NAME_ORG1;
    }

    public void setNAME_ORG1(String NAME_ORG1) {
        this.NAME_ORG1 = NAME_ORG1;
    }

    public String getERNAM() {
        return ERNAM;
    }

    public void setERNAM(String ERNAM) {
        this.ERNAM = ERNAM;
    }

    public String getEKORG() {
        return EKORG;
    }

    public void setEKORG(String EKORG) {
        this.EKORG = EKORG;
    }

    public String getBEDAT() {
        return BEDAT;
    }

    public void setBEDAT(String BEDAT) {
        this.BEDAT = BEDAT;
    }

    public String getVTEXT() {
        return VTEXT;
    }

    public void setVTEXT(String VTEXT) {
        this.VTEXT = VTEXT;
    }

    public String getAEDAT() {
        return AEDAT;
    }

    public void setAEDAT(String AEDAT) {
        this.AEDAT = AEDAT;
    }

    public String getWAERS() {
        return WAERS;
    }

    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }

    public List<ContractReviewDetailReq> getItem() {
        return item;
    }

    public void setItem(List<ContractReviewDetailReq> item) {
        this.item = item;
    }

    public String getKNTTP() {
        return KNTTP;
    }

    public void setKNTTP(String KNTTP) {
        this.KNTTP = KNTTP;
    }

    @Override
    public String toString() {
        return "ContractReviewReq{" +
                "KNTTP='" + KNTTP + '\'' +
                ", BSART='" + BSART + '\'' +
                ", NAME_ORG1='" + NAME_ORG1 + '\'' +
                ", ERNAM='" + ERNAM + '\'' +
                ", EKORG='" + EKORG + '\'' +
                ", BEDAT='" + BEDAT + '\'' +
                ", VTEXT='" + VTEXT + '\'' +
                ", AEDAT='" + AEDAT + '\'' +
                ", WAERS='" + WAERS + '\'' +
                ", EBELN='" + EBELN + '\'' +
                ", KDATB='" + KDATB + '\'' +
                ", KDATE='" + KDATE + '\'' +
                ", item=" + item +
                '}';
    }

    public String getEBELN() {
        return EBELN;
    }

    public void setEBELN(String EBELN) {
        this.EBELN = EBELN;
    }
}