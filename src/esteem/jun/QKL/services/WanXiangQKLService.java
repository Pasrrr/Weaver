package esteem.jun.QKL.services;

import esteem.jun.QKL.req.InvoicemodeReq;
import esteem.jun.QKL.req.ResultMesage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/23
 * @Description:
 */

@WebService(targetNamespace = "http://common.wanxiang.com")
public interface WanXiangQKLService {

    // 供应链服务系统推送万向空间发票信息
    @WebMethod(operationName = "WriteinvoiceMode", action = "urn:esteem.jun.wanxiang.services.WriteinvoiceMode")
    public ResultMesage WriteinvoiceMode(@WebParam(name="InvoicemodeReq") InvoicemodeReq invoicemodeReq) throws UnsupportedEncodingException;

}
