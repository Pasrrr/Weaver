package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-03 8:08
 * @Description:
 */
public class PurchaseOrderDetailReq {

    /**	资产编号 	*/
    private String 	ANLN1;
    /**	采购凭证的项目编号 	*/
    private String 	EBELP;
    /**	科目分配类别	*/
    private String 	KNTTP;
    /**	项目类别	*/
    private String 	PSTYP;
    /**	采购凭证删除标	*/
    private String 	LOEKZ;
    /**	物料编号	*/
    private String 	MATNR;
    /**	工厂	*/
    private String 	WERKS;
    /**	采购信息记录号	*/
    private String 	INFNR	;
    /**	采购订单数量	*/
    private String 	MENGE	;
    /**	采购订单计量单位	*/
    private String 	MEINS	;
    /**	订单价格单位(采购)	*/
    private String 	BPRME	;
    /**	单价	*/
    private String 	NETPR	;
    /**	总价	*/
    private String 	NETWR	;
    /**	采购申请编号	*/
    private String 	BANFN	;
    /**	采购申请的项目编号 	*/
    private String 	BNFPO	;
    /**	成本中心	*/
    private String 	KOSTL	;
    /**	重要的采购协议号	*/
    private String 	KONNR	;
    /**	基本采购协议的项目编号 	*/
    private String 	KTPNR	;
    /**物料描述*/
    private String TXZ01;
    /**采购申请单价*/
    private String PREIS;


    public String getPREIS() {
        return PREIS;
    }

    public void setPREIS(String PREIS) {
        this.PREIS = PREIS;
    }

    public String getANLN1() {
        return ANLN1;
    }

    public void setANLN1(String ANLN1) {
        this.ANLN1 = ANLN1;
    }

    public String getTXZ01() {
        return TXZ01;
    }

    public void setTXZ01(String TXZ01) {
        this.TXZ01 = TXZ01;
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

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getINFNR() {
        return INFNR;
    }

    public void setINFNR(String INFNR) {
        this.INFNR = INFNR;
    }

    public String getMENGE() {
        return MENGE;
    }

    public void setMENGE(String MENGE) {
        this.MENGE = MENGE;
    }

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getBPRME() {
        return BPRME;
    }

    public void setBPRME(String BPRME) {
        this.BPRME = BPRME;
    }

    public String getNETPR() {
        return NETPR;
    }

    public void setNETPR(String NETPR) {
        this.NETPR = NETPR;
    }

    public String getNETWR() {
        return NETWR;
    }

    public void setNETWR(String NETWR) {
        this.NETWR = NETWR;
    }

    public String getBANFN() {
        return BANFN;
    }

    public void setBANFN(String BANFN) {
        this.BANFN = BANFN;
    }

    public String getBNFPO() {
        return BNFPO;
    }

    public void setBNFPO(String BNFPO) {
        this.BNFPO = BNFPO;
    }

    public String getKOSTL() {
        return KOSTL;
    }

    public void setKOSTL(String KOSTL) {
        this.KOSTL = KOSTL;
    }

    public String getKONNR() {
        return KONNR;
    }

    public void setKONNR(String KONNR) {
        this.KONNR = KONNR;
    }

    public String getKTPNR() {
        return KTPNR;
    }

    public void setKTPNR(String KTPNR) {
        this.KTPNR = KTPNR;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetailReq{" +
                "ANLN1='" + ANLN1 + '\'' +
                ", EBELP='" + EBELP + '\'' +
                ", KNTTP='" + KNTTP + '\'' +
                ", PSTYP='" + PSTYP + '\'' +
                ", LOEKZ='" + LOEKZ + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", INFNR='" + INFNR + '\'' +
                ", MENGE='" + MENGE + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", BPRME='" + BPRME + '\'' +
                ", NETPR='" + NETPR + '\'' +
                ", NETWR='" + NETWR + '\'' +
                ", BANFN='" + BANFN + '\'' +
                ", BNFPO='" + BNFPO + '\'' +
                ", KOSTL='" + KOSTL + '\'' +
                ", KONNR='" + KONNR + '\'' +
                ", KTPNR='" + KTPNR + '\'' +
                ", TXZ01='" + TXZ01 + '\'' +
                ", PREIS='" + PREIS + '\'' +
                '}';
    }
}