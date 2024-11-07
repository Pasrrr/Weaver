package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class ProjectMaterialproofDetailReq {
    /**行*/
    private String RSPOS;
    /**物料*/
    private String  MATNR;
    /**物料描述*/
    private String  MAKTX;
    /**领用数量*/
    private String  BDMNG;
    /**计量单位*/
    private String  MEINS;
    /**标准成本单价*/
    private String STDPRS;
    /**标准成本金额*/
    private String  STDAMT;
    /**工厂*/
    private String  WERKS;
    /**存储地点*/
    private String  LGORT;
    /**删除标识*/
    private String  XLOEK;
    /**总账科目*/
    private String  SAKNR;
    /**总账科目名称*/
    private String  TXT50;

    @Override
    public String toString() {
        return "ProjectMaterialDetailReq{" +
                "RSPOS='" + RSPOS + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", MAKTX='" + MAKTX + '\'' +
                ", BDMNG='" + BDMNG + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", STDPRS='" + STDPRS + '\'' +
                ", STDAMT='" + STDAMT + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", LGORT='" + LGORT + '\'' +
                ", XLOEK='" + XLOEK + '\'' +
                ", SAKNR='" + SAKNR + '\'' +
                ", TXT50='" + TXT50 + '\'' +
                '}';
    }

    public String getRSPOS() {
        return RSPOS;
    }

    public void setRSPOS(String RSPOS) {
        this.RSPOS = RSPOS;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getMAKTX() {
        return MAKTX;
    }

    public void setMAKTX(String MAKTX) {
        this.MAKTX = MAKTX;
    }

    public String getBDMNG() {
        return BDMNG;
    }

    public void setBDMNG(String BDMNG) {
        this.BDMNG = BDMNG;
    }

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getSTDPRS() {
        return STDPRS;
    }

    public void setSTDPRS(String STDPRS) {
        this.STDPRS = STDPRS;
    }

    public String getSTDAMT() {
        return STDAMT;
    }

    public void setSTDAMT(String STDAMT) {
        this.STDAMT = STDAMT;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getLGORT() {
        return LGORT;
    }

    public void setLGORT(String LGORT) {
        this.LGORT = LGORT;
    }

    public String getXLOEK() {
        return XLOEK;
    }

    public void setXLOEK(String XLOEK) {
        this.XLOEK = XLOEK;
    }

    public String getSAKNR() {
        return SAKNR;
    }

    public void setSAKNR(String SAKNR) {
        this.SAKNR = SAKNR;
    }

    public String getTXT50() {
        return TXT50;
    }

    public void setTXT50(String TXT50) {
        this.TXT50 = TXT50;
    }
}
