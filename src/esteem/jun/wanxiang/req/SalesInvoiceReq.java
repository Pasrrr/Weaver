package esteem.jun.wanxiang.req;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/15
 * @Description:
 */
public class SalesInvoiceReq {
    private List<SalesInvoiceDetailReq> items;

    public List<SalesInvoiceDetailReq> getItems() {
        return items;
    }

    public void setItems(List<SalesInvoiceDetailReq> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SalesInvoiceReq{" +
                "items=" + items +
                '}';
    }
}
