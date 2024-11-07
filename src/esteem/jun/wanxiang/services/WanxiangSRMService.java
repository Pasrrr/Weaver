package esteem.jun.wanxiang.services;


import esteem.jun.wanxiang.action.bean.ResultMesage;
import esteem.jun.wanxiang.req.*;

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
public interface WanxiangSRMService {

    // 创建供应商新增申请
    @WebMethod(operationName = "creatsupplierRequisitionWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatsupplierRequisitionWorkFlow")
    public ResultMesage creatsupplierRequisitionWorkFlow(@WebParam(name="SupplierRequisitionReq") SupplierRequisitionReq supplierRequisitionReq) throws UnsupportedEncodingException;
    // 索赔单
    @WebMethod(operationName = "creatPunishmentWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatPunishmentWorkFlow")
    public ResultMesage creatPunishmentWorkFlow(@WebParam(name="PunishmentReq") PunishmentReq punishmentReq) throws UnsupportedEncodingException;

    @WebMethod(operationName = "creatPunishmentChangeWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatPunishmentChangeWorkFlow")
    public ResultMesage creatPunishmentChangeWorkFlow(@WebParam(name="PunishmentChangeWorkFlow") PunishmentChangeReq punishmentChangeReq) throws UnsupportedEncodingException;
}
