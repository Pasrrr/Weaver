package esteem.jun.common.util;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import weaver.file.Prop;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-29 17:05
 * @Description:
 */
public class WebServiceUtil {
    //private static Log log = LogFactory.getLog(WebServiceUtil.class.getName());

    public static String USERNAME = Prop.getPropValue("wanxiang", "sap.username");
    //public static String USERNAME ="A123";
    public static String PASSWORD = Prop.getPropValue("wanxiang", "sap.password");
    //public static String PASSWORD ="Admin12345";
    public static String HOST = Prop.getPropValue("wanxiang", "host");
    //public static String HOST = "http://sappodap01.a123systems.com:50000";

    public static String callSapWs(String url, String xmlStr){
        String sapResult ="";
        try {
            url = HOST + url;
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
            HttpEntity httpEntity = new StringEntity(xmlStr, "UTF-8");
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            sapResult = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sapResult;
    }



    public  static String getSoapXmlTitle(String containXml,String urn){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">");
        stringBuffer.append("<soapenv:Header/>");
        stringBuffer.append("<soapenv:Body>");
        stringBuffer.append("<urn:"+urn+">");
        stringBuffer.append("<IS_REQ>");
        stringBuffer.append("<REQKEYID>?</REQKEYID>");
        stringBuffer.append("<MESSAGEID>?</MESSAGEID>");
        stringBuffer.append("<SNDPRN>?</SNDPRN>");
        stringBuffer.append("<RCVPRN>?</RCVPRN>");
        stringBuffer.append("<REQUSER>?</REQUSER>");
        stringBuffer.append("<NOTE1>?</NOTE1>");
        stringBuffer.append("<NOTE2>?</NOTE2>");
        stringBuffer.append("<NOTE3>?</NOTE3>");
        stringBuffer.append("</IS_REQ>");
        stringBuffer.append(containXml);
        stringBuffer.append("</urn:"+urn+">");
        stringBuffer.append("</soapenv:Body>");
        stringBuffer.append("</soapenv:Envelope>");
        return stringBuffer.toString();
    }

    public static SOAPMessage formatSoapString(String soapString) {
        MessageFactory msgFactory;
        try {
            msgFactory = MessageFactory.newInstance();
            SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(soapString.getBytes("UTF-8")));
            reqMsg.saveChanges();
            return reqMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}