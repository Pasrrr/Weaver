package esteem.jun.wanxiang.req;

public class SupplierRequisitionReq {
    /**SRM流程ID*/
    private String TaskID;
    /**供应商名称*/
    private String NAME1;
    /**供应商分组*/
    private String KTOKK;
    /**纳税人登记号*/
    private String STCD5;
    /**企业性质*/
    private String ANRED;
    /**国家*/
    private String LAND1;
    /**地区*/
    private String ORT02;
    /**城市*/
    private String ORT01;
    /**邮政编码*/
    private String PSTLZ;
    /**供应商联系人*/
    private String TELF1;
    /**供应商电话*/
    private String TELF2;
    /**供应商邮箱*/
    private String TELBX;
    /**是否为客户*/
    private String KUNNR;
    /**银行名称*/
    private String BANKA;
    /**银行账号*/
    private String BANKN;
    /**银行代码*/
    private String BANKL;
    /**订单货币*/
    private String WAERS;
    /**付款条件*/
    private String ZTERM;
    /**付款条件文本*/
    private String VTEXT;

    /**创建人字段*/
    private String Account;

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1;
    }

    public String getKTOKK() {
        return KTOKK;
    }

    public void setKTOKK(String KTOKK) {
        this.KTOKK = KTOKK;
    }

    public String getSTCD5() {
        return STCD5;
    }

    public void setSTCD5(String STCD5) {
        this.STCD5 = STCD5;
    }

    public String getANRED() {
        return ANRED;
    }

    public void setANRED(String ANRED) {
        this.ANRED = ANRED;
    }

    public String getLAND1() {
        return LAND1;
    }

    public void setLAND1(String LAND1) {
        this.LAND1 = LAND1;
    }

    public String getORT02() {
        return ORT02;
    }

    public void setORT02(String ORT02) {
        this.ORT02 = ORT02;
    }

    public String getORT01() {
        return ORT01;
    }

    public void setORT01(String ORT01) {
        this.ORT01 = ORT01;
    }

    public String getPSTLZ() {
        return PSTLZ;
    }

    public void setPSTLZ(String PSTLZ) {
        this.PSTLZ = PSTLZ;
    }

    public String getTELF1() {
        return TELF1;
    }

    public void setTELF1(String TELF1) {
        this.TELF1 = TELF1;
    }

    public String getTELF2() {
        return TELF2;
    }

    public void setTELF2(String TELF2) {
        this.TELF2 = TELF2;
    }

    public String getTELBX() {
        return TELBX;
    }

    public void setTELBX(String TELBX) {
        this.TELBX = TELBX;
    }

    public String getKUNNR() {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR) {
        this.KUNNR = KUNNR;
    }

    public String getBANKA() {
        return BANKA;
    }

    public void setBANKA(String BANKA) {
        this.BANKA = BANKA;
    }

    public String getBANKN() {
        return BANKN;
    }

    public void setBANKN(String BANKN) {
        this.BANKN = BANKN;
    }

    public String getBANKL() {
        return BANKL;
    }

    public void setBANKL(String BANKL) {
        this.BANKL = BANKL;
    }

    public String getWAERS() {
        return WAERS;
    }

    public void setWAERS(String WAERS) {
        this.WAERS = WAERS;
    }

    public String getZTERM() {
        return ZTERM;
    }

    public void setZTERM(String ZTERM) {
        this.ZTERM = ZTERM;
    }

    public String getVTEXT() {
        return VTEXT;
    }

    public void setVTEXT(String VTEXT) {
        this.VTEXT = VTEXT;
    }

    @Override
    public String toString() {
        return "SupplierRequisitionReq{" +
                "TaskID='" + TaskID + '\'' +
                ", NAME1='" + NAME1 + '\'' +
                ", KTOKK='" + KTOKK + '\'' +
                ", STCD5='" + STCD5 + '\'' +
                ", ANRED='" + ANRED + '\'' +
                ", LAND1='" + LAND1 + '\'' +
                ", ORT02='" + ORT02 + '\'' +
                ", ORT01='" + ORT01 + '\'' +
                ", PSTLZ='" + PSTLZ + '\'' +
                ", TELF1='" + TELF1 + '\'' +
                ", TELF2='" + TELF2 + '\'' +
                ", TELBX='" + TELBX + '\'' +
                ", KUNNR='" + KUNNR + '\'' +
                ", BANKA='" + BANKA + '\'' +
                ", BANKN='" + BANKN + '\'' +
                ", BANKL='" + BANKL + '\'' +
                ", WAERS='" + WAERS + '\'' +
                ", ZTERM='" + ZTERM + '\'' +
                ", VTEXT='" + VTEXT + '\'' +
                ", Account='" + Account + '\'' +
                '}';
    }
}
