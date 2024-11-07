package esteem.jun.wanxiang.job;

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
 * @Author: Code of Passion
 * @Date: 2022-11-29 16:16
 * @Description:物料主数据信息 同步
 */
public class SynMaterialInfoCron extends BaseCronJob {
    private Log log = LogFactory.getLog(SynMaterialInfoCron.class.getName());

    private String modeid;
    /**日志开启状态*/
    private String logstatus;

    public void execute() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------------同步物料主数据------------------------------开始");
        RecordSet recordSet = new RecordSet();
        String sapurl= Prop.getPropValue("wanxiang","sap.wlz.url");
        String urn= Prop.getPropValue("wanxiang","sap.wlz.urn");
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
                    // 物料编码
                    String MATNR= Util.null2String(map.get("MATNR"));
                    // 物料名称
                    String MAKTX= Util.null2String(map.get("MAKTX"));
                    // 单位
                    String MEINS= Util.null2String(map.get("MEINS"));
                    //删除标识
                    String LVORM= Util.null2String(map.get("LVORM"));
                    //物料组
                    String MATKL= Util.null2String(map.get("MATKL"));
                    //物料组描述
                    String WGBEZ= Util.null2String(map.get("WGBEZ"));
                    String sql="select * from uf_wlzsj where MATNR ='"+MATNR+"'";
                    recordSet.execute(sql);
                    if(recordSet.next()){
                        if(!MAKTX.equals(Util.null2String(recordSet.getString("MAKTX")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!MEINS.equals(Util.null2String(recordSet.getString("MEINS")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!LVORM.equals(Util.null2String(recordSet.getString("LVORM")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!MATKL.equals(Util.null2String(recordSet.getString("MATKL")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }
                        else if(!WGBEZ.equals(Util.null2String(recordSet.getString("WGBEZ")))){
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
        log.info("------------------------------同步物料主数据------------------------------结束");
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
                            if (items.getNodeName().equals("MATNR")) {
                                resultMap.put("MATNR",items.getValue());
                            }
                            if (items.getNodeName().equals("MAKTX")) {
                                resultMap.put("MAKTX",items.getValue());
                            }
                            if (items.getNodeName().equals("MEINS")) {
                                resultMap.put("MEINS",items.getValue());
                            }
                            if (items.getNodeName().equals("LVORM")) {
                                resultMap.put("LVORM",items.getValue());
                            }
                            if (items.getNodeName().equals("MATKL")) {
                                resultMap.put("MATKL",items.getValue());
                            }
                            if (items.getNodeName().equals("WGBEZ")) {
                                resultMap.put("WGBEZ",items.getValue());
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