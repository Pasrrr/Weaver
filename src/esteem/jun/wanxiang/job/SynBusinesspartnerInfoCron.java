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
 * @Date: 2024-11-5 16:16
 * @Description:商业伙伴主数据信息 同步
 */
public class SynBusinesspartnerInfoCron extends BaseCronJob {
    private Log log = LogFactory.getLog(SynBusinesspartnerInfoCron.class.getName());

    private String modeid;
    /**日志开启状态 1开启 0关闭*/
    private String logstatus;

    public void execute() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------------同步商业伙伴主数据------------------------------开始");
        RecordSet recordSet = new RecordSet();
        String sapurl= Prop.getPropValue("wanxiang","sap.syhb.url");
        String urn= Prop.getPropValue("wanxiang","sap.syhb.urn");
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
                    String PARTNER= Util.null2String(map.get("PARTNER"));
                    String NAME_ORG1= Util.null2String(map.get("NAME_ORG1"));
                    String STCD5= Util.null2String(map.get("STCD5"));
                    String BANKN= Util.null2String(map.get("BANKN"));
                    String BANKL= Util.null2String(map.get("BANKL"));
                    String BANKA= Util.null2String(map.get("BANKA"));
                    String STRAS= Util.null2String(map.get("STRAS"));
                    String STRAS1= Util.null2String(map.get("STRAS1"));
                    String MITKZ= Util.null2String(map.get("MITKZ"));
                    String sql="select * from uf_syhb where PARTNER ='"+PARTNER+"'";
                    recordSet.execute(sql);
                    if(recordSet.next()){
                        if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("NAME_ORG1")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("STCD5")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("BANKN")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("BANKL")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("BANKA")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("STRAS")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("STRAS1")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }if(!NAME_ORG1.equals(Util.null2String(recordSet.getString("MITKZ")))){
                            map.put("id",Util.null2String(recordSet.getString("id")));
                        }else{
                            continue;
                        }
                    }
                    ModeDataUtil.SaveModeDataInfo(map,modeid,"1");
                }
            }
            log.info("result-->"+result.toString());
            if("1".equals(logstatus)){
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("商业伙伴主数据同步","0", soapXml,resultXml,result.toString(),"","","OA","-1",result.getCode(),(System.currentTimeMillis() - startTime)+"ms", InetAddress.getLocalHost().getHostAddress(),sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("------------------------------同步商业伙伴主数据------------------------------结束");
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
                            if (items.getNodeName().equals("PARTNER")) {
                                resultMap.put("PARTNER",items.getValue());
                            }
                            if (items.getNodeName().equals("NAME_ORG1")) {
                                resultMap.put("NAME_ORG1",items.getValue());
                            }
                            if (items.getNodeName().equals("STCD5")) {
                                resultMap.put("STCD5",items.getValue());
                            }
                            if (items.getNodeName().equals("BANKN")) {
                                resultMap.put("BANKN",items.getValue());
                            }
                            if (items.getNodeName().equals("BANKL")) {
                                resultMap.put("BANKL",items.getValue());
                            }
                            if (items.getNodeName().equals("BANKA")) {
                                resultMap.put("BANKA",items.getValue());
                            }
                            if (items.getNodeName().equals("STRAS")) {
                                resultMap.put("STRAS",items.getValue());
                            }
                            if (items.getNodeName().equals("STRAS1")) {
                                resultMap.put("STRAS1",items.getValue());
                            }
                            if (items.getNodeName().equals("MITKZ")) {
                                resultMap.put("MITKZ",items.getValue());
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