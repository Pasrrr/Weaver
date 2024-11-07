package esteem.jun.wanxiang.services;

import esteem.jun.wanxiang.action.bean.ResultMesage;
import esteem.jun.wanxiang.req.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.UnsupportedEncodingException;

/**
 * @className: SendBomMsgRemindService
 * @author: jun
 * @date: 2021-12-13 19:00
 * @Depiction:
 **/
@WebService(targetNamespace = "http://common.wanxiang.com")
public interface WanXiangXgxxService {

    // 创建采购申请审批流程
    @WebMethod(operationName = "creatPurchaseRequisitionWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatCgsqspWorkFlow")
    public ResultMesage creatPurchaseRequisitionWorkFlow(@WebParam(name="PurchaseRequisitionReq") PurchaseRequisitionReq purchaseRequisitionReq) throws UnsupportedEncodingException;

    // 原材料不合格品流程处理 573
    @WebMethod(operationName = "creatRawMaterialUnqualifiedWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatRawMaterialUnqualifiedWorkFlow")
    public ResultMesage creatRawMaterialUnqualifiedWorkFlow(@WebParam(name="RawMaterialUnqualifiedReq") RawMaterialUnqualifiedReq rawMaterialUnqualifiedReq) throws UnsupportedEncodingException;

    // 原材料检验流程处理 571
    @WebMethod(operationName = "creatRawMaterialCheckWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatRawMaterialCheckWorkFlow")
    public ResultMesage creatRawMaterialCheckWorkFlow(@WebParam(name="RawMaterialCheckReq") RawMaterialCheckReq RawMaterialCheckReq) throws UnsupportedEncodingException;

    // 采购订单 578
    @WebMethod(operationName = "creatPurchaseOrderWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatPurchaseOrderWorkFlow")
    public ResultMesage creatPurchaseOrderWorkFlow(@WebParam(name="PurchaseOrderReq") PurchaseOrderReq purchaseOrderReq) throws UnsupportedEncodingException;

    // 合同评审577
    @WebMethod(operationName = "creatContractReviewWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatContractReviewWorkFlow")
    public ResultMesage creatContractReviewWorkFlow(@WebParam(name="ContractReviewReq") ContractReviewReq contractReviewReq) throws UnsupportedEncodingException;


    // 发票入账流程 576
    @WebMethod(operationName = "creatInvoiceEntryWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatInvoiceEntryWorkFlow")
    public ResultMesage creatInvoiceEntryWorkFlow(@WebParam(name="InvoiceEntryReq") InvoiceEntryReq invoiceEntryReq) throws UnsupportedEncodingException;

    //主数据创建流程
    @WebMethod(operationName = "creatAgileWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatAgileWorkFlow")
    public ResultMesage creatAgileWorkFlow(@WebParam(name="creatAgileWorkFlow") MasterDateReq masterDateReq) throws UnsupportedEncodingException;

    //项目类物料凭证
    @WebMethod(operationName = "creatProjectMaterialWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatProjectMaterialWorkFlow")
    public ResultMesage creatProjectMaterialWorkFlow(@WebParam(name="creatProjectMaterialWorkFlow") ProjectMaterialReq projectMaterialproofReq) throws UnsupportedEncodingException;

    //项目类预留单
    @WebMethod(operationName = "creatProjectMaterialReserveWorkFlow", action = "urn:esteem.jun.wanxiang.services.creatProjectMaterialReserveWorkFlow")
    public ResultMesage creatProjectMaterialReserveWorkFlow(@WebParam(name="creatProjectMaterialReserveWorkFlow") ProjectMaterialproofReq projectMaterialReq) throws UnsupportedEncodingException;

    //销售收货单
    @WebMethod(operationName = "creatSalesInvoiceMode", action = "urn:esteem.jun.wanxiang.services.creatSalesInvoiceMode")
    public ResultMesage creatSalesInvoiceMode(@WebParam(name="creatSalesInvoiceMode") SalesInvoiceReq salesInvoicereq) throws UnsupportedEncodingException;
}


