package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/15
 * @Description:
 */
public class SalesInvoiceDetailReq {
    /**	交货单	*/
    private String 	VBELN;
    /**	交货行	*/
    private String 	POSNR;
    /**	物料号	*/
    private String 	MATNR;
    /**	物料名称	*/
    private String 	ARKTX;
    /**	交货数量	*/
    private String 	LFIMG;
    /**	实际交货数量	*/
    private String 	LGMNG;
    /**	计量单位	*/
    private String 	MEINS;
    /**	创建人	*/
    private String 	ERNAM;
    /**	创建日期	*/
    private String 	ERDAT;
    /**	工厂	*/
    private String 	WERKS;
    /**	存储地点	*/
    private String 	LGORT;
    /**	参考凭证	*/
    private String 	VGBEL;
    /**	参考行 	*/
    private String 	VGPOS;
    /**	收货方 	*/
    private String 	KUNNR;
    /**	送达方	*/
    private String 	KUNAG;
    /**	物料凭证	*/
    private String 	MBLNR;
    /**	客户物料	*/
    private String 	KDMAT;

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN;
    }

    public String getPOSNR() {
        return POSNR;
    }

    public void setPOSNR(String POSNR) {
        this.POSNR = POSNR;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getARKTX() {
        return ARKTX;
    }

    public void setARKTX(String ARKTX) {
        this.ARKTX = ARKTX;
    }

    public String getLFIMG() {
        return LFIMG;
    }

    public void setLFIMG(String LFIMG) {
        this.LFIMG = LFIMG;
    }

    public String getLGMNG() {
        return LGMNG;
    }

    public void setLGMNG(String LGMNG) {
        this.LGMNG = LGMNG;
    }

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getERNAM() {
        return ERNAM;
    }

    public void setERNAM(String ERNAM) {
        this.ERNAM = ERNAM;
    }

    public String getERDAT() {
        return ERDAT;
    }

    public void setERDAT(String ERDAT) {
        this.ERDAT = ERDAT;
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

    public String getVGBEL() {
        return VGBEL;
    }

    public void setVGBEL(String VGBEL) {
        this.VGBEL = VGBEL;
    }

    public String getVGPOS() {
        return VGPOS;
    }

    public void setVGPOS(String VGPOS) {
        this.VGPOS = VGPOS;
    }

    public String getKUNNR() {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR) {
        this.KUNNR = KUNNR;
    }

    public String getKUNAG() {
        return KUNAG;
    }

    public void setKUNAG(String KUNAG) {
        this.KUNAG = KUNAG;
    }

    public String getMBLNR() {
        return MBLNR;
    }

    public void setMBLNR(String MBLNR) {
        this.MBLNR = MBLNR;
    }

    public String getKDMAT() {
        return KDMAT;
    }

    public void setKDMAT(String KDMAT) {
        this.KDMAT = KDMAT;
    }

    @Override
    public String toString() {
        return "SalesInvoiceReq{" +
                "VBELN='" + VBELN + '\'' +
                ", POSNR='" + POSNR + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", ARKTX='" + ARKTX + '\'' +
                ", LFIMG='" + LFIMG + '\'' +
                ", LGMNG='" + LGMNG + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", ERNAM='" + ERNAM + '\'' +
                ", ERDAT='" + ERDAT + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", LGORT='" + LGORT + '\'' +
                ", VGBEL='" + VGBEL + '\'' +
                ", VGPOS='" + VGPOS + '\'' +
                ", KUNNR='" + KUNNR + '\'' +
                ", KUNAG='" + KUNAG + '\'' +
                ", MBLNR='" + MBLNR + '\'' +
                ", KDMAT='" + KDMAT + '\'' +
                '}';
    }
}
