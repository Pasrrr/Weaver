package LR.wanxiang.services;


import LR.wanxiang.Req.DeclassifyRequisitionReq;
import esteem.jun.wanxiang.action.bean.ResultMesage;
import esteem.jun.wanxiang.req.SupplierRequisitionReq;
import oracle.jdbc.proxy.annotation.Methods;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.UnsupportedEncodingException;

/**
 * @className: SendSRMMsgRemindService
 * @author: Cao
 * @date: 2023-10-18 19:00
 * @Depiction:
 **/

@WebService(targetNamespace = "http://common.wanxiang.com")
public interface WanxiangLRService {


    // 创建解密申请
    @WebMethod(operationName = "creatdeclassifyRequisitionWorkFlow", action = "urn:LR.wanxiang.services.creatdeclassifyRequisitionWorkFlow")
    public ResultMesage creatdeclassifyRequisitionWorkFlow(@WebParam(name="DeclassifyRequisitionReq") DeclassifyRequisitionReq declassifyRequisitionReq) throws UnsupportedEncodingException;

}
