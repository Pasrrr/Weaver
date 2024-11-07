package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Cao
 * @Date: 2024-1-15
 * @Description:索赔单
 */
public class PunishmentReq {
    /**SRM流程ID*/
    private String TaskID;
    /**创建人字段*/
    private String Account;
    /**供应商名称*/
    private String SupName;
    /**供应商编码*/
    private String SupCode;
    /**索赔通知创建日期*/
    private String NoticeBeginDate;
    /**供应商工厂代码*/
    private String SupFactoryCode;
    /**处罚单类型*/
    private String ExtendField7;
    /**索赔开始日期*/
    private String ClaimantBeginDate;
    /**索赔结束日期*/
    private String ClaimantEndDate;
    /**QCR编号*/
    private String QCRCode;
    /**问题零件号*/
    private String ProblemPartNumber;
    /**问题零件名称*/
    private String ProblemPartName;
    /**问题描述*/
    private String ProblemDescribe;
    /**费用合计*/
    private String PriceSum;
    /**合计*/
    private String Sum;
    /**变更理由*/
    private String Reason;

    private List<PunishmentDetailReq> item1;

    private List<PunishmentDetail2Req> item2;

    @Override
    public String toString() {
        return "PunishmentReq{" +
                "TaskID='" + TaskID + '\'' +
                ", Account='" + Account + '\'' +
                ", SupName='" + SupName + '\'' +
                ", SupCode='" + SupCode + '\'' +
                ", NoticeBeginDate='" + NoticeBeginDate + '\'' +
                ", SupFactoryCode='" + SupFactoryCode + '\'' +
                ", ExtendField7='" + ExtendField7 + '\'' +
                ", ClaimantBeginDate='" + ClaimantBeginDate + '\'' +
                ", ClaimantEndDate='" + ClaimantEndDate + '\'' +
                ", QCRCode='" + QCRCode + '\'' +
                ", ProblemPartNumber='" + ProblemPartNumber + '\'' +
                ", ProblemPartName='" + ProblemPartName + '\'' +
                ", ProblemDescribe='" + ProblemDescribe + '\'' +
                ", PriceSum='" + PriceSum + '\'' +
                ", Sum='" + Sum + '\'' +
                ", Reason='" + Reason + '\'' +
                ", item1=" + item1 +
                ", item2=" + item2 +
                '}';
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getPriceSum() {
        return PriceSum;
    }

    public void setPriceSum(String priceSum) {
        PriceSum = priceSum;
    }

    public String getSum() {
        return Sum;
    }

    public void setSum(String sum) {
        Sum = sum;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }

    public String getSupCode() {
        return SupCode;
    }

    public void setSupCode(String supCode) {
        SupCode = supCode;
    }

    public String getNoticeBeginDate() {
        return NoticeBeginDate;
    }

    public void setNoticeBeginDate(String noticeBeginDate) {
        NoticeBeginDate = noticeBeginDate;
    }

    public String getSupFactoryCode() {
        return SupFactoryCode;
    }

    public void setSupFactoryCode(String supFactoryCode) {
        SupFactoryCode = supFactoryCode;
    }

    public String getExtendField7() {
        return ExtendField7;
    }

    public void setExtendField7(String extendField7) {
        ExtendField7 = extendField7;
    }

    public String getClaimantBeginDate() {
        return ClaimantBeginDate;
    }

    public void setClaimantBeginDate(String claimantBeginDate) {
        ClaimantBeginDate = claimantBeginDate;
    }

    public String getClaimantEndDate() {
        return ClaimantEndDate;
    }

    public void setClaimantEndDate(String claimantEndDate) {
        ClaimantEndDate = claimantEndDate;
    }

    public String getQCRCode() {
        return QCRCode;
    }

    public void setQCRCode(String QCRCode) {
        this.QCRCode = QCRCode;
    }

    public String getProblemPartNumber() {
        return ProblemPartNumber;
    }

    public void setProblemPartNumber(String problemPartNumber) {
        ProblemPartNumber = problemPartNumber;
    }

    public String getProblemPartName() {
        return ProblemPartName;
    }

    public void setProblemPartName(String problemPartName) {
        ProblemPartName = problemPartName;
    }

    public String getProblemDescribe() {
        return ProblemDescribe;
    }

    public void setProblemDescribe(String problemDescribe) {
        ProblemDescribe = problemDescribe;
    }

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

    public List<PunishmentDetailReq> getItem1() {
        return item1;
    }

    public void setItem1(List<PunishmentDetailReq> item1) {
        this.item1 = item1;
    }

    public List<PunishmentDetail2Req> getItem2() {
        return item2;
    }

    public void setItem2(List<PunishmentDetail2Req> item2) {
        this.item2 = item2;
    }
}
