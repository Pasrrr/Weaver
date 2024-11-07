package com.api.wx123;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
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
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * @author
 * @title: DH_SavekqToOAAPI
 * @description: TODO
 * @date 2023-06-12 17:14
 */
@Path("/savetooa")
public class DH_SavekqToOAAPI extends BaseBean {

    private static Log log = LogFactory.getLog(DH_SavekqToOAAPI.class);

    public static String HOST = "https://172.16.200.11/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/service/receiveRecord";
    /**
     * 浏览器访问地址：/api/savetooa/kqinfos
     * 访问白名单(不登陆系统直接访问)配置文件地址：WEB-INF/prop/weaver_session_filter.properties
     * 正式环境HOST地址172.16.200.11 测试环境10.80.120.4
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/kqinfos")
    @RequestBody
    @Produces(MediaType.APPLICATION_JSON)
    public String getPlmMsgService(@Context HttpServletRequest request, @Context HttpServletResponse response, @RequestBody JSONObject json) {
        JSONObject jsonObj_result = new JSONObject();
        log.info("InsertToOAKQInfos_begin"+json.toString());

        String method = Util.null2String(json.get("method"));//alarm.msg
        String subsystem = Util.null2String(json.get("subsystem"));//evo-accesscontrol
        String category = Util.null2String(json.get("category"));//alarm

        //消息体
        String info = Util.null2String(json.get("info"));
        if (info.equals("")) {
            jsonObj_result.put("errMsg", "info消息体为空,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result.toJSONString();
        }
        JSONObject infoobj = JSONObject.parseObject(info);
        String extend = Util.null2String(infoobj.get("extend"));
        if (extend.equals("")) {
            jsonObj_result.put("errMsg", "extend对象为空,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result.toJSONString();
        }

//        JSONArray extendarray = JSONArray.parseArray(extend);
//        for (int i = 0; i < extendarray.size(); i++) {
//            JSONObject extendobj = extendarray.getJSONObject(i);
        JSONObject extendobj = JSONObject.parseObject(extend);
        //刷卡时间
        String swingTime = Util.null2String(extendobj.get("swingTime"));
        String carddate = "";
        String cardtime = "";

        if (swingTime.equals("") || !swingTime.contains("")) {
            jsonObj_result.put("errMsg", "刷卡时间为空或格式有误,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result.toJSONString();
        } else {
            String[] s = swingTime.split(" ");
            carddate = s[0];
            cardtime = s[1];

        }
        //人员编号
        String personCode = Util.null2String(extendobj.get("personCode"));
        String userid = "";
        if (personCode.equals("")) {
            jsonObj_result.put("errMsg", "人员编号为空,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result.toJSONString();
        } else {
            userid = getUseridBycode(personCode);
        }
        String deviceName = Util.null2String(extendobj.get("deviceName"));
        //开门类型
        String openType = Util.null2String(extendobj.get("openType"));//61
        //写入考勤表
        String msg1 = addKQInfos(userid, carddate, cardtime,deviceName);
        log.error("insert_kq:"+msg1);
        //写入线程表 - 同一天只需写入一条
        String msg2 = addKQPool(userid, carddate);
        log.error("insert_xc:"+msg2);
        log.info("resultJSON=" + jsonObj_result.toJSONString());

        JSONArray codearray=new JSONArray();
        codearray.add(Util.null2String(infoobj.get("alarmCode")));
        JSONObject codeobject=new JSONObject();
        codeobject.put("alarmCodes",codearray);
        jsonObj_result.put("code",Util.null2String(infoobj.get("alarmCodes")));
        log.info("callbackinfo:"+codeobject.toString());
        String callbackresult=callbackkqinfo(codeobject.toString());
        log.info("callbackresult"+callbackresult);
        log.info("result:"+jsonObj_result.toString());
        log.info("InsertToOAKQInfos_end");

        //String[] swingTimes=swingTime.split(" ");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = dateFormat.parse(swingTime);
            System.out.println(date);
            String starttime="09:30:00";
            Date startdate=dateFormat.parse(swingTime);
            String endtime="16:00:00";
            Date enddate=dateFormat.parse(endtime);
        if (!date.after(startdate) && date.before(enddate)) {
            MessageType messageType = MessageType.newInstance(1256); //消息来源（见文档第四点补充）
            Set<String> userIdList = new HashSet<>(); //接收人id
            userIdList.add(userid);
            String title = ""; //标题
            String context = "您在" + swingTime + "于" + deviceName + "打卡成功"; //内容
            String linkUrl = ""; //PC端链接 纯文本就传空字符串
            String linkMobileUrl = ""; //移动端链接 纯文本就传空字符串
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context, linkUrl, linkMobileUrl);
            messageBean.setCreater(1);//创建人id
//message.setBizState("0");需要修改消息状态时传入,表示消息最初状态为待处理
            // messageBean.setTargetId("121|22"); //消息来源code +“|”+业务id  需要修改消息状态时传入，这个字段是自定义的，和修改消息状态的时候传入相同的值，可做更新。
            Util_Message.store(messageBean);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jsonObj_result.toJSONString();
    }

        //写入OA考勤表-hrmschedulesign
    public String addKQInfos(String userid, String carddate, String cardtime,String deviceName) {
        //通过这个hrmschedulesign表写入，除了插入hrmschedulesign原始表，
        // 还要插入kq_format_pool考勤报表计算线程表，
        // 这个表里面只用插入人员id和日期。比如说hrmschedulesign插入了1天的打卡数据，
        // 那么不管几条，就往这个表kq_format_pool插入一条数据，
        // resourceid和kqdate字段数据就可以了，这样系统考勤日历对应的表就会有数据的。
        // 考勤日历对应的表是kq_format_detail。考勤的测试下来有两个需要优化的地方，
        // 入参添加deviceName字段设备名称。
        // 对应hrmschedulesign表addr字段，sql添加signfrom,默认值OutDataSourceSyn
        //INSERT INTO hrmschedulesign
        // (USERID,USERTYPE,SIGNFROM,ADDR,SIGNDATE,SIGNTIME,ISINCOM) V
        // ALUES(3013,1,"OutDataSourceSyn","deviceName字段名称设备","2023-06-13","13:40:23",1);
        RecordSet rs = new RecordSet();
        try {

            String insertsql = "INSERT INTO  hrmschedulesign (" +
                    "USERID ,USERTYPE, SIGNDATE , SIGNTIME,ISINCOM,ADDR,SIGNFROM)" +
                    "VALUES" +
                    "(" + userid + ",1,'" + carddate + "','" + cardtime + "',1,'"+deviceName+"','OutDataSourceSyn')";
            log.error("hrmschedulesign_insertsql:"+insertsql);
            rs.execute(insertsql);
            return "写入OA考勤表成功!";
        } catch (Exception e) {
            e.printStackTrace();
            return "写入OA考勤表失败!";
        }
    }

    //写入OA考勤表-kq_format_pool
    public String addKQPool(String userid, String carddate) {
        //表kq_format_pool插入一条数据，resourceid和kqdate字段数据就可以了
        RecordSet rs = new RecordSet();

        String insertsql = "INSERT INTO kq_format_pool " +
                "(resourceid, kqdate) " +
                "VALUES ( " + userid + ", '" + carddate + "')";
        log.error("kq_format_pool_insertsql:"+insertsql);

        rs.execute(insertsql);
        return "写入OA考勤线程表成功!";

    }
    //获取人员ID
    public String getUseridBycode(String personCode) {
        RecordSet rs = new RecordSet();
        String querysql = "SELECT id from HRMRESOURCE where certificatenum = '" + personCode + "'";
        rs.execute(querysql);
        if (rs.next()) {
            return rs.getString("id");
        }
        return "";
    }
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
