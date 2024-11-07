package esteem.jun.sign;


import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ess.v20201111.models.DescribeFileUrlsRequest;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @className: QianAPIUtil
 * @author: cao
 * @date: 2023-11-24 10:41
 * @Depiction:
 **/
public class QianAPIUtil {
    private static Log log = LogFactory.getLog(QianAPIUtil.class.getName());
/*    private final static String SECRET_ID = "AKIDZzIUUGn7D6vlN3TyRWTYXZYaUBdrzJah";
    // 需要设置环境变量 TENCENTCLOUD_SECRET_KEY，值为示例的 Gu5t9xGARNpq86cd98joQYCN3*******
    private final static String SECRET_KEY = "1viNq79x74oMjsoW9i6SwJSN4jIfojW7";*/

    /**param1 建模主表相关数据 param2 调用Action*/
    /*public static String CreateDepartment(String jsobj,String Action){
        String Result="";
        try {
            *//**配置文件获取HOST*//*
            String HOST= Prop.getPropValue("qian","qian.EndPoint.HOST");
            *//**测试用*//*
            String timestamp=String.valueOf(System.currentTimeMillis()/1000);
            //String HOST="https://ess.test.ess.tencent.cn";
            String Authorization=TencentauthorizationUtil.Getauthorization(timestamp);
            HttpClient httpClient = createSSLInsecureClient();
            HttpPost httpPost = new HttpPost(HOST);
            //httpPost.addHeader("Accept-Charset", "utf-8");
            //httpPost.addHeader("contentType", "application/json");
            httpPost.addHeader("Content-Type", "application/json");
            //httpPost.setHeader("Accept", "application/json");
            httpPost.addHeader("X-TC-Action",Action);
            httpPost.addHeader("X-TC-Version","2020-11-11");
            httpPost.addHeader("X-TC-Timestamp", String.valueOf(System.currentTimeMillis()));
            httpPost.addHeader("X-TC-Region","ap-guangzhou");
            httpPost.addHeader("Authorization",Authorization);
            //System.out.println(TencentauthorizationUtil.Getauthorization());
            //System.out.println("TC3-HMAC-SHA256 Credential=AKyDwiuUUckpog784mUERg3wzy272MnWtZ/2023-11-23/ess/tc3_request, SignedHeaders=content-type;host, Signature=cabbcb9514aa64a197d82a0f699d60fd57483bc81dd007602489eb43d54b0784");
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result;
    }*/

    public static String DownFlowFile(String jsobj, String Action){
        String Result="";
        try {
            /**配置文件获取HOST*/
            //String HOST= Prop.getPropValue("qian","Qian.EndPoint.HOST");
            /**测试用*/
            //String HOST="https://ess.test.ess.tencent.cn";
            String HOST="https://ess.tencentcloudapi.com";
            //System.out.println(HOST);
            String timestamp=String.valueOf(System.currentTimeMillis()/1000);
            HttpClient httpClient = createSSLInsecureClient();
            //System.out.println(timestamp);
            HttpPost httpPost = new HttpPost(HOST);
            //httpPost.addHeader("Accept-Charset", "utf-8");
            //httpPost.addHeader("contentType", "application/json");
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            httpPost.addHeader("X-TC-Action",Action);
            httpPost.addHeader("X-TC-Version","2020-11-11");
            httpPost.addHeader("X-TC-Timestamp", timestamp);
            String Authorization=TencentauthorizationUtil.Getauthorization(timestamp);
            //String Authorization=TencentauthorizationUtil.getAuth("AKIDZzIUUGn7D6vlN3TyRWTYXZYaUBdrzJah","1viNq79x74oMjsoW9i6SwJSN4jIfojW7",timestamp);
            httpPost.addHeader("X-TC-Region","ap-guangzhou");
            httpPost.addHeader("Authorization",Authorization);
            //System.out.println(TencentauthorizationUtil.Getauthorization());

            //System.out.println("TC3-HMAC-SHA256 Credential=AKyDwiuUUckpog784mUERg3wzy272MnWtZ/2023-11-23/ess/tc3_request, SignedHeaders=content-type;host, Signature=cabbcb9514aa64a197d82a0f699d60fd57483bc81dd007602489eb43d54b0784");
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
        } catch (Exception e) {
            throw new RuntimeException(e);
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


    public static String DescribeFileUrls(DescribeFileUrlsRequest req, String SecretId, String SecretKey, String Endpoint) throws TencentCloudSDKException {
        Credential cred = new Credential(SecretId, SecretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(Endpoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CommonClient client = new CommonClient("ess", "2020-11-11", cred, "", clientProfile);
        String result = client.call("DescribeFileUrls", DescribeFileUrlsRequest.toJsonString(req));
        return result;
    }
}
