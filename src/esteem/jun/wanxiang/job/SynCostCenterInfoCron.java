package esteem.jun.wanxiang.job;

import com.tencentcloudapi.tke.v20180525.models.Switch;
import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/31
 * @Description:
 */
public class SynCostCenterInfoCron extends BaseCronJob {
    private Log log = LogFactory.getLog(SynMaterialInfoCron.class.getName());

    private String modeid;
    /**日志开启状态*/
    private String logstatus;

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------------同步成本中心主数据------------------------------开始");
        RecordSet recordSet = new RecordSet();
        String sapurl= Prop.getPropValue("wanxiang","sap.cbzx.url");
        String urn= Prop.getPropValue("wanxiang","sap.cbzx.urn");
        log.info("sapurl-->"+sapurl);
        try {
            String soapXml = WebServiceUtil.getSoapXmlTitle("",urn);
            log.info("soapXml-->"+soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl,soapXml);
            log.info("resultXml-->"+resultXml);
            Result result=new Result();
            SOAPMessage soapMessage = SoapUtil.formatSoapString(resultXml);
            SOAPBody body = soapMessage.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            parsingXmlResult(iterator, null,result);
            if(result.getCode().equals("S")){
                List<Map<String,Object>> items =result.getItems();
                for(Map<String,Object> map :items){
                    // 成本中心控制范围
                    String KOKRS= Util.null2String(map.get("KOKRS"));
                    // 成本中心
                    String KOSTL= Util.null2String(map.get("KOSTL"));
                    // 有效期截止日期
                    String DATBI= Util.null2String(map.get("DATBI"));
                    //成本中心名称
                    String KTEXT= Util.null2String(map.get("KTEXT"));
                    //系统状态
                    String STATUS= Util.null2String(map.get("STATUS"));
                    //成本中心类型
                    String KOSAR= Util.null2String(map.get("KOSAR"));
                    String sql="select * from uf_cbzxzsj where KOSTL ='"+KOSTL +"'";
                    recordSet.execute(sql);
                    if(recordSet.next()){
                        if(!KOKRS.equals(Util.null2String(recordSet.getString("KOKRS")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!KOSTL.equals(Util.null2String(recordSet.getString("KOSTL")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!DATBI.equals(Util.null2String(recordSet.getString("DATBI")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!KTEXT.equals(Util.null2String(recordSet.getString("KTEXT")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!STATUS.equals(Util.null2String(recordSet.getString("STATUS")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!KOSAR.equals(Util.null2String(recordSet.getString("KOSAR")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }else {
                            continue;
                        }
                    }
                    ModeDataUtil.SaveModeDataInfo(map,modeid,"1");
                    log.info("map:"+map.toString());
                }

            }
            log.info("result-->"+result.toString());
            if("1".equals(logstatus)) {
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("同步物料主数据", "2", soapXml, resultXml, result.toString(), "", "", "OA", "-1", result.getCode(), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("------------------------------同步成本中心主数据------------------------------结束");
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

            if("GT_ITEM".equals(element.getNodeName())){
                Iterator<SOAPElement> it = element.getChildElements();
                while (it.hasNext()) {
                    SOAPElement el = it.next();
                    if("item".equals(el.getNodeName())){
                        Map<String,Object> resultMap = new HashMap();
                        Iterator<SOAPElement> els = el.getChildElements();
                        while (els.hasNext()) {
                            SOAPElement items = els.next();
                            if (items.getNodeName().equals("KOKRS")) {
                                resultMap.put("KOKRS",items.getValue());
                            }
                            if (items.getNodeName().equals("KOSTL")) {
                                resultMap.put("KOSTL",items.getValue());
                            }
                            if (items.getNodeName().equals("DATBI")) {
                                resultMap.put("DATBI",items.getValue());
                            }
                            if (items.getNodeName().equals("KTEXT")) {
                                resultMap.put("KTEXT",items.getValue());
                            }
                            if (items.getNodeName().equals("STATUS")) {
                                resultMap.put("STATUS",items.getValue());
                            }
                            if (items.getNodeName().equals("KOSAR")) {
                                resultMap.put("KOSAR",items.getValue());
                            }
                        }
                        result.getItems().add(resultMap);
                    }
                }
            }
            if (null == element.getValue() && element.getChildElements().hasNext()) {
                parsingXmlResult(element.getChildElements(), side + "-----",result);
            }
        }
    }

    public String getModeid() {
        return modeid;
    }

    public void setModeid(String modeid) {
        this.modeid = modeid;
    }

    public String getLogstatus() {
        return logstatus;
    }

    public void setLogstatus(String logstatus) {
        this.logstatus = logstatus;
    }
}
