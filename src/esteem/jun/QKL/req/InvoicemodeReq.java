package esteem.jun.QKL.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/24
 * @Description:
 */
public class InvoicemodeReq {
    /**会计凭证的凭证编号(预制发票号)*/
    private String BELNR;
    /**参考凭证编号(发票号码)*/
    private String XBLNR;
    /**	凭证类型(发票类型)*/
    private String BLART;
    /**	发票代码*/
    private String invoiceCode;
    /**凭证中的凭证日期/开票日期*/
    private String BLDAT;
    /**买方名称*/
    private String 	buyerName;
    /**买方税号*/
    private String buyerCreditCode;
    /**卖方名称*/
    private String 	sellerName;
    /**卖方税号*/
    private String 	sellerCreditCode;
    /**含税金额*/
    private String beforeTaxAmount;
    /**不含税金额*/
    private String afterTaxAmount;
    /**已使用金额*/
    private String 	usedAmount;
    /**可用金额*/
    private String availableAmount;
    /**校验码后6位*/
    private String checkCode;

    public String getBELNR() {
        return BELNR;
    }

    public void setBELNR(String BELNR) {
        this.BELNR = BELNR;
    }

    public String getXBLNR() {
        return XBLNR;
    }

    public void setXBLNR(String XBLNR) {
        this.XBLNR = XBLNR;
    }

    public String getBLART() {
        return BLART;
    }

    public void setBLART(String BLART) {
        this.BLART = BLART;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getBLDAT() {
        return BLDAT;
    }

    public void setBLDAT(String BLDAT) {
        this.BLDAT = BLDAT;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerCreditCode() {
        return buyerCreditCode;
    }

    public void setBuyerCreditCode(String buyerCreditCode) {
        this.buyerCreditCode = buyerCreditCode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerCreditCode() {
        return sellerCreditCode;
    }

    public void setSellerCreditCode(String sellerCreditCode) {
        this.sellerCreditCode = sellerCreditCode;
    }

    public String getBeforeTaxAmount() {
        return beforeTaxAmount;
    }

    public void setBeforeTaxAmount(String beforeTaxAmount) {
        this.beforeTaxAmount = beforeTaxAmount;
    }

    public String getAfterTaxAmount() {
        return afterTaxAmount;
    }

    public void setAfterTaxAmount(String afterTaxAmount) {
        this.afterTaxAmount = afterTaxAmount;
    }

    public String getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(String usedAmount) {
        this.usedAmount = usedAmount;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "InvoicemodeReq{" +
                "BELNR='" + BELNR + '\'' +
                ", XBLNR='" + XBLNR + '\'' +
                ", BLART='" + BLART + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", BLDAT='" + BLDAT + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerCreditCode='" + buyerCreditCode + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", sellerCreditCode='" + sellerCreditCode + '\'' +
                ", beforeTaxAmount='" + beforeTaxAmount + '\'' +
                ", afterTaxAmount='" + afterTaxAmount + '\'' +
                ", usedAmount='" + usedAmount + '\'' +
                ", availableAmount='" + availableAmount + '\'' +
                ", checkCode='" + checkCode + '\'' +
                '}';
    }
}
