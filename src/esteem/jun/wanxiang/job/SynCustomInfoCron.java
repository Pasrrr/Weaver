package esteem.jun.wanxiang.job;

import weaver.file.Prop;
import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
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
 * @Description:客户主数据信息 同步
 */
public class SynCustomInfoCron extends BaseCronJob {
    private Log log = LogFactory.getLog(SynCustomInfoCron.class.getName());

    private String modeid;
    /**日志开启状态 1开启 0关闭*/
    private String logstatus;

    public void execute() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------------同步客户主数据------------------------------开始");
        RecordSet recordSet = new RecordSet();
        String sapurl= Prop.getPropValue("wanxiang","sap.khxx.url");
        String urn= Prop.getPropValue("wanxiang","sap.khxx.urn");
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
                    String KUNNR= Util.null2String(map.get("KUNNR"));
                    String NAME1= Util.null2String(map.get("NAME1"));
                    String sql="select * from uf_KNA1 where KUNNR ='"+KUNNR+"'";
                    recordSet.execute(sql);
                    if(recordSet.next()){
                        if(!NAME1.equals(Util.null2String(recordSet.getString("NAME1")))){
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
                UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("客户主数据同步","0", soapXml,resultXml,result.toString(),"","","OA","-1",result.getCode(),(System.currentTimeMillis() - startTime)+"ms", InetAddress.getLocalHost().getHostAddress(),sapurl);
                ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("------------------------------同步客户主数据------------------------------结束");
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
                            if (items.getNodeName().equals("KUNNR")) {
                                resultMap.put("KUNNR",items.getValue());
                            }
                            if (items.getNodeName().equals("NAME1")) {
                                resultMap.put("NAME1",items.getValue());
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