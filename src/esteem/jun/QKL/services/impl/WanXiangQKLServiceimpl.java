package esteem.jun.QKL.services.impl;

import esteem.jun.QKL.req.InvoicemodeReq;
import esteem.jun.QKL.req.ResultMesage;
import esteem.jun.QKL.services.WanXiangQKLService;
import esteem.jun.common.util.ModeDataUtil;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static weaver.general.Util.null2String;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/24
 * @Description:
 */
public class WanXiangQKLServiceimpl implements WanXiangQKLService {
    private Log log = LogFactory.getLog(WanXiangQKLServiceimpl.class.getName());
    String sql = " select * from uf_Ivmode where BELNR=?";

    @Override
    public ResultMesage WriteinvoiceMode(InvoicemodeReq invoicemodeReq) throws UnsupportedEncodingException {
        log.info("------------------------发票状态----------------------------begin");
        log.info("参数信息：" + invoicemodeReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        Map<String, Object> Map = new HashMap<>();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(invoicemodeReq.getBELNR())) {
                resultMesage.setReturnCode("1001");
                resultMesage.setReturnDesc("错误信息:预制发票号不能为空");
                return resultMesage;
            }
            if (StringUtils.isEmpty(invoicemodeReq.getXBLNR())) {
                resultMesage.setReturnCode("1001");
                resultMesage.setReturnDesc("错误信息:发票代码不能为空");
                return resultMesage;
            }
            if (StringUtils.isEmpty(invoicemodeReq.getUsedAmount())) {
                resultMesage.setReturnCode("1001");
                resultMesage.setReturnDesc("错误信息:已用余额不能为空");
                return resultMesage;
            }
            if (StringUtils.isEmpty(invoicemodeReq.getAvailableAmount())) {
                resultMesage.setReturnCode("1001");
                resultMesage.setReturnDesc("错误信息:可用余额不能为空");
                return resultMesage;
            }
            String BELNR="";
            recordSet.executeQuery(sql, invoicemodeReq.getBELNR());
            if (recordSet.next()) {
                BELNR = null2String(recordSet.getString("id"));
            }
            if ("".equals(BELNR)) {
                resultMesage.setReturnCode("1002");
                resultMesage.setReturnDesc("错误信息:此发票未在系统录入");
                return resultMesage;
            }
            Map.put("id",BELNR);
            Map.put("BELNR",null2String(invoicemodeReq.getBELNR()));
            Map.put("XBLNR",null2String(invoicemodeReq.getXBLNR()));
            Map.put("BLART",null2String(invoicemodeReq.getBLART()));
            //Map.put("invoiceCode",null2String(invoicemodeReq.getInvoiceCode()));
            Map.put("BLDAT",null2String(invoicemodeReq.getBLDAT()));
            //Map.put("buyerName",null2String(invoicemodeReq.getBuyerName()));
            //Map.put("buyerCreditCode",null2String(invoicemodeReq.getBuyerCreditCode()));
            //Map.put("sellerName",null2String(invoicemodeReq.getSellerName()));
            //Map.put("sellerCreditCode",null2String(invoicemodeReq.getSellerCreditCode()));
            Map.put("beforeTaxAmount",null2String(invoicemodeReq.getBeforeTaxAmount()));
            Map.put("afterTaxAmount",null2String(invoicemodeReq.getAfterTaxAmount()));
            Map.put("usedAmount",null2String(invoicemodeReq.getUsedAmount()));
            Map.put("availableAmount",null2String(invoicemodeReq.getAvailableAmount()));
            //Map.put("checkCode",null2String(invoicemodeReq.getCheckCode()));
            ModeDataUtil.SaveModeDataInfo(Map, "241", "1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        resultMesage.setReturnCode("0");
        resultMesage.setReturnDesc("信息接收成功");
        log.info("------------------发票状态--------------------end");
        return resultMesage;
    }
}
