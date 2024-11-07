package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-01 8:28
 * @Description:原材料不合格处理流程（SAP）
 */
public class RawMaterialUnqualifiedReq {
    /**SAP质量通知单编码*/
    private String QMNUM;
    /**创建人*/
    private String ERNAM;
    /**创建日期*/
    private String ERDAT;
    /**物料编号*/
    private String MATNR;
    /**物料描述*/
    private String maktx;
    /**供应商描述*/
    private String NAME1;
    /**检验数量*/
    private String BZMNG;
    /**缺陷类型代码组描述*/
    private String KURZTEXT;
    /**万向批次号*/
    private String ZZBATCH;

    public String getQMNUM() {
        return QMNUM;
    }

    public void setQMNUM(String QMNUM) {
        this.QMNUM = QMNUM;
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

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1;
    }

    public String getBZMNG() {
        return BZMNG;
    }

    public void setBZMNG(String BZMNG) {
        this.BZMNG = BZMNG;
    }

    public String getKURZTEXT() {
        return KURZTEXT;
    }

    public void setKURZTEXT(String KURZTEXT) {
        this.KURZTEXT = KURZTEXT;
    }

    public String getZZBATCH() {
        return ZZBATCH;
    }

    public void setZZBATCH(String ZZBATCH) {
        this.ZZBATCH = ZZBATCH;
    }

    @Override
    public String toString() {
        return "RawMaterialUnqualifiedReq{" +
                "QMNUM='" + QMNUM + '\'' +
                ", ERNAM='" + ERNAM + '\'' +
                ", ERDAT='" + ERDAT + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", maktx='" + maktx + '\'' +
                ", NAME1='" + NAME1 + '\'' +
                ", BZMNG='" + BZMNG + '\'' +
                ", KURZTEXT='" + KURZTEXT + '\'' +
                ", ZZBATCH='" + ZZBATCH + '\'' +
                '}';
    }
}