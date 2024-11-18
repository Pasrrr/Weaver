package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-02 7:58
 * @Description:采购订单
 */
public class PurchaseOrderReq {
    /**	付款类型编码	*/
    private String 	ZTERM;
    /**	付款类型描述	*/
    private String 	TEXT1;
    /**	货币类型	*/
    private String 	WAERS;
    /**	汇率	*/
    private String 	WKURS;
    /**	公司代码	*/
    private String 	BUKRS;
    /**	创建对象的人员名称	*/
    private String 	ERNAM;
    /**	采购凭证类别	*/
    private String 	BSTYP;
    /**	采购凭证类型	*/
    private String 	BSART;
    /**	供应商帐户号	*/
    private String 	LIFNR;
    /**	采购组织	*/
    private String 	EKORG;
    /**	采购凭证日期	*/
    private String 	BEDAT;
    /**	审批策略	*/
    private String 	FRGSX;
    /**	批准标识：采购凭证 	*/
    private String 	FRGKE;
    /**	发布状态 	*/
    private String 	FRGZU;
    /**	采购凭证编号	*/
    private String 	EBELN;
    /**	固定资产类别	*/
    private String 	ANLKL;
    /**	不含税价 	*/
    private String 	NETWR;
    /**	含税价	*/
    private String 	BRTWR;
    /**	万向采购进项税	*/
    private String 	ZSE;

    public String getNETWR() {
        return NETWR;
    }

    public void setNETWR(String NETWR) {
        this.NETWR = NETWR;
    }

    public String getBRTWR() {
        return BRTWR;
    }

    public void setBRTWR(String BRTWR) {
        this.BRTWR = BRTWR;
    }

    public String getZSE() {
        return ZSE;
    }

    public void setZSE(String ZSE) {
        this.ZSE = ZSE;
    }

    public String getZTERM() {
        return ZTERM;
    }

    public void setZTERM(String ZTERM) {
        this.ZTERM = ZTERM;
    }

    public String getTEXT1() {
        return TEXT1;
    }

    public void setTEXT1(String TEXT1) {
        this.TEXT1 = TEXT1;
    }

    public String getWAERS() {
        return WAERS;
    }

    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }

    public String getWKURS() {
        return WKURS;
    }

    public void setWKURS(String WKURS) {
        this.WKURS = WKURS;
    }

    private List<PurchaseOrderDetailReq> item;

    public String getANLKL() {
        return ANLKL;
    }

    public void setANLKL(String ANLKL) {
        this.ANLKL = ANLKL;
    }

    public String getBUKRS() {
        return BUKRS;
    }

    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }

    public String getERNAM() {
        return ERNAM;
    }

    public void setERNAM(String ERNAM) {
        this.ERNAM = ERNAM;
    }

    public String getBSTYP() {
        return BSTYP;
    }

    public void setBSTYP(String BSTYP) {
        this.BSTYP = BSTYP;
    }

    public String getBSART() {
        return BSART;
    }

    public void setBSART(String BSART) {
        this.BSART = BSART;
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
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

    public String getFRGSX() {
        return FRGSX;
    }

    public void setFRGSX(String FRGSX) {
        this.FRGSX = FRGSX;
    }

    public String getFRGKE() {
        return FRGKE;
    }

    public void setFRGKE(String FRGKE) {
        this.FRGKE = FRGKE;
    }

    public String getFRGZU() {
        return FRGZU;
    }

    public void setFRGZU(String FRGZU) {
        this.FRGZU = FRGZU;
    }

    public String getEBELN() {
        return EBELN;
    }

    public void setEBELN(String EBELN) {
        this.EBELN = EBELN;
    }

    @Override
    public String toString() {
        return "PurchaseOrderReq{" +
                "ZTERM='" + ZTERM + '\'' +
                ", TEXT1='" + TEXT1 + '\'' +
                ", WAERS='" + WAERS + '\'' +
                ", WKURS='" + WKURS + '\'' +
                ", BUKRS='" + BUKRS + '\'' +
                ", ERNAM='" + ERNAM + '\'' +
                ", BSTYP='" + BSTYP + '\'' +
                ", BSART='" + BSART + '\'' +
                ", LIFNR='" + LIFNR + '\'' +
                ", EKORG='" + EKORG + '\'' +
                ", BEDAT='" + BEDAT + '\'' +
                ", FRGSX='" + FRGSX + '\'' +
                ", FRGKE='" + FRGKE + '\'' +
                ", FRGZU='" + FRGZU + '\'' +
                ", EBELN='" + EBELN + '\'' +
                ", ANLKL='" + ANLKL + '\'' +
                ", NETWR='" + NETWR + '\'' +
                ", BRTWR='" + BRTWR + '\'' +
                ", ZSE='" + ZSE + '\'' +
                ", item=" + item +
                '}';
    }

    public List<PurchaseOrderDetailReq> getItem() {
        return item;
    }

    public void setItem(List<PurchaseOrderDetailReq> item) {
        this.item = item;
    }
}