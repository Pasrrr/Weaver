package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-01 8:28
 * @Description:原材料来料检验 -对应  IQC数据库流程（SAP） 571
 */
public class RawMaterialCheckReq {

    /**申请人*/
    private String ERNAM;
    /**检验批编号*/
    private String PRUEFLOS;
    /**物料号*/
    private String MATNR;
    /**物料名称*/
    private String maktx;
    /**SAP批次号*/
    private String CHARG;
    /**万向批次号*/
    private String ZZBATCH;
    /**生产日期*/
    private String HSDAT;
    /**供应商编码*/
    private String LIFNR;
    /**供应商名称*/
    private String NAME1;
    /**工厂*/
    private String WERKS;
    /**库存地点*/
    private String LAGORTVORG;
    /**检验数量*/
    private String LOSMENGE;
    /**单位*/
    private String MENGENEINH;
    /**所需开始日期*/
    private String PASTRTERM;
    /**要求结束日期*/
    private String PAENDTERM;

    public String getERNAM() {
        return ERNAM;
    }

    public void setERNAM(String ERNAM) {
        this.ERNAM = ERNAM;
    }

    public String getPRUEFLOS() {
        return PRUEFLOS;
    }

    public void setPRUEFLOS(String PRUEFLOS) {
        this.PRUEFLOS = PRUEFLOS;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getCHARG() {
        return CHARG;
    }

    public void setCHARG(String CHARG) {
        this.CHARG = CHARG;
    }

    public String getZZBATCH() {
        return ZZBATCH;
    }

    public void setZZBATCH(String ZZBATCH) {
        this.ZZBATCH = ZZBATCH;
    }

    public String getHSDAT() {
        return HSDAT;
    }

    public void setHSDAT(String HSDAT) {
        this.HSDAT = HSDAT;
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
    }

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getLAGORTVORG() {
        return LAGORTVORG;
    }

    public void setLAGORTVORG(String LAGORTVORG) {
        this.LAGORTVORG = LAGORTVORG;
    }

    public String getLOSMENGE() {
        return LOSMENGE;
    }

    public void setLOSMENGE(String LOSMENGE) {
        this.LOSMENGE = LOSMENGE;
    }

    public String getMENGENEINH() {
        return MENGENEINH;
    }

    public void setMENGENEINH(String MENGENEINH) {
        this.MENGENEINH = MENGENEINH;
    }

    public String getPASTRTERM() {
        return PASTRTERM;
    }

    public void setPASTRTERM(String PASTRTERM) {
        this.PASTRTERM = PASTRTERM;
    }

    public String getPAENDTERM() {
        return PAENDTERM;
    }

    public void setPAENDTERM(String PAENDTERM) {
        this.PAENDTERM = PAENDTERM;
    }

    @Override
    public String toString() {
        return "RawMaterialCheckReq{" +
                "PRUEFLOS='" + PRUEFLOS + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", maktx='" + maktx + '\'' +
                ", CHARG='" + CHARG + '\'' +
                ", ZZBATCH='" + ZZBATCH + '\'' +
                ", HSDAT='" + HSDAT + '\'' +
                ", LIFNR='" + LIFNR + '\'' +
                ", NAME1='" + NAME1 + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", LAGORTVORG='" + LAGORTVORG + '\'' +
                ", LOSMENGE='" + LOSMENGE + '\'' +
                ", MENGENEINH='" + MENGENEINH + '\'' +
                ", PASTRTERM='" + PASTRTERM + '\'' +
                ", PAENDTERM='" + PAENDTERM + '\'' +
                '}';
    }
}