package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-03 9:24
 * @Description:发票入账审批流程
 */
public class InvoiceEntryReq {

    /**公司代码*/
    private String 	BUKRS;
    /**会计凭证的凭证编号 */
    private String 	BELNR;
    /**会计年度*/
    private String 	GJAHR;
    /**凭证类型*/
    private String 	BLART;
    /**凭证中的凭证日期*/
    private String 	 BLDAT;
    /**凭证中的过账日期*/
    private String 	BUDAT;
    /**会计期间	*/
    private String 	 MONAT;
    /**会计凭证输入日期*/
    private String 	CPUDT;
    /**用户名	*/
    private String 	USNAM;
    /**参考凭证编号*/
    private String 	ZBZZD;
    /**冲销凭证号 */
    private String 	STBLG;
    /**冲销凭证的会计年度 */
    private String 	STJAH;
    /**凭证抬头文本*/
    private String 	 BKTXT;
    /**货币码 */
    private String 	WAERS;
    /**未计划的运费*/
    private String 	FRATH;
    /**供应商描述*/
    private String 	NAME_ORG1;
    /**供应商编码*/
    private String 	LIFNR;

    /**以凭证货币计的税额*/
    private String 	wmwst1;

    /**付款代码*/
    private String 	ZTERM;

    private List<InvoiceEntryDetailReq> item;

    public String getZTERM() {
        return ZTERM;
    }

    public void setZTERM(String ZTERM) {
        this.ZTERM = ZTERM;
    }

    public String getBUKRS() {
        return BUKRS;
    }

    public void setBUKRS(String BUKRS) {
        this.BUKRS = BUKRS;
    }

    public String getBELNR() {
        return BELNR;
    }

    public void setBELNR(String BELNR) {
        this.BELNR = BELNR;
    }

    public String getGJAHR() {
        return GJAHR;
    }

    public void setGJAHR(String GJAHR) {
        this.GJAHR = GJAHR;
    }

    public String getBLART() {
        return BLART;
    }

    public void setBLART(String BLART) {
        this.BLART = BLART;
    }

    public String getBLDAT() {
        return BLDAT;
    }

    public void setBLDAT(String BLDAT) {
        this.BLDAT = BLDAT;
    }

    public String getBUDAT() {
        return BUDAT;
    }

    public void setBUDAT(String BUDAT) {
        this.BUDAT = BUDAT;
    }

    public String getMONAT() {
        return MONAT;
    }

    public void setMONAT(String MONAT) {
        this.MONAT = MONAT;
    }

    public String getCPUDT() {
        return CPUDT;
    }

    public void setCPUDT(String CPUDT) {
        this.CPUDT = CPUDT;
    }

    public String getUSNAM() {
        return USNAM;
    }

    public void setUSNAM(String USNAM) {
        this.USNAM = USNAM;
    }


    public String getZBZZD() {
        return ZBZZD;
    }

    public void setZBZZD(String ZBZZD) {
        this.ZBZZD = ZBZZD;
    }

    public String getSTBLG() {
        return STBLG;
    }

    public void setSTBLG(String STBLG) {
        this.STBLG = STBLG;
    }

    public String getSTJAH() {
        return STJAH;
    }

    public void setSTJAH(String STJAH) {
        this.STJAH = STJAH;
    }

    public String getBKTXT() {
        return BKTXT;
    }

    public void setBKTXT(String BKTXT) {
        this.BKTXT = BKTXT;
    }

    public String getWAERS() {
        return WAERS;
    }

    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }

    public String getFRATH() {
        return FRATH;
    }

    public void setFRATH(String FRATH) {
        this.FRATH = FRATH;
    }

    public String getNAME_ORG1() {
        return NAME_ORG1;
    }

    public void setNAME_ORG1(String NAME_ORG1) {
        this.NAME_ORG1 = NAME_ORG1;
    }

    public List<InvoiceEntryDetailReq> getItem() {
        return item;
    }

    public void setItem(List<InvoiceEntryDetailReq> item) {
        this.item = item;
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
    }

    public String getWmwst1() {
        return wmwst1;
    }

    public void setWmwst1(String wmwst1) {
        this.wmwst1 = wmwst1;
    }

    @Override
    public String toString() {
        return "InvoiceEntryReq{" +
                "BUKRS='" + BUKRS + '\'' +
                ", BELNR='" + BELNR + '\'' +
                ", GJAHR='" + GJAHR + '\'' +
                ", BLART='" + BLART + '\'' +
                ", BLDAT='" + BLDAT + '\'' +
                ", BUDAT='" + BUDAT + '\'' +
                ", MONAT='" + MONAT + '\'' +
                ", CPUDT='" + CPUDT + '\'' +
                ", USNAM='" + USNAM + '\'' +
                ", ZBZZD='" + ZBZZD + '\'' +
                ", STBLG='" + STBLG + '\'' +
                ", STJAH='" + STJAH + '\'' +
                ", BKTXT='" + BKTXT + '\'' +
                ", WAERS='" + WAERS + '\'' +
                ", FRATH='" + FRATH + '\'' +
                ", NAME_ORG1='" + NAME_ORG1 + '\'' +
                ", LIFNR='" + LIFNR + '\'' +
                ", wmwst1='" + wmwst1 + '\'' +
                ", ZTERM='" + ZTERM + '\'' +
                ", item=" + item +
                '}';
    }
}