package com.api.wx123.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import weaver.general.Util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2023-11-10 17:05
 * @Description:
 */

public class DH_CallbackKqUtil {
        private static Log log = LogFactory.getLog(DH_CallbackKqUtil.class.getName());
        public static String HOST = "https://10.80.120.4/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/service/receiveRecord";

    public static String callbackkqinfo(String jsobj){
        String Result="";
        try {
            HttpClient httpClient = createSSLInsecureClient();
            HttpPost httpPost = new HttpPost(HOST);
            httpPost.addHeader("Accept-Charset", "utf-8");
            //httpPost.addHeader("contentType", "application/json");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            //httpPost.addHeader("Authorization", Util.null2String(getAuthorization()));
            //httpPost.addHeader("Authorization", "bearer 1:1245d90e-4b4a-4a3f-adfe-a01f9314859a");
            //String requestBody = "{\"alarmCode\": \"0ca9188048d74877873482d8d6729429\"}";
            HttpEntity httpEntity = new StringEntity(jsobj, "utf-8");
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200, code);
            Result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("code:"+code+Result);
        } catch (ClientProtocolException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return Result;
    }


    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
