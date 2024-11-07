package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description: 物料预留单
 */
public class ProjectMaterialproofReq {
    /**预留单号*/
    private String  RSNUM;
    /**预留单创建日期*/
    private String RSDAT;
    /**创建人*/
    private String  USNAM;
    /**移动类型*/
    private String  BWART;
    /**项目号*/
    private String  AUFNR;
    /**项目名称*/
    private String  KTEXT;
    /**成本中心*/
    private String  KOSTL;
    /**成本中心名称*/
    private String  KTEXT2;
    /**专项项目*/
    private String ZSPECPRO;
    /**明细字段*/
    private List<ProjectMaterialproofDetailReq> items;

    @Override
    public String toString() {
        return "ProjectMaterialproofReq{" +
                "RSNUM='" + RSNUM + '\'' +
                ", RSDAT='" + RSDAT + '\'' +
                ", USNAM='" + USNAM + '\'' +
                ", BWART='" + BWART + '\'' +
                ", AUFNR='" + AUFNR + '\'' +
                ", KTEXT='" + KTEXT + '\'' +
                ", KOSTL='" + KOSTL + '\'' +
                ", KTEXT2='" + KTEXT2 + '\'' +
                ", ZSPECPRO='" + ZSPECPRO + '\'' +
                ", items=" + items +
                '}';
    }

    public String getZSPECPRO() {
        return ZSPECPRO;
    }

    public void setZSPECPRO(String ZSPECPRO) {
        this.ZSPECPRO = ZSPECPRO;
    }

    public String getRSNUM() {
        return RSNUM;
    }

    public void setRSNUM(String RSNUM) {
        this.RSNUM = RSNUM;
    }

    public String getRSDAT() {
        return RSDAT;
    }

    public void setRSDAT(String RSDAT) {
        this.RSDAT = RSDAT;
    }

    public String getUSNAM() {
        return USNAM;
    }

    public void setUSNAM(String USNAM) {
        this.USNAM = USNAM;
    }

    public String getBWART() {
        return BWART;
    }

    public void setBWART(String BWART) {
        this.BWART = BWART;
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

    public String getKOSTL() {
        return KOSTL;
    }

    public void setKOSTL(String KOSTL) {
        this.KOSTL = KOSTL;
    }

    public String getKTEXT2() {
        return KTEXT2;
    }

    public void setKTEXT2(String KTEXT2) {
        this.KTEXT2 = KTEXT2;
    }

    public List<ProjectMaterialproofDetailReq> getItems() {
        return items;
    }

    public void setItems(List<ProjectMaterialproofDetailReq> items) {
        this.items = items;
    }
}
