package com.api.wx123.util;

import com.alibaba.fastjson.JSON;
import com.api.wx123.RequestBody;
import esteem.jun.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.file.Prop;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */

@Path("/SAP")
public class ZLowPR {
    private static Log log = LogFactory.getLog(ZLowPR.class.getName());

    @POST
    @Path("/ZLowPr")
    @Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getZlowPR(@RequestBody String MATNR){
        log.info("ZLowPRAction:接口成功进入");
        log.info("MATNR:"+MATNR);
        String result=null;
        try {
            result=GetZlow(MATNR);
        }catch (Exception e){
            throw  new RuntimeException();
        }
        log.info("ZLowPRAction:接口结束");
        log.info("JSONresult:"+result);
        return JSON.toJSONString(result);
    }

    public static String GetZlow(String MATNR) {
        long startTime = System.currentTimeMillis();
        ZLOWResult result = new ZLOWResult();
        String sapurl = Prop.getPropValue("wanxiang", "sap.cxlow.url");
        String urn = Prop.getPropValue("wanxiang", "sap.cxlow.urn");
        try {
            StringBuffer contentXml = new StringBuffer();
            /**采购申请类型*/
            contentXml.append("<EQ_MATNR>" + MATNR + "</EQ_MATNR>");
            String soapXml = WebServiceUtil.getSoapXmlTitle(contentXml.toString(), urn);
            log.info("soapXml-->" + soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl, soapXml);
            log.info("resultXml-->" + resultXml);
            SOAPMessage soapMessage = SoapUtil.formatSoapString(resultXml);
            SOAPBody body = soapMessage.getSOAPBody();
            Iterator<SOAPElement> iterator = body.getChildElements();
            parsingXmlResult(iterator, null, result);
            log.info("result-->"+result.toString());
            UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("查询物料最低价格-SAP", "7", soapXml, resultXml, result.toString(), "", "110", "OA", "-1", "S", (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), sapurl);
            ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
        } catch (Exception e) {
            log.info("捕获异常");
        }
        return result.toString();
    }
    public static void parsingXmlResult(Iterator<SOAPElement> iterator, String side, ZLOWResult result) throws ParseException, SOAPException {
        while (iterator.hasNext()) {
            SOAPElement element = (SOAPElement) iterator.next();
            if("ZLASTDT".equals(element.getNodeName())){
                if ("0000-00-00".equals(element.getValue())){
                    result.setZLASTDT(null);
                }else {
                    result.setZLASTDT(element.getValue());
                }
            }
            if("ZLASTPR".equals(element.getNodeName())){
                result.setZLASTPR(element.getValue());
            }
            if("ZLOWESTDT".equals(element.getNodeName())){
                if ("0000-00-00".equals(element.getValue())){
                    result.setZLOWESTDT(null);
                }else {
                    result.setZLOWESTDT(element.getValue());
                }
            }
            if("ZLOWESTPR".equals(element.getNodeName())){
                result.setZLOWESTPR(element.getValue());
            }
            if (null == element.getValue() && element.getChildElements().hasNext()) {
                parsingXmlResult(element.getChildElements(), side + "-----",result);
            }
        }
    }

}
