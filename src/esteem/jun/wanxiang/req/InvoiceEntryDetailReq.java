package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-03 9:34
 * @Description:
 */
public class InvoiceEntryDetailReq {
    /**凭证货币金额*/
    private String 	WRBTR;
    /**以凭证货币计的税额*/
    private String 	 WMWST;
    /**基准日期	*/
    private String 	ZFBDT;
    /**付款条件代码*/
    private String 	ZTERM;
    /**采购订单号*/
    private String 	EBELN;
    /**采购订单行项目号	*/
    private String 	EBELP;
    /**供应商编号	*/
    private String 	 LIFNR;

    /**物料编码	*/
    private String 	 MATNR;

    /**物料描述	*/
    private String 	 MAKTX;

    /**数量	*/
    private String 	 MENGE;
    /**收货数量	*/
    private String 	 MENGE1;
    /**参考	*/
    private String 	 XBLNR_MKPF;
    /**订单价格	*/
    private String 	 BRTWR;
    /**采购申请价格	*/
    private String 	 GSWRT;

    public String getBRTWR() {
        return BRTWR;
    }

    public void setBRTWR(String BRTWR) {
        this.BRTWR = BRTWR;
    }

    public String getGSWRT() {
        return GSWRT;
    }

    public void setGSWRT(String GSWRT) {
        this.GSWRT = GSWRT;
    }

    public String getMENGE1() {
        return MENGE1;
    }

    public void setMENGE1(String MENGE1) {
        this.MENGE1 = MENGE1;
    }

    public String getXBLNR_MKPF() {
        return XBLNR_MKPF;
    }

    public void setXBLNR_MKPF(String XBLNR_MKPF) {
        this.XBLNR_MKPF = XBLNR_MKPF;
    }

    public String getWRBTR() {
        return WRBTR;
    }

    public void setWRBTR(String WRBTR) {
        this.WRBTR = WRBTR;
    }

    public String getWMWST() {
        return WMWST;
    }

    public void setWMWST(String WMWST) {
        this.WMWST = WMWST;
    }

    public String getZFBDT() {
        return ZFBDT;
    }

    public void setZFBDT(String ZFBDT) {
        this.ZFBDT = ZFBDT;
    }

    public String getZTERM() {
        return ZTERM;
    }

    public void setZTERM(String ZTERM) {
        this.ZTERM = ZTERM;
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

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
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

    public String getMENGE() {
        return MENGE;
    }

    public void setMENGE(String MENGE) {
        this.MENGE = MENGE;
    }

    @Override
    public String toString() {
        return "InvoiceEntryDetailReq{" +
                "WRBTR='" + WRBTR + '\'' +
                ", WMWST='" + WMWST + '\'' +
                ", ZFBDT='" + ZFBDT + '\'' +
                ", ZTERM='" + ZTERM + '\'' +
                ", EBELN='" + EBELN + '\'' +
                ", EBELP='" + EBELP + '\'' +
                ", LIFNR='" + LIFNR + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", MAKTX='" + MAKTX + '\'' +
                ", MENGE='" + MENGE + '\'' +
                ", MENGE1='" + MENGE1 + '\'' +
                ", XBLNR_MKPF='" + XBLNR_MKPF + '\'' +
                ", BRTWR='" + BRTWR + '\'' +
                ", GSWRT='" + GSWRT + '\'' +
                '}';
    }
}