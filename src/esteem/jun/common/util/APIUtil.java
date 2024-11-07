package esteem.jun.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import weaver.file.Prop;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:CAO
 * @Date:2024-1-17
 * @Description:
 */
public class APIUtil {
    private static Log log = LogFactory.getLog(APIUtil.class.getName());
    public static String HOST = Prop.getPropValue("wanxiang", "SRM.host");

    public static String CreateDepartment(String jsobj){
        String Result="";
        try {
            /**配置文件获取HOST*/
            //String HOST= Prop.getPropValue("qian","HOST");
            /**测试用*/
            //String Authorization= TencentauthorizationUtil.Getauthorization();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(HOST);
            httpPost.addHeader("Content-Type", "application/json");
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

    public static String Callback(String jsobj,String host){
        String Result="";
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(host);
            httpPost.addHeader("Content-Type", "application/json");
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



}
