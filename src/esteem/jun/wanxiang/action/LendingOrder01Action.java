package esteem.jun.wanxiang.action;

import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-02 13:36
 * @Description:借贷订单流程 抛转SAP信息
 */
public class LendingOrder01Action  extends BaseBean implements Action {
    private Log log = LogFactory.getLog(LendingOrder01Action.class.getName());

    /**日志开启状态 1开启 0关闭*/
    private String logstatus;

    public String execute(RequestInfo requestInfo) {
        long startTime = System.currentTimeMillis();
        String sapurl= Prop.getPropValue("wanxiang","sap.jdxdd.url");
        String urn= Prop.getPropValue("wanxiang","sap.jdxdd.urn");
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String formId = baseActionInfo.getFormId();
        String tableName = requestInfo.getRequestManager().getBillTableName();
        RecordSet recordSet = new RecordSet();
        try{
            List<Map<String, String>> detailDatas = baseActionInfo.getDetailTableData(1);
            StringBuffer contentXml=new StringBuffer();
            contentXml.append("<IS_HEAD>");
            /**销售组织*/
            contentXml.append("<VKORG>"+baseActionInfo.getMainTableValue("VKORG")+"</VKORG>");
            /**分销渠道*/
            contentXml.append("<VTWEG>"+baseActionInfo.getMainTableValue("VTWEG")+"</VTWEG>");
            /**产品组*/
            contentXml.append("<SPART>"+baseActionInfo.getMainTableValue("SPART")+"</SPART>");
            /**客户编码*/
            contentXml.append("<KUNNR>"+baseActionInfo.getMainTableValue("KUNNR")+"</KUNNR>");
            /**客户编码*/
            contentXml.append("<BSTNK>"+baseActionInfo.getMainTableValue("KUNNR")+"</BSTNK>");
            /**销售订单类型*/
            contentXml.append("<AUART>"+baseActionInfo.getMainTableValue("AUART")+"</AUART>");
            /**销售订单原因*/
            contentXml.append("<AUGRU>"+baseActionInfo.getMainTableValue("AUGRU")+"</AUGRU>");
            /**送达方*/
            contentXml.append("<ZSDF>"+baseActionInfo.getMainTableValue("SDF")+"</ZSDF>");
            contentXml.append("</IS_HEAD>");
            contentXml.append("<GT_ITEM>");
            for(Map<String, String> rowData: detailDatas){
                contentXml.append("<item>");
                /**物料编码*/
                contentXml.append("<MATNR>"+baseActionInfo.getDetailTableValue(rowData, "MATNR")+"</MATNR>");
                /**物料名称*/
                contentXml.append("<ARKTX>"+baseActionInfo.getDetailTableValue(rowData, "ARKTX")+"</ARKTX>");
                /**数量*/
                contentXml.append("<ZMENG>"+baseActionInfo.getDetailTableValue(rowData, "ZMENG")+"</ZMENG>");
                /**工厂*/
                contentXml.append("<WERKS>"+baseActionInfo.getDetailTableValue(rowData, "WERKS")+"</WERKS>");
                /**凭证货币*/
                contentXml.append("<WAERK>"+baseActionInfo.getDetailTableValue(rowData, "WAERK")+"</WAERK>");
                /**销售凭证项目类别*/
                contentXml.append("<PSTYV>"+baseActionInfo.getDetailTableValue(rowData, "PSTYV")+"</PSTYV>");
                /**单位*/
                contentXml.append("<MEINS>"+baseActionInfo.getDetailTableValue(rowData, "MEINS")+"</MEINS>");
                /**税率*/
                contentXml.append("<MWSBP>"+baseActionInfo.getDetailTableValue(rowData, "MWSBP")+"</MWSBP>");
                /**金额*/
                contentXml.append("<NETWR>"+baseActionInfo.getDetailTableValue(rowData, "NETWR")+"</NETWR>");
                /**价格*/
                contentXml.append("<NETPR>"+baseActionInfo.getDetailTableValue(rowData, "NETPR")+"</NETPR>");
                /**付款条件*/
                contentXml.append("<ZTERM>"+baseActionInfo.getDetailTableValue(rowData, "ZTERM")+"</ZTERM>");
                contentXml.append("</item>");
            }
            contentXml.append("</GT_ITEM>");
            String soapXml = WebServiceUtil.getSoapXmlTitle(contentXml.toString(),urn);
            log.info("soapXml-->"+soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl,soapXml);
            log.info("resultXml-->"+resultXml);
            Result result=new Result();
            SOAPMessage soapMessage = SoapUtil.formatSoapString(resultXml);
            SOAPBody body = soapMessage.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            parsingXmlResult(iterator, null,result);
            if("1".equals(logstatus)){
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("借贷订单-SAP借货项订单","3", soapXml,resultXml,result.toString(),"",requestId,"OA","-1",result.getCode(),(System.currentTimeMillis() - startTime)+"ms", InetAddress.getLocalHost().getHostAddress(),sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
            String sql="update "+tableName+" set sapfhbs='"+result.getCode()+"',sapfhjg='"+result.getMsg()+"' where requestid ='"+requestId+"'";
            log.info("sql--->"+sql);
            recordSet.execute(sql);
            if(!result.getCode().equals("S")) {
                requestInfo.getRequestManager().setMessage("111100");
                requestInfo.getRequestManager().setMessagecontent("SAP服务方返回错误信息:"+result.getMsg()+"!");
                log.info("触发流程:"+requestName+";执行接口类名:"+this.getClass().getName()+";发起请求RequestId:" + requestId + ";程序异常!");
                return Action.FAILURE_AND_CONTINUE;
            }
        }catch(Exception e){
            requestInfo.getRequestManager().setMessage("111100");
            requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
            log.info("触发流程:"+requestName+";执行接口类名:"+this.getClass().getName()+";发起请求RequestId:" + requestId + ";程序异常!");
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