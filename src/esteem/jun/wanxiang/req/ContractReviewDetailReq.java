package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-03 8:28
 * @Description:
 */
public class ContractReviewDetailReq {
    /**有效开始日期*/
    private String  KDATB;
    /**有效结束日期*/
    private String  KDATE;
    /**审批策略*/
    private String  FRGSX;
    /**采购凭证编号*/
    private String  EBELN;
    /**采购凭证的项目编号*/
    private String  EBELP;
    /**科目分配类别*/
    private String  KNTTP;
    /**项目类别*/
    private String  PSTYP;
    /**采购凭证删除标识*/
    private String  LOEKZ;
    /**物料编号*/
    private String  MATNR;
    /**物料描述*/
    private String  TXZ01;
    /**工厂*/
    private String  WERKS;
    /**目标数量*/
    private String  KTMNG;
    /**采购订单计量单位*/
    private String  MEINS;
    /**单价*/
    private String  NETPR;
    /**总价*/
    private String  BRTWR;
    /**申请人*/
    private String  AFNAM;

    public String getAFNAM() {
        return AFNAM;
    }

    public void setAFNAM(String AFNAM) {
        this.AFNAM = AFNAM;
    }

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

    public String getFRGSX() {
        return FRGSX;
    }

    public void setFRGSX(String FRGSX) {
        this.FRGSX = FRGSX;
    }

    public String getEBELN() {
        return EBELN;
    }

    public void setEBELN(String EBELN) {
        this.EBELN = EBELN;
    }

    public String getEBELP() {
        return EBELP;
    }

    public void setEBELP(String EBELP) {
        this.EBELP = EBELP;
    }

    public String getKNTTP() {
        return KNTTP;
    }

    public void setKNTTP(String KNTTP) {
        this.KNTTP = KNTTP;
    }

    public String getPSTYP() {
        return PSTYP;
    }

    public void setPSTYP(String PSTYP) {
        this.PSTYP = PSTYP;
    }

    public String getLOEKZ() {
        return LOEKZ;
    }

    public void setLOEKZ(String LOEKZ) {
        this.LOEKZ = LOEKZ;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getTXZ01() {
        return TXZ01;
    }

    public void setTXZ01(String TXZ01) {
        this.TXZ01 = TXZ01;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getKTMNG() {
        return KTMNG;
    }

    public void setKTMNG(String KTMNG) {
        this.KTMNG = KTMNG;
    }

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getNETPR() {
        return NETPR;
    }

    public void setNETPR(String NETPR) {
        this.NETPR = NETPR;
    }

    public String getBRTWR() {
        return BRTWR;
    }

    public void setBRTWR(String BRTWR) {
        this.BRTWR = BRTWR;
    }

    @Override
    public String toString() {
        return "ContractReviewDetailReq{" +
                "KDATB='" + KDATB + '\'' +
                ", KDATE='" + KDATE + '\'' +
                ", FRGSX='" + FRGSX + '\'' +
                ", EBELN='" + EBELN + '\'' +
                ", EBELP='" + EBELP + '\'' +
                ", KNTTP='" + KNTTP + '\'' +
                ", PSTYP='" + PSTYP + '\'' +
                ", LOEKZ='" + LOEKZ + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", TXZ01='" + TXZ01 + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", KTMNG='" + KTMNG + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", NETPR='" + NETPR + '\'' +
                ", BRTWR='" + BRTWR + '\'' +
                ", AFNAM='" + AFNAM + '\'' +
                '}';
    }
}