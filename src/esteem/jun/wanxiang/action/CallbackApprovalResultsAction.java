package esteem.jun.wanxiang.action;

import com.cloudstore.api.util.Util_DateTime;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static weaver.general.Util.null2String;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-06 13:06
 * @Description:回调审批结果 调用 action
 */
public class CallbackApprovalResultsAction   extends BaseBean implements Action {
    private Log log = LogFactory.getLog(CallbackApprovalResultsAction.class.getName());
    private String logstatus;
    /**单据类型 1-采购申请 2-采购订单 3-采购合同 4-质量通知单编码 5-采购发票 6-检验批编号 7-预留单 8910-验收单 11-委外测试合同*/
    private String zdjlx;
    /**发票入账节点ID*/
    private String NODEID;

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
        //String formId = baseActionInfo.getFormId();
        String lastname =baseActionInfo.getRequestManager().getUser().getLastname();
        //String tableName = requestInfo.getRequestManager().getBillTableName();
        String src = requestInfo.getRequestManager().getSrc();


        log.info("src---->"+src);
        try {
            StringBuffer contentXml = new StringBuffer();
            /**1-采购申请 采购申购流程(SAP)*/
            String ZBM="";
            if("1".equals(hdlx)){
                /**采购申请编号*/
                ZBM=baseActionInfo.getMainTableValue("BANFN");
            }
            //2-采购订单 使用中 采购订单流程（SAP）
            if("2".equals(hdlx)){
                /**采购凭证编号*/
                ZBM=baseActionInfo.getMainTableValue("EBELN");
            }
            //3-采购合同 采购合同评审流程(SAP)
            if("3".equals(hdlx)){
                /**合同编号*/
                ZBM=baseActionInfo.getMainTableValue("htbh");
            }
            //4-质量通知单编码 原材料不合格处理流程(SAP)
            if("4".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("QMNUM");
            }
            //5-采购发票 使用中
            if("5".equals(hdlx)){
                /**会计凭证编号*/
                ZBM=baseActionInfo.getMainTableValue("BELNR");
                RecordSet recordSet=new RecordSet();
                RecordSet recordSet1=new RecordSet();
                String sql="select hrmresource.lastname,cus_fielddata.field13 from hrmresource join cus_fielddata on hrmresource.id =cus_fielddata.id and cus_fielddata.SCOPEID='-1' and hrmresource.ID = ?";
                String sql1="SELECT OPERATOR FROM workflow_requestlog WHERE NODEID='"+NODEID+"' AND REQUESTID=? ORDER BY LOGID DESC LIMIT 1";
                log.info("HEADER_TXT:"+null2String(requestInfo.getLastoperator()));
                log.info("AgentID"+null2String(baseActionInfo.getRequestManager().getAgentId()));
                recordSet1.executeQuery(sql1,null2String(baseActionInfo.getRequestId()));
                if (recordSet1.next()){
                    recordSet.executeQuery(sql,null2String(recordSet1.getString("OPERATOR")));
                    log.info("OPERATOR"+null2String(recordSet1.getString("OPERATOR")));
                    if (recordSet.next()){
                        contentXml.append("<HEADER_TXT>"+recordSet.getString("field13").toUpperCase()+"</HEADER_TXT>");

                        lastname=recordSet.getString("lastname");
                    }
                }
            }
            //6-检验批编号 原材料检验处理流程(SAP)
            if("6".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("PRUEFLOS");
            }
            //6-检验批编号 原材料检验处理流程(SAP)
            if("8".equals(hdlx)){
                /***/
                ZBM=baseActionInfo.getMainTableValue("EBELN");
            }
            if("11".equals(hdlx)){
                /**合同编号*/
                ZBM=baseActionInfo.getMainTableValue("cgddherp");
            }

            /**SAP单据号*/
            contentXml.append("<ZBM>"+ZBM+"</ZBM>");
            log.info("ZBM:"+ZBM);
            String [] ZBSArr={"1","2","3","5","8","9","10"};
            int ZBSString =Arrays.binarySearch(ZBSArr,hdlx);
            String spzt=baseActionInfo.getMainTableValue("spzt");
            /**是否审批结束，X-已完成审批*/
/*            if("3".equals(hdlx)){
                contentXml.append("<ZBS>X</ZBS>");
                log.info("ZBS-->"+contentXml.toString());
            }else{
                contentXml.append("<ZBS></ZBS>");
            }*/
            if(ZBSString>=0 && spzt.equals("X")){
                contentXml.append("<ZBS>"+spzt+"</ZBS>");
                log.info("ZBS-->"+contentXml.toString());
            }else{
                spzt=null;
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
            // 发票归档 审批完成
            if("5".equals(hdlx)){

                // 提交
                if("0".equals(baseActionInfo.getMainTableValue("spzt"))){
                    log.info("发票入账审批状态-->"+baseActionInfo.getMainTableValue("spzt"));
                    contentXml.append("<IV_ZOAZT>3</IV_ZOAZT>");
                }else{
                    contentXml.append("<IV_ZOAZT>4</IV_ZOAZT>");
                }
                // 退回
/*                if("1".equals(spzt)){
                    contentXml.append("<IV_ZOAZT>4</IV_ZOAZT>");
                }
                if("reject".equals(src)){
                    contentXml.append("<IV_ZOAZT>4</IV_ZOAZT>");
                }*/
            }else{
                contentXml.append("<IV_ZOAZT>4</IV_ZOAZT>");
            }
            contentXml.append("<IS_QMEL>");

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            ;
            if("4".equals(hdlx)){
                /**OA流程单号*/
                contentXml.append("<DJBH>"+requestId+"</DJBH>");
                /**处理结果*/
                contentXml.append("<ZCLJG>流程退回</ZCLJG>");
                /**回传日期*/
                contentXml.append("<LTRMN>"+format.format(new Date())+"</LTRMN>");
                /**回传NCMR编号*/
                contentXml.append("<REFNUM>"+ZBM+"</REFNUM>");
            }else{
                /**OA流程单号*/
                contentXml.append("<DJBH></DJBH>");
                /**处理结果*/
                contentXml.append("<ZCLJG></ZCLJG>");
                /**回传日期*/
                contentXml.append("<LTRMN></LTRMN>");
                /**回传NCMR编号*/
                contentXml.append("<REFNUM></REFNUM>");
            }

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

    public String getZdjlx() {
        return zdjlx;
    }

    public void setZdjlx(String zdjlx) {
        this.zdjlx = zdjlx;
    }

    public String getNODEID() {
        return NODEID;
    }

    public void setNODEID(String NODEID) {
        this.NODEID = NODEID;
    }
}