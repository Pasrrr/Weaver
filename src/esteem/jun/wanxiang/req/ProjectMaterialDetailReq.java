package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class ProjectMaterialDetailReq {
    /**行*/
    private String ZEILE;
    /**项目号*/
    private String  AUFNR;
    /**项目名称*/
    private String  KTEXT;
    /**物料*/
    private String  MATNR;
    /**物料描述*/
    private String  MAKTX;
    /**领用数量*/
    private String MENGE;
    /**计量单位*/
    private String  MEINS;
    /**金额*/
    private String  DMBTR;
    /**预留单号*/
    private String  RSNUM;
    /**预留行*/
    private String  RSPOS;
    /**预留人*/
    private String  USNAM;
    /**预留人名称*/
    private String  NAME;

    @Override
    public String toString() {
        return "ProjectMaterialproofDetailReq{" +
                "ZEILE='" + ZEILE + '\'' +
                ", AUFNR='" + AUFNR + '\'' +
                ", KTEXT='" + KTEXT + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", MAKTX='" + MAKTX + '\'' +
                ", MENGE='" + MENGE + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", DMBTR='" + DMBTR + '\'' +
                ", RSNUM='" + RSNUM + '\'' +
                ", RSPOS='" + RSPOS + '\'' +
                ", USNAM='" + USNAM + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }

    public String getZEILE() {
        return ZEILE;
    }

    public void setZEILE(String ZEILE) {
        this.ZEILE = ZEILE;
    }

    public String getAUFNR() {
        return AUFNR;
    }

    public void setAUFNR(String AUFNR) {
        this.AUFNR = AUFNR;
    }

    public String getKTEXT() {
        return KTEXT;
    }

    public void setKTEXT(String KTEXT) {
        this.KTEXT = KTEXT;
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

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getDMBTR() {
        return DMBTR;
    }

    public void setDMBTR(String DMBTR) {
        this.DMBTR = DMBTR;
    }

    public String getRSNUM() {
        return RSNUM;
    }

    public void setRSNUM(String RSNUM) {
        this.RSNUM = RSNUM;
    }

    public String getRSPOS() {
        return RSPOS;
    }

    public void setRSPOS(String RSPOS) {
        this.RSPOS = RSPOS;
    }

    public String getUSNAM() {
        return USNAM;
    }

    public void setUSNAM(String USNAM) {
        this.USNAM = USNAM;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
