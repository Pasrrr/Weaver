package esteem.jun.wanxiang.action;

import com.cloudstore.api.util.Util_DateTime;
import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2024-5-9 13:06
 * @Description:验收单回调审批结果 调用 action
 */
public class AcceptancelCallbackAction extends BaseBean implements Action {
    private Log log = LogFactory.getLog(AcceptancelCallbackAction.class.getName());
    private String logstatus;
    /**单据类型 1-采购申请 2-采购订单 3-采购合同 4-质量通知单编码 5-采购发票 6-检验批编号 7-预留单 8910-验收单*/
    private String zdjlx;
    public String execute(RequestInfo requestInfo) {
        long startTime = System.currentTimeMillis();
        String workflowid =requestInfo.getWorkflowid();
        String[] zdjlxArr =zdjlx.split(",");
        String hdlx="";
        for(String zdjlxStr:zdjlxArr){
            if(workflowid.equals(zdjlxStr.split("-")[0])){
                hdlx=zdjlxStr.split("-")[1];
                break;
            }
        }
        String sapurl = Prop.getPropValue("wanxiang", "sap.zdspjg.url");
        String urn = Prop.getPropValue("wanxiang", "sap.zdspjg.urn");
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String formId = baseActionInfo.getFormId();
        String lastname =baseActionInfo.getRequestManager().getUser().getLastname();
        String tableName = requestInfo.getRequestManager().getBillTableName();
        String src = requestInfo.getRequestManager().getSrc();
        log.info("src---->"+src);
        try {
            StringBuffer contentXml = new StringBuffer();
            /**1-采购申请 采购申购流程(SAP)*/
            String ZBM="";
            if("8".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("EBELN");
            }if("9".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("EBELN");
            }if("10".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("EBELN");
            }

            /**SAP单据号*/
            contentXml.append("<ZBM>"+ZBM+"</ZBM>");
            log.info("ZBM:"+ZBM);
            contentXml.append("<HEADER_TXT>"+baseActionInfo.getMainTableValue("bz")+"</HEADER_TXT>");
            String spzt=baseActionInfo.getMainTableValue("spzt");
            /**是否审批结束，X-已完成审批*/
            if(spzt.equals("X")){
                contentXml.append("<ZBS>"+spzt+"</ZBS>");
                log.info("ZBS-->"+contentXml.toString());
            }else{
                spzt="S";
                contentXml.append("<ZBS>"+spzt+"</ZBS>");
            }

            /**单据类型*/
            contentXml.append("<ZDJLX>"+hdlx+"</ZDJLX>");
            /**OA单据编码*/
            contentXml.append("<ZNUMOA>"+requestId+"</ZNUMOA>");
            /**OA审批人*/
            contentXml.append("<ZSPR>"+lastname+"</ZSPR>");
            /**OA审批日期*/
            contentXml.append("<ZSPRQ>"+ Util_DateTime.getNowDate()+"</ZSPRQ>");

            contentXml.append("<IV_ZOAZT>4</IV_ZOAZT>");

            contentXml.append("<IS_QMEL>");
            /**OA流程单号*/
            contentXml.append("<DJBH></DJBH>");
            /**处理结果*/
            contentXml.append("<ZCLJG></ZCLJG>");
            /**回传日期*/
            contentXml.append("<LTRMN></LTRMN>");
            /**回传NCMR编号*/
            contentXml.append("<REFNUM></REFNUM>");
            contentXml.append("</IS_QMEL>");
            contentXml.append("<GT_ITEM>");
            List<Map<String, String>> detailDatas = baseActionInfo.getDetailTableData(1);
            for (Map<String, String> rowData : detailDatas) {
                contentXml.append("<item>");
                /**采购凭证的项目编号*/
                contentXml.append("<EBELN>" + baseActionInfo.getMainTableValue("EBELN") + "</EBELN>");
                /**采购凭证的项目编号*/
                contentXml.append("<EBELP>" + baseActionInfo.getDetailTableValue(rowData, "EBELP") + "</EBELP>");
                /**采购订单数量*/
                contentXml.append("<MENGE>" + baseActionInfo.getDetailTableValue(rowData, "MENGE") + "</MENGE>");
                contentXml.append("</item>");
            }
            contentXml.append("</GT_ITEM>");
            log.info("contentXml-->"+contentXml.toString());
            String soapXml = WebServiceUtil.getSoapXmlTitle(contentXml.toString(), urn);
            log.info("soapXml-->" + soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl, soapXml);
            log.info("resultXml-->" + resultXml);
            Result result = new Result();
            SOAPMessage soapMessage = SoapUtil.formatSoapString(resultXml);
            SOAPBody body = soapMessage.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            parsingXmlResult(iterator, null, result);
            if ("1".equals(logstatus)) {
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("SAP触发回调接口", "10", soapXml, resultXml, result.toString(), "", requestId, "OA", "-1", result.getCode(), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
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

    public String getZdjlx() {
        return zdjlx;
    }

    public void setZdjlx(String zdjlx) {
        this.zdjlx = zdjlx;
    }
}