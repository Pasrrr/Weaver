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
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class ProjectMatiralCallbackAction extends BaseBean implements Action {

    private Log log = LogFactory.getLog(ProjectMatiralCallbackAction.class.getName());
    private String logstatus;
    /**单据类型 1-采购申请 2-采购订单 3-采购合同 4-质量通知单编码 5-采购发票 6-检验批编号 8-预留单*/
    private String zdjlx;

    public String execute(RequestInfo requestInfo) {
        long startTime = System.currentTimeMillis();
        String workflowid =requestInfo.getWorkflowid();
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
            /**预留单号*/
            String ZBM=baseActionInfo.getMainTableValue("RSNUM");
            /**专项项目号*/
            String ZSPECPRO=baseActionInfo.getMainTableValue("ZSPECPRO");
            /**SAP单据号*/
            contentXml.append("<ZBM>"+ZBM+"</ZBM>");
            //log.info("ZBM:"+ZBM);
            contentXml.append("<ZSPECPRO>"+ZSPECPRO+"</ZSPECPRO>");
            /**单据类型*/
            contentXml.append("<ZDJLX>"+7+"</ZDJLX>");
            /**OA单据编码*/
            contentXml.append("<ZNUMOA>"+requestId+"</ZNUMOA>");
            /**OA审批人*/
            contentXml.append("<ZSPR>"+lastname+"</ZSPR>");
            /**OA审批日期*/
            contentXml.append("<ZSPRQ>"+ Util_DateTime.getNowDate()+"</ZSPRQ>");
            if("0".equals(baseActionInfo.getMainTableValue("spzt"))){
                /**审批通过*/
                log.info("预留单审批状态-->"+baseActionInfo.getMainTableValue("spzt"));
                contentXml.append("<IV_ZOAZT>1</IV_ZOAZT>");
            } else if ("1".equals(baseActionInfo.getMainTableValue("spzt"))){
                contentXml.append("<IV_ZOAZT>3</IV_ZOAZT>");
            }else{
            contentXml.append("<IV_ZOAZT>2</IV_ZOAZT>");
        }
            contentXml.append("<IS_QMEL>");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            /**OA流程单号*/
            contentXml.append("<DJBH></DJBH>");
            /**处理结果*/
            contentXml.append("<ZCLJG></ZCLJG>");
            /**回传日期*/
            contentXml.append("<LTRMN></LTRMN>");
            /**回传NCMR编号*/
            contentXml.append("<REFNUM></REFNUM>");
            contentXml.append("</IS_QMEL>");
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
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("SAP触发回调接口", "5", soapXml, resultXml, result.toString(), "", requestId, "OA", "-1", result.getCode(), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
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
}
