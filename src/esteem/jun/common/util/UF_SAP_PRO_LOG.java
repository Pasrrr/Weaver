package esteem.jun.common.util;

/**
 * @className: uf_sap_pro_log
 * @author: jun
 * @date: 2021-12-31 10:46
 * @Depiction:
 **/
public class UF_SAP_PRO_LOG {

    /**日志标题*/
    private String rzbt;
    /**日志类型*/
    private String rzlx;
    /**发送数据*/
    private String fssj;
    /**接收数据*/
    private String jssj;
    /**解析后结果*/
    private String jxhjg;
    /**其他信息*/
    private String qxx;
    /**相关ID*/
    private String xgid;
    /**发送方*/
    private String fsf;
    /**相关流程*/
    private String xglc;
    /**执行状态*/
    private String zhzt;
    /**耗时*/
    private String hs;
    /**请求地址*/
    private String qqdz;
    /**接口地址*/
    private String jkdz;

    public UF_SAP_PRO_LOG(String rzbt, String rzlx, String jxhjg, String qxx, String xgid, String fsf) {
        this.rzbt = rzbt;
        this.rzlx = rzlx;
        this.jxhjg = jxhjg;
        this.qxx = qxx;
        this.xgid = xgid;
        this.fsf = fsf;
    }

    public UF_SAP_PRO_LOG(String rzbt, String rzlx, String fssj, String jssj, String jxhjg, String qxx, String xgid, String fsf, String xglc, String zhzt, String hs, String qqdz, String jkdz) {
        this.rzbt = rzbt;
        this.rzlx = rzlx;
        this.fssj = fssj;
        this.jssj = jssj;
        this.jxhjg = jxhjg;
        this.qxx = qxx;
        this.xgid = xgid;
        this.fsf = fsf;
        this.xglc = xglc;
        this.zhzt = zhzt;
        this.hs = hs;
        this.qqdz = qqdz;
        this.jkdz = jkdz;
    }

    public String getRzbt() {
        return rzbt;
    }

    public void setRzbt(String rzbt) {
        this.rzbt = rzbt;
    }

    public String getRzlx() {
        return rzlx;
    }

    public void setRzlx(String rzlx) {
        this.rzlx = rzlx;
    }


    public String getJxhjg() {
        return jxhjg;
    }

    public void setJxhjg(String jxhjg) {
        this.jxhjg = jxhjg;
    }

    public String getQxx() {
        return qxx;
    }

    public void setQxx(String qxx) {
        this.qxx = qxx;
    }

    public String getXgid() {
        return xgid;
    }

    public void setXgid(String xgid) {
        this.xgid = xgid;
    }

    public String getFsf() {
        return fsf;
    }

    public void setFsf(String fsf) {
        this.fsf = fsf;
    }

    public String getFssj() {
        return fssj;
    }

    public void setFssj(String fssj) {
        this.fssj = fssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getXglc() {
        return xglc;
    }

    public void setXglc(String xglc) {
        this.xglc = xglc;
    }

    public String getZhzt() {
        return zhzt;
    }

    public void setZhzt(String zhzt) {
        this.zhzt = zhzt;
    }

    public String getHs() {
        return hs;
    }

    public void setHs(String hs) {
        this.hs = hs;
    }

    public String getQqdz() {
        return qqdz;
    }

    public void setQqdz(String qqdz) {
        this.qqdz = qqdz;
    }

    public String getJkdz() {
        return jkdz;
    }

    public void setJkdz(String jkdz) {
        this.jkdz = jkdz;
    }

}
