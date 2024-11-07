package esteem.jun.wanxiang.req;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-30 10:31
 * @Description:采购申请流程
 */

public class PurchaseRequisitionReq {
    /**采购申请编号*/
    private String BANFN;
    /**申请人*/
    private String BNAME;
    /**	采购由请类型 	*/
    private String 	BSART;
    /**	采购申请类型描述	*/
    private String 	BATXT;

    public String getBSART() {
        return BSART;
    }

    public void setBSART(String BSART) {
        this.BSART = BSART;
    }

    public String getBATXT() {
        return BATXT;
    }

    public void setBATXT(String BATXT) {
        this.BATXT = BATXT;
    }

    private List<PurchaseRequisitionDetailReq> item;

    public String getBANFN() {
        return BANFN;
    }

    public void setBANFN(String BANFN) {
        this.BANFN = BANFN;
    }

    public List<PurchaseRequisitionDetailReq> getItem() {
        return item;
    }

    public void setItem(List<PurchaseRequisitionDetailReq> item) {
        this.item = item;
    }

    public String getBNAME() {
        return BNAME;
    }

    public void setBNAME(String BNAME) {
        this.BNAME = BNAME;
    }

    @Override
    public String toString() {
        return "PurchaseRequisitionReq{" +
                "BANFN='" + BANFN + '\'' +
                ", BNAME='" + BNAME + '\'' +
                ", BSART='" + BSART + '\'' +
                ", BATXT='" + BATXT + '\'' +
                ", item=" + item +
                '}';
    }
}