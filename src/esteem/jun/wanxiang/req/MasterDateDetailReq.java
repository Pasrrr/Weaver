package esteem.jun.wanxiang.req;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-30 10:31
 * @Description:主数据创建流程
 */
public class MasterDateDetailReq {
    /**旧物料号*/
    private String BISMT;
    /**SLED的期间标识*/
    private String IPRKZ;
    /**物料描述*/
    private String MAKTX;
    /**物料组*/
    private String MATKL;
    /**物料编码*/
    private String MATNR;
    /**行业领域*/
    private String MBRSH;
    /**基本计量单位*/
    private String MEINS;
    /**总货架寿命*/
    private String MHDHB;
    /**最小剩余货架寿命*/
    private String MHDRZ;
    /**物料类型*/
    private String MTART;
    /**利润中心*/
    private String PRCTR;
    /**工厂*/
    private String WERKS;
    /**是否启用批次*/
    private String XCHPF;
    /**成品等级*/
    private String ZMLEVEL;
    /**物料品类*/
    private String ZSORT;
    /**物料状态*/
    private String ZSTATUS;
    /**质检字段*/
    private String List21;

    public String getList21() {
        return List21;
    }

    public void setList21(String list21) {
        List21 = list21;
    }

    public String getBISMT() {
        return BISMT;
    }

    public void setBISMT(String BISMT) {
        this.BISMT = BISMT;
    }

    public String getIPRKZ() {
        return IPRKZ;
    }

    public void setIPRKZ(String IPRKZ) {
        this.IPRKZ = IPRKZ;
    }

    public String getMAKTX() {
        return MAKTX;
    }

    public void setMAKTX(String MAKTX) {
        this.MAKTX = MAKTX;
    }

    public String getMATKL() {
        return MATKL;
    }

    public void setMATKL(String MATKL) {
        this.MATKL = MATKL;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getMBRSH() {
        return MBRSH;
    }

    public void setMBRSH(String MBRSH) {
        this.MBRSH = MBRSH;
    }

    public String getMEINS() {
        return MEINS;
    }

    public void setMEINS(String MEINS) {
        this.MEINS = MEINS;
    }

    public String getMHDHB() {
        return MHDHB;
    }

    public void setMHDHB(String MHDHB) {
        this.MHDHB = MHDHB;
    }

    public String getMHDRZ() {
        return MHDRZ;
    }

    public void setMHDRZ(String MHDRZ) {
        this.MHDRZ = MHDRZ;
    }

    public String getMTART() {
        return MTART;
    }

    public void setMTART(String MTART) {
        this.MTART = MTART;
    }

    public String getPRCTR() {
        return PRCTR;
    }

    public void setPRCTR(String PRCTR) {
        this.PRCTR = PRCTR;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getXCHPF() {
        return XCHPF;
    }

    public void setXCHPF(String XCHPF) {
        this.XCHPF = XCHPF;
    }

    public String getZMLEVEL() {
        return ZMLEVEL;
    }

    public void setZMLEVEL(String ZMLEVEL) {
        this.ZMLEVEL = ZMLEVEL;
    }

    public String getZSORT() {
        return ZSORT;
    }

    public void setZSORT(String ZSORT) {
        this.ZSORT = ZSORT;
    }

    public String getZSTATUS() {
        return ZSTATUS;
    }

    public void setZSTATUS(String ZSTATUS) {
        this.ZSTATUS = ZSTATUS;
    }

    @Override
    public String toString() {
        return "MasterDateDetailReq{" +
                "BISMT='" + BISMT + '\'' +
                ", IPRKZ='" + IPRKZ + '\'' +
                ", MAKTX='" + MAKTX + '\'' +
                ", MATKL='" + MATKL + '\'' +
                ", MATNR='" + MATNR + '\'' +
                ", MBRSH='" + MBRSH + '\'' +
                ", MEINS='" + MEINS + '\'' +
                ", MHDHB='" + MHDHB + '\'' +
                ", MHDRZ='" + MHDRZ + '\'' +
                ", MTART='" + MTART + '\'' +
                ", PRCTR='" + PRCTR + '\'' +
                ", WERKS='" + WERKS + '\'' +
                ", XCHPF='" + XCHPF + '\'' +
                ", ZMLEVEL='" + ZMLEVEL + '\'' +
                ", ZSORT='" + ZSORT + '\'' +
                ", ZSTATUS='" + ZSTATUS + '\'' +
                ", List21='" + List21 + '\'' +
                '}';
    }
}