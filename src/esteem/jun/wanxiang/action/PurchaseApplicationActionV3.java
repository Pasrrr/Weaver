package esteem.jun.wanxiang.action;

import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @Author: Code of Passion
 * @Date: 2024-3-25
 * @Description: 采购申购流程 -->SAP 创建采购申购
 *
 */
public class PurchaseApplicationActionV3 extends BaseBean implements Action {
    private Log log = LogFactory.getLog(PurchaseApplicationActionV3.class.getName());
    private String logstatus;

    public String execute(RequestInfo requestInfo) {
        long startTime = System.currentTimeMillis();
        String sapurl = Prop.getPropValue("wanxiang", "sap.cgsq.url");
        String urn = Prop.getPropValue("wanxiang", "sap.cgsq.urn");
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String formId = baseActionInfo.getFormId();
        String tableName = requestInfo.getRequestManager().getBillTableName();

        RecordSet recordSet = new RecordSet();
        try {
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            StringBuffer contentXml = new StringBuffer();
            /**采购申请类型*/
            contentXml.append("<EQ_BSART>" + baseActionInfo.getMainTableValue("BSART1") + "</EQ_BSART>");
            /**历史单据号*/
            contentXml.append("<EQ_BANFN>" + baseActionInfo.getMainTableValue("BANFN") + "</EQ_BANFN>");
            contentXml.append("<LT_ITEM>");
            List<Map<String, String>> detailDatas = baseActionInfo.getDetailTableData(1);
            int count=0;
            for (Map<String, String> rowData : detailDatas) {
                count=count+10;
                contentXml.append("<item>");
                /**采购申请行项目号*/
                contentXml.append("<BNFPO>" + count + "</BNFPO>");
                /**科目分配类别*/
                contentXml.append("<KNTTP>" + baseActionInfo.getDetailTableValue(rowData, "KNTTP") + "</KNTTP>");
                /**项目类别*/
                contentXml.append("<PSTYP>" + baseActionInfo.getDetailTableValue(rowData, "PSTYP") + "</PSTYP>");
                /**物料编码*/
                contentXml.append("<MATNR>" + baseActionInfo.getDetailTableValue(rowData, "MATNR") + "</MATNR>");
                /**物料描述*/
                contentXml.append("<TXZ01>" + "</TXZ01>");
                /**申请数量*/
                contentXml.append("<MENGE>" + baseActionInfo.getDetailTableValue(rowData, "sl") + "</MENGE>");
                /**单位*/
                contentXml.append("<MEINS>" + baseActionInfo.getDetailTableValue(rowData, "dw") + "</MEINS>");
                /**交货日期*/
                contentXml.append("<LFDAT>" + baseActionInfo.getDetailTableValue(rowData, "xqrq") + "</LFDAT>");
                /**物料组*/
                contentXml.append("<MATKL>" + baseActionInfo.getDetailTableValue(rowData, "MATKL") + "</MATKL>");
                /**工厂*/
                contentXml.append("<WERKS>" + baseActionInfo.getDetailTableValue(rowData, "WERKS") + "</WERKS>");
                /**采购组*/
                contentXml.append("<EKGRP>" + baseActionInfo.getDetailTableValue(rowData, "EKGRP") + "</EKGRP>");
                /**申请人*/
                contentXml.append("<AFNAM>" + baseActionInfo.getDetailTableValue(rowData, "xqr1") + "</AFNAM>");
                /**采购申请中的价格*/
                contentXml.append("<PREIS>" + baseActionInfo.getDetailTableValue(rowData, "yjdj") + "</PREIS>");
                /**价格单位*/
                contentXml.append("<PEINH>" + baseActionInfo.getDetailTableValue(rowData, "jgdw") + "</PEINH>");
                /**删除标识*/
                contentXml.append("<LOEKZ>" + baseActionInfo.getDetailTableValue(rowData, "LOEKZ") + "</LOEKZ>");
                /**批准标识*/
                contentXml.append("<FRGKZ>" + baseActionInfo.getDetailTableValue(rowData, "FRGKZ") + "</FRGKZ>");
                /**最低订单单价*/
                contentXml.append("<ZLOWESTPR>" + baseActionInfo.getDetailTableValue(rowData, "ZLOWESTPR") + "</ZLOWESTPR>");
                /**最低订单单价日期*/
                contentXml.append("<ZLOWESTDT>" + baseActionInfo.getDetailTableValue(rowData, "ZLOWESTDT") + "</ZLOWESTDT>");
                /**最低订单单价*/
                contentXml.append("<ZLASTPR>" + baseActionInfo.getDetailTableValue(rowData, "ZLASTPR") + "</ZLASTPR>");
                /**最低订单单价日期*/
                contentXml.append("<ZLASTDT>" + baseActionInfo.getDetailTableValue(rowData, "ZLASTDT") + "</ZLASTDT>");
                /**总账科目*/
                contentXml.append("<SAKTO>" + baseActionInfo.getDetailTableValue(rowData, "SAKTO") + "</SAKTO>");
                /**成本中心*/
                contentXml.append("<KOSTL>" + baseActionInfo.getDetailTableValue(rowData, "KOSTL") + "</KOSTL>");
                /**成本控制范围*/
                //contentXml.append("<KOKRS>" + baseActionInfo.getDetailTableValue(rowData, "KOKRS") + "</KOKRS>");
                /**职能范围*/
                //contentXml.append("<SKBER>" + baseActionInfo.getDetailTableValue(rowData, "SKBER") + "</SKBER>");
                /**订单*/
                contentXml.append("<AUFNR>" + baseActionInfo.getDetailTableValue(rowData, "AUFNR") + "</AUFNR>");
                /**创建日期*/
                contentXml.append("<BADAT>" + baseActionInfo.getMainTableValue("sqrq") + "</BADAT>");
                /**批准日期*/
                contentXml.append("<FRGDT>" + Util.null2String(df.format(now)) + "</FRGDT>");
                /**品牌*/
                contentXml.append("<ZBRAND>" + baseActionInfo.getDetailTableValue(rowData, "pp") + "</ZBRAND>");
                /**所属预算*/
                contentXml.append("<ZBUDGET>" + baseActionInfo.getDetailTableValue(rowData, "szys1") + "</ZBUDGET>");
                /**是否已签合同*/
                contentXml.append("<ZCONTRACT>" + baseActionInfo.getDetailTableValue(rowData, "sfykjht") + "</ZCONTRACT>");
                /**申请理由*/
                contentXml.append("<ZREASON>" + baseActionInfo.getDetailTableValue(rowData, "ZREASON") + "</ZREASON>");
                /**使用产线*/
                contentXml.append("<ZSHYCHX>" + baseActionInfo.getDetailTableValue(rowData, "ZSHYCHX") + "</ZSHYCHX>");
                /**具体使用设备*/
                contentXml.append("<ZSHYSHB>" + baseActionInfo.getDetailTableValue(rowData, "ZSHYSHB") + "</ZSHYSHB>");

                contentXml.append("</item>");
            }
            contentXml.append("</LT_ITEM>");
            String soapXml = WebServiceUtil.getSoapXmlTitle(contentXml.toString(), urn);
            log.info("soapXml-->" + soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl, soapXml);
            log.info("resultXml-->" + resultXml);
            Result result = new Result();
            SOAPMessage soapMessage = SoapUtil.formatSoapString(resultXml);
            SOAPBody body = soapMessage.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            parsingXmlResult(iterator, null, result);
            log.info("result-->"+result.toString());
            if ("1".equals(logstatus)) {
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("采购申购流程流程-SAP", "7", soapXml, resultXml, result.toString(), "", requestId, "OA", "-1", result.getCode(), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
            /**更新接口状态*/
            String sql="update "+tableName+" set sapfhbs='"+result.getCode()+"',sapfhjg='"+result.getMsg()+"',BANFN='"+result.getBak1()+"' where requestid ='"+requestId+"'";
            log.info("sql--->"+sql);
            recordSet.execute(sql);
            if (!result.getCode().equals("S")) {
                requestInfo.getRequestManager().setMessage("111100");
                requestInfo.getRequestManager().setMessagecontent("SAP服务方返回错误信息:" + result.getMsg() + "!");
                log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
                return Action.FAILURE_AND_CONTINUE;
            }
        } catch (Exception e) {
            requestInfo.getRequestManager().setMessage("111100");
            requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
            return Action.FAILURE_AND_CONTINUE;
        }
        return Action.SUCCESS;
    }

    public static void parsingXmlResult(Iterator<SOAPElement> iterator, String side, Result result) throws ParseException, SOAPException {
        while (iterator.hasNext()) {
            SOAPElement element = (SOAPElement) iterator.next();
            if("ES_RET".equals(element.getNodeName())){
                Iterator<SOAPElement> it = element.getChildElements();
                while (it.hasNext()) {
                    SOAPElement el = it.next();
                    System.out.println(el.getNodeName());
                    if (el.getNodeName().equals("CODE")) {
                        result.setCode(el.getValue());
                    }
                    if (el.getNodeName().equals("MSG")) {
                        result.setMsg(el.getValue());
                    }
                }
            }
            if("ES_BANFN".equals(element.getNodeName())){
                result.setBak1(element.getValue());
            }
            if (null == element.getValue() && element.getChildElements().hasNext()) {
                parsingXmlResult(element.getChildElements(), side + "-----",result);
            }
        }
    }
    public String getLogstatus() {
        return logstatus;
    }

    public void setLogstatus(String logstatus) {
        this.logstatus = logstatus;
    }
}