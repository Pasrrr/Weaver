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
public class MaterialInfoAction extends BaseBean implements Action {
    private Log log = LogFactory.getLog(MaterialInfoAction.class.getName());
    private String logstatus;

    public String execute(RequestInfo requestInfo) {
        long startTime = System.currentTimeMillis();
        String sapurl = Prop.getPropValue("wanxiang", "sap.wlzsj.url");
        String urn = Prop.getPropValue("wanxiang", "sap.wlzsj.urn");
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
            contentXml.append("<GT_ITEM>");
            List<Map<String, String>> detailData1 = baseActionInfo.getDetailTableData(1);
            List<Map<String, String>> detailData2 = baseActionInfo.getDetailTableData(2);
            List<Map<String, String>> detailData3 = baseActionInfo.getDetailTableData(3);
            List<Map<String, String>> detailData4 = baseActionInfo.getDetailTableData(4);
            List<Map<String, String>> detailData5 = baseActionInfo.getDetailTableData(5);
            List<Map<String, String>> detailData6 = baseActionInfo.getDetailTableData(6);
            List<Map<String, String>> detailData7 = baseActionInfo.getDetailTableData(7);
            for (int i=0;i<detailData1.size();i++) {
                contentXml.append("<item>");
                /**物料类型*/
                contentXml.append("<MTART>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MTART") + "</MTART>");
                /**行业类型*/
                contentXml.append("<MBRSH>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MBRSH") + "</MBRSH>");
                /**物料编码*/
                contentXml.append("<MATNR>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MATNR") + "</MATNR>");
                /**基本计量单位*/
                contentXml.append("<MEINS>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MEINS") + "</MEINS>");
                /**物料描述*/
                contentXml.append("<MAKTX>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MAKTX") + "</MATNR>");
                /**物料组*/
                contentXml.append("<MATKL>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "MATKL") + "</MATKL>");
                /**旧物料号*/
                contentXml.append("<BISMT>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "BISMT") + "</BISMT>");
                /**毛重*/
                contentXml.append("<BRGEW>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "BRGEW") + "</BRGEW>");
                /**净重*/
                contentXml.append("<NTGEW>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "NTGEW") + "</NTGEW>");
                /**批次*/
                contentXml.append("<XCHPF>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "NTGEW") + "</XCHPF>");
                /**工厂*/
                contentXml.append("<WERKS>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "WERKS") + "</WERKS>");
                /**库存地点*/
                contentXml.append("<LGORT>" + baseActionInfo.getDetailTableValue(detailData1.get(i), "LGORT") + "</LGORT");
                /**采购组*/
                contentXml.append("<EKGRP>" + baseActionInfo.getDetailTableValue(detailData2.get(i), "EKGRP") + "</EKGRP>");
                /**是否批次管理*/
                String XCHPF="";
                switch (baseActionInfo.getDetailTableValue(detailData2.get(i), "XCHPF")){
                    case "0":XCHPF="X";
                        break;
                    default:
                        break;
                }
                contentXml.append("<XCHPF>" + XCHPF + "</XCHPF>");
                /**采购计量单位*/
                contentXml.append("<BSTME>" + baseActionInfo.getDetailTableValue(detailData2.get(i), "BSTME") + "</BSTME>");
                /**销售组织*/
                String VKORG="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "VKORG")){
                    case "0":VKORG="6000";
                        break;
                    default:
                        break;
                }
                contentXml.append("<VKORG>" + VKORG + "</VKORG>");
                /**分销渠道*/
                String VTWEG="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "VTWEG")){
                    case "0":VTWEG="10";
                        break;
                    default:
                        break;
                }
                contentXml.append("<VTWEG>" + VTWEG + "</VTWEG>");
                /**产品组*/
                String SPART="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "SPART")){
                    case "0":SPART="00";
                        break;
                    case "1":SPART="10";
                        break;
                    case "2":SPART="20";
                        break;
                    case "3":SPART="30";
                        break;
                    case "4":SPART="40";
                        break;
                    case "5":SPART="50";
                        break;
                    case "6":SPART="60";
                        break;
                    default:
                        break;
                }
                contentXml.append("<SPART>" + SPART + "</SPART>");
                /**销售单位*/
                contentXml.append("<VRKME>" + baseActionInfo.getDetailTableValue(detailData3.get(i), "VRKME") + "</VRKME>");
                /**交货工厂*/
                String DWERK="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "DWERK")){
                    case "0":DWERK="6000";
                        break;
                    case "1":DWERK="6001";
                        break;
                    default:
                        break;
                }
                contentXml.append("<DWERK>" + DWERK + "</DWERK>");
                /**税分类*/
                String TAKLM="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "TAKLM")){
                    case "0":TAKLM="1";
                        break;
                    case "1":TAKLM="0";
                        break;
                    default:
                        break;
                }
                contentXml.append("<TAKLM>" + TAKLM + "</TAKLM>");
                /**项目类别组*/
                String MTPOS="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "MTPOS")){
                    case "0":MTPOS="NORM";
                        break;
                    case "1":MTPOS="LEIH";
                        break;
                    default:
                        break;
                }
                contentXml.append("<MTPOS>" + MTPOS + "</MTPOS>");
                /**物料科目分配组*/
                String KTGRM="";
                switch (baseActionInfo.getDetailTableValue(detailData3.get(i), "KTGRM")){
                    case "0":KTGRM="10";
                        break;
                    case "1":KTGRM="20";
                        break;
                    case "2":KTGRM="30";
                        break;
                    case "3":KTGRM="40";
                        break;
                    case "4":KTGRM="50";
                        break;
                    case "5":KTGRM="60";
                        break;
                    case "6":KTGRM="70";
                        break;
                    case "7":KTGRM="80";
                        break;
                    case "8":KTGRM="99";
                        break;
                    default:
                        break;
                }
                contentXml.append("<KTGRM>" + KTGRM + "</KTGRM>");
                /**物料运输组*/
                contentXml.append("<MFRGR>" + baseActionInfo.getDetailTableValue(detailData3.get(i), "MFRGR") + "</MFRGR>");
                /**运输组*/
                contentXml.append("<TRAGR>" + baseActionInfo.getDetailTableValue(detailData3.get(i), "TRAGR") + "</TRAGR>");
                /**装载组*/
                contentXml.append("<LADGR>" + baseActionInfo.getDetailTableValue(detailData3.get(i), "LADGR") + "</LADGR>");
                /**可用性检查*/
                contentXml.append("<MTVFP>" + baseActionInfo.getDetailTableValue(detailData3.get(i), "MTVFP") + "</MTVFP>");
                /**跨工厂物料状态*/
                contentXml.append("<MSTAE>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "MSTAE") + "</MSTAE>");
                /**舍入值*/
                contentXml.append("<BSTRF>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "BSTRF") + "</BSTRF>");
                /**MRP类型*/
                contentXml.append("<DISMM>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DISMM") + "</DISMM>");
                /**MRP控制者*/
                contentXml.append("<DISPO>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DISPO") + "</DISPO>");
                /**批量大小*/
                contentXml.append("<DISLS>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DISLS") + "</DISPO>");
                /**最小批量*/
                contentXml.append("<BSTMI>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "BSTMI") + "</BSTMI>");
                /**计划时界*/
                contentXml.append("<DISMM>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DISMM") + "</DISMM>");
                /**MRP组*/
                contentXml.append("<DISGR>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DISGR") + "</DISGR>");
                /**MRP 范围*/
                contentXml.append("<BERID>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "BERID") + "</BERID>");
                /**外部采购仓储地点*/
                contentXml.append("<LGFSB>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "LGFSB") + "</LGFSB>");
                /**计划交货时间*/
                contentXml.append("<PLIFZ>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "PLIFZ") + "</PLIFZ>");
                /**计划边际码*/
                contentXml.append("<FHORI>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "FHORI") + "</FHORI>");
                /**收货处理时间*/
                contentXml.append("<WEBAZ>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "WEBAZ") + "</WEBAZ>");
                /**采购类型*/
                String BESKZ="";
                switch (baseActionInfo.getDetailTableValue(detailData4.get(i), "BESKZ")){
                    case "0":BESKZ="E";
                        break;
                    case "1":BESKZ="F";
                        break;
                    case "2":BESKZ="X";
                        break;
                    default:
                        break;
                }
                contentXml.append("<BESKZ>" + BESKZ + "</BESKZ>");
                /**反冲*/
                String RGEKZ="";
                switch (baseActionInfo.getDetailTableValue(detailData4.get(i), "RGEKZ")){
                    case "0":RGEKZ="1";
                        break;
                    case "1":RGEKZ="2";
                        break;
                    default:
                        break;
                }
                contentXml.append("<RGEKZ>" + RGEKZ + "</RGEKZ>");
                /**特殊采购类型*/
                String SOBSL="";
                switch (baseActionInfo.getDetailTableValue(detailData4.get(i), "SOBSL")){
                    case "0":SOBSL="10";
                        break;
                    case "1":SOBSL="30";
                        break;
                    default:
                        break;
                }
                contentXml.append("<SOBSL>" + SOBSL + "</SOBSL>");
                /**生产仓储地点*/
                contentXml.append("<LGPRO>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "LGPRO") + "</LGPRO>");
                /**自制生产时间*/
                contentXml.append("<DZEIT>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "DZEIT") + "</DZEIT>");
                /**安全库存*/
                contentXml.append("<EISBE>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "EISBE") + "</EISBE>");
                /**策略组*/
                String STRGR="";
                switch (baseActionInfo.getDetailTableValue(detailData4.get(i), "STRGR")){
                    case "0":STRGR="40";
                        break;
                    default:
                        break;
                }
                contentXml.append("<STRGR>" + STRGR + "</STRGR>");
                /**消耗模式*/
                contentXml.append("<VRMOD>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "VRMOD") + "</VRMOD>");
                /**逆向消耗期间*/
                contentXml.append("<VINT1>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "VINT1") + "</VINT1>");
                /**向前消耗期间*/
                contentXml.append("<VINT2>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "VINT2") + "</VINT2>");
                /**可用性检查*/
                contentXml.append("<MTVFP>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "MTVFP") + "</MTVFP>");
                /**独立/集中*/
                contentXml.append("<SBDKZ>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "SBDKZ") + "</SBDKZ>");
                /**生产管理员*/
                contentXml.append("<FEVOR>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "FEVOR") + "</FEVOR>");
                /**生产单位*/
                contentXml.append("<FRTEM>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "FRTEM") + "</FRTEM>");
                /**生产计划参数文件 */
                String SFCPF="";
                switch (baseActionInfo.getDetailTableValue(detailData4.get(i), "SFCPF")){
                    case "0":SFCPF="000001";
                        break;
                    default:
                        break;
                }
                contentXml.append("<SFCPF>" + SFCPF + "</SFCPF>");
                /**容差数据未限制的*/
                contentXml.append("<UEETK>" + baseActionInfo.getDetailTableValue(detailData4.get(i), "rcsjwxzd") + "</UEETK>");
                /**检验类型*/
                contentXml.append("<ART>" + baseActionInfo.getDetailTableValue(detailData5.get(i), "ART") + "</ART>");
                /**检验类型活动*/
                contentXml.append("<AKTIV>" + baseActionInfo.getDetailTableValue(detailData5.get(i), "AKTIV") + "</AKTIV>");
                /**检验天数*/
                contentXml.append("<PRFRQ>" + baseActionInfo.getDetailTableValue(detailData5.get(i), "PRFRQ") + "</PRFRQ>");
                /**价格确定*/
                String MLAST="";
                switch (baseActionInfo.getDetailTableValue(detailData6.get(i), "MLAST")){
                    case "0":MLAST="2";
                    break;
                    case "1":MLAST="3";
                }
                contentXml.append("<MLAST>" + MLAST + "</MLAST>");
                /**价格控制*/
                String VPRSV="";
                switch (baseActionInfo.getDetailTableValue(detailData6.get(i), "VPRSV")){
                    case "0":VPRSV="S";
                    break;
                    case "1":VPRSV="V";
                    break;
                }
                contentXml.append("<VPRSV>" + VPRSV + "</VPRSV>");
                /**价格单位*/
                contentXml.append("<PEINH>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "PEINH") + "</PEINH>");
                /**评估类*/
                contentXml.append("<BKLAS>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "BKLAS") + "</BKLAS>");
                /**评估类别*/
                contentXml.append("<BWTTY>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "BWTTY") + "</BWTTY>");
                /**标准价格*/
                contentXml.append("<STPRS>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "STPRS") + "</STPRS>");
                /**移动平均价格*/
                contentXml.append("<VMVER>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "VMVER") + "</VMVER>");
                /**VC:销售订单库存*/
                contentXml.append("<EKLAS>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "EKLAS") + "</EKLAS>");
                /**无成本核算*/
                contentXml.append("<NCOST>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "NCOST") + "</NCOST>");
                /**用QS的成本估算*/
                contentXml.append("<EKALR>" + "X" + "</EKALR>");
                /**物料来源*/
                contentXml.append("<HKMAT>" + "X" + "</HKMAT>");
                /**利润中心*/
                String PRCTR="";
                switch (baseActionInfo.getDetailTableValue(detailData6.get(i), "PRCTR")){
                    case "0":PRCTR="L600000000";
                    break;
                    case "1":PRCTR="L600100000";
                    break;
                }
                contentXml.append("<PRCTR>" + PRCTR + "</PRCTR>");
                /**差异码*/
                contentXml.append("<AWSLS>" + "000001" + "</AWSLS>");
                /**成本核算批量*/
                contentXml.append("<LOSGR>" + baseActionInfo.getDetailTableValue(detailData6.get(i), "LOSGR") + "</LOSGR>");
                /**仓库号*/
                contentXml.append("<LGNUM>" + "6000" + "</LGNUM>");
                /**仓储类型*/
                contentXml.append("<LGTYP>" + baseActionInfo.getDetailTableValue(detailData7.get(i), "cclx") + "</LGTYP>");
                /**入库控制*/
                contentXml.append("<PUT_STRA>" + baseActionInfo.getDetailTableValue(detailData7.get(i), "cclx") + "</PUT_STRA>");
                /**补货单位*/
                contentXml.append("<IS_VIRTUAL>" + baseActionInfo.getDetailTableValue(detailData7.get(i), "bhdw") + "</IS_VIRTUAL>");
                contentXml.append("</item>");
            }
            contentXml.append("<GT_ITEM>");
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
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("采购申购流程流程-SAP", "9", soapXml, resultXml, result.toString(), "", requestId, "OA", "-1", result.getCode(), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
            /**更新接口状态*/
            String sql="update "+tableName+" set sapfhbs='"+result.getCode()+"',sapfhjg='"+result.getMsg()+"' where requestid ='"+requestId+"'";
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