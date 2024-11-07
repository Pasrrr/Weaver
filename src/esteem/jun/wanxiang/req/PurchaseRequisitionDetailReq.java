package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-30 10:31
 * @Description:采购申请流程
 */
public class PurchaseRequisitionDetailReq {
    /**采购申请行项目号*/
    private String BNFPO;
    /**科目分配类别*/
    private String KNTTP;
    /**项目类别*/
    private String PSTYP;
    /**成本中心*/
    private String KOSTL;
    /**物料编码*/
    private String MATNR;
    /**物料描述*/
    private String TXZ01;
    /**申请数量*/
    private String MENGE;
    /**单位*/
    private String MEINS;
    /**交货日期*/
    private String LFDAT;
    /**物料组*/
    private String MATKL;
    /**工厂*/
    private String WERKS;
    /**采购组*/
    private String EKGRP;

    /**采购申请中的价格*/
    private String PREIS;
    /**价格单位*/
    private String PEINH;

    /**删除标识*/
    private String LOEKZ;
    /**批准标识*/
    private String FRGKZ;
    /**品牌*/
    private String ZBRAND;
    /**是否已签合同*/
    private String ZCONTRACT;
    /**所属预算*/
    private String ZBUDGET;
    /**申请理由*/
    private String ZREASON;
    /**使用产线*/
    private String ZSHYCHX;
    /**具体使用设备*/
    private String ZSHYSHB;

    /**需求人*/
    private String AFNAM;
    /**	最低订单单价 	*/
    private String 	ZLOWESTPR;
    /**	最低订单单价日期 	*/
    private String 	ZLOWESTDT;
    /**	最近订单单价 	*/
    private String 	ZLASTPR;
    /**	最近订单单价日期	*/
    private String 	ZLASTDT;
    /**	币种	*/
    private String 	WAERS;

    public String getWAERS() {
        return WAERS;
    }

    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }

    public String getZLOWESTPR() {
        return ZLOWESTPR;
    }

    public void setZLOWESTPR(String ZLOWESTPR) {
        this.ZLOWESTPR = ZLOWESTPR;
    }

    public String getZLOWESTDT() {
        return ZLOWESTDT;
    }

    public void setZLOWESTDT(String ZLOWESTDT) {
        this.ZLOWESTDT = ZLOWESTDT;
    }

    public String getZLASTPR() {
        return ZLASTPR;
    }

    public void setZLASTPR(String ZLASTPR) {
        this.ZLASTPR = ZLASTPR;
    }

    public String getZLASTDT() {
        return ZLASTDT;
    }

    public void setZLASTDT(String ZLASTDT) {
        this.ZLASTDT = ZLASTDT;
    }

    public String getBNFPO() {
        return BNFPO;
    }

    public void setBNFPO(String BNFPO) {
        this.BNFPO = BNFPO;
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

    public String getKOSTL() {
        return KOSTL;
    }

    public void setKOSTL(String KOSTL) {
        this.KOSTL = KOSTL;
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

    public String getLFDAT() {
        return LFDAT;
    }

    public void setLFDAT(String LFDAT) {
        this.LFDAT = LFDAT;
    }

    public String getMATKL() {
        return MATKL;
    }

    public void setMATKL(String MATKL) {
        this.MATKL = MATKL;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getEKGRP() {
        return EKGRP;
    }

    public void setEKGRP(String EKGRP) {
        this.EKGRP = EKGRP;
    }



    public String getPREIS() {
        return PREIS;
    }

    public void setPREIS(String PREIS) {
        this.PREIS = PREIS;
    }

    public String getPEINH() {
        return PEINH;
    }

    public void setPEINH(String PEINH) {
        this.PEINH = PEINH;
    }

    public String getLOEKZ() {
        return LOEKZ;
    }

    public void setLOEKZ(String LOEKZ) {
        this.LOEKZ = LOEKZ;
    }

    public String getFRGKZ() {
        return FRGKZ;
    }

    public void setFRGKZ(String FRGKZ) {
        this.FRGKZ = FRGKZ;
    }

    public String getZBRAND() {
        return ZBRAND;
    }

    public void setZBRAND(String ZBRAND) {
        this.ZBRAND = ZBRAND;
    }

    public String getZCONTRACT() {
        return ZCONTRACT;
    }

    public void setZCONTRACT(String ZCONTRACT) {
        this.ZCONTRACT = ZCONTRACT;
    }

    public String getZBUDGET() {
        return ZBUDGET;
    }

    public void setZBUDGET(String ZBUDGET) {
        this.ZBUDGET = ZBUDGET;
    }

    public String getZREASON() {
        return ZREASON;
    }

    public void setZREASON(String ZREASON) {
        this.ZREASON = ZREASON;
    }

    public String getZSHYCHX() {
        return ZSHYCHX;
    }

    public void setZSHYCHX(String ZSHYCHX) {
        this.ZSHYCHX = ZSHYCHX;
    }

    public String getZSHYSHB() {
        return ZSHYSHB;
    }

    public void setZSHYSHB(String ZSHYSHB) {
        this.ZSHYSHB = ZSHYSHB;
    }

    public String getAFNAM() {
        return AFNAM;
    }

    public void setAFNAM(String AFNAM) {
        this.AFNAM = AFNAM;
    }

    @Override
    public String toString() {
        return "PurchaseRequisitionDetailReq{" +
                "BNFPO='" + BNFPO + '\'' +
                ", KNTTP='" + KNTTP + '\'' +
                ", PSTYP='" + PSTYP + '\'' +
                ", KOSTL='" + KOSTL + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", TXZ01='" + TXZ01 + '\'' +
                ", MENGE='" + MENGE + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", LFDAT='" + LFDAT + '\'' +
                ", MATKL='" + MATKL + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", EKGRP='" + EKGRP + '\'' +
                ", PREIS='" + PREIS + '\'' +
                ", PEINH='" + PEINH + '\'' +
                ", LOEKZ='" + LOEKZ + '\'' +
                ", FRGKZ='" + FRGKZ + '\'' +
                ", ZBRAND='" + ZBRAND + '\'' +
                ", ZCONTRACT='" + ZCONTRACT + '\'' +
                ", ZBUDGET='" + ZBUDGET + '\'' +
                ", ZREASON='" + ZREASON + '\'' +
                ", ZSHYCHX='" + ZSHYCHX + '\'' +
                ", ZSHYSHB='" + ZSHYSHB + '\'' +
                ", AFNAM='" + AFNAM + '\'' +
                ", ZLOWESTPR='" + ZLOWESTPR + '\'' +
                ", ZLOWESTDT='" + ZLOWESTDT + '\'' +
                ", ZLASTPR='" + ZLASTPR + '\'' +
                ", ZLASTDT='" + ZLASTDT + '\'' +
                ", WAERS='" + WAERS + '\'' +
                '}';
    }
}