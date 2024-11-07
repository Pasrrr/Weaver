package HR.util;

import com.cloudstore.dev.api.util.Util_DataCache;
import com.weaver.file.Prop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.alibaba.fastjson.JSONObject;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import weaver.general.Util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class QWUtil {
    private static Log log = LogFactory.getLog(QWUtil.class.getName());

    public static String Host="https://qyapi.weixin.qq.com";

    public static String corpid="ww82eddaa7a1f68861";

    public static String corpsecret="k5iy4x7wSHu44hRoRLTGSYQAUvRv-4nIZk0WBbyjakc";

    public static String gettoken() throws IOException {
        try {
            String gettokenhost=Host+"/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet=new HttpGet(gettokenhost);
            HttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200, code);
            String Result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("code:"+code+Result);
            //System.out.println("code:"+code+Result);
            return Result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String GetTokenInfoCacheUtil(){
        String  access_token="";
        //如果缓存内有值就从缓存读取
        if (Util_DataCache.containsKey("access_token")&&!Util_DataCache.getObjVal("access_token").equals("")) {
            //log.error("进入缓存读取Token数据...");
            access_token = Util.null2String(Util_DataCache.getObjVal("access_token") );
            log.error("登陆成功,从缓存读取access_token:" + access_token);
        } else {
            //调用认证接口
            try {
                String result=gettoken();
                if(!result.equals("")){
                    JSONObject resultObject = JSONObject.parseObject(result);
                    access_token = Util.null2String(resultObject.get("access_token"));
                    log.error("登陆成功,从接口返回access_token:" + access_token);
                    //将token放入缓存内-20分钟自动失效
                    Util_DataCache.setObjVal("access_token", access_token,1200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.error("企微_Token参数入参转换_从缓存读取--->end");
        return access_token;
    }

    public static String update_staff_info(String access_token,String jsobj){
        String Result="";
        try {
            String HOST="https://qyapi.weixin.qq.com/cgi-bin/hr/update_staff_info?access_token="+access_token;
            HttpClient httpClient = createSSLInsecureClient();
            HttpPost httpPost = new HttpPost(HOST);
            HttpEntity httpEntity = new StringEntity(jsobj, "utf-8");
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200, code);
            Result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("code:"+code+Result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();;
        } catch (Exception e) {
            e.printStackTrace();
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
