package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description: 物料凭证
 */
public class ProjectMaterialReq {
    /**SAP物料凭证号*/
    private String  MBLNR;
    /**过账日期*/
    private String BUDAT;
    /**创建人*/
    private String  USNAM;
    /**创建日期*/
    private String  CPUDT;
    /**移动类型*/
    private String  BWART;
    private List<ProjectMaterialDetailReq> items;

    @Override
    public String toString() {
        return "ProjectMaterialproofReq{" +
                "MBLNR='" + MBLNR + '\'' +
                ", BUDAT='" + BUDAT + '\'' +
                ", USNAM='" + USNAM + '\'' +
                ", CPUDT='" + CPUDT + '\'' +
                ", BWART='" + BWART + '\'' +
                ", items=" + items +
                '}';
    }

    public String getMBLNR() {
        return MBLNR;
    }

    public void setMBLNR(String MBLNR) {
        this.MBLNR = MBLNR;
    }

    public String getBUDAT() {
        return BUDAT;
    }

    public void setBUDAT(String BUDAT) {
        this.BUDAT = BUDAT;
    }

    public String getUSNAM() {
        return USNAM;
    }

    public void setUSNAM(String USNAM) {
        this.USNAM = USNAM;
    }

    public String getCPUDT() {
        return CPUDT;
    }

    public void setCPUDT(String CPUDT) {
        this.CPUDT = CPUDT;
    }

    public String getBWART() {
        return BWART;
    }

    public void setBWART(String BWART) {
        this.BWART = BWART;
    }

    public List<ProjectMaterialDetailReq> getItems() {
        return items;
    }

    public void setItems(List<ProjectMaterialDetailReq> items) {
        this.items = items;
    }
}
