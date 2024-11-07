package com.weaver.esb.wx123;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.dev.api.util.Util_DataCache;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import org.apache.commons.codec.digest.DigestUtils;
import weaver.general.Util;

import java.util.Date;
import java.util.Map;

/**
 * @author
 * @title: 腾讯_Token参数入参转换_从缓存读取
 * @description: TODO
 * @date 2023-02-28 16:37
 */
public class TX_GetTokenInfoCacheUtil {
    private static Log log = LogFactory.getLog(TX_GetTokenInfoCacheUtil.class);

    public Map execute(Map param) {
        //log.error("TX_Token参数入参转换_从缓存读取--->begin");
        //调用接口 header参数
        String  access_token="";

        //如果缓存内有值就从缓存读取
        if (Util_DataCache.containsKey("access_token")&&!Util_DataCache.getObjVal("access_token").equals("")) {
            //log.error("进入缓存读取Token数据...");
            access_token = Util.null2String(Util_DataCache.getObjVal("access_token") );
            log.error("登陆成功,从缓存读取access_token:" + access_token);

        } else {
            //调用认证接口
            try {
                //log.error("进入认证接口调用返回数据...");
                //清除缓存数据
                Util_DataCache.clearVal("access_token");

                String eventKey = "QW_GetTokenAPI"; //事件标识
                String params = "{" +
                        "}"; //事件请求参数
                //log.error("params:"+params);
                //获取 ESB 服务 -- 泛微产品
                EsbService service = EsbClient.getService();
                //调用ESB事件-MK_GetTokenAPI获取tokenid / entCode
                String result = service.execute(eventKey, params);
                //log.error("result:"+result);

                if(!result.equals("")){
                    JSONObject resultObject = JSONObject.parseObject(result);
                    JSONObject dataobj = resultObject.getJSONObject("data");
                    access_token = Util.null2String(dataobj.get("access_token"));
                    log.error("登陆成功,从接口返回access_token:" + access_token);
                    //将token放入缓存内-20分钟自动失效
                    Util_DataCache.setObjVal("access_token", access_token,1200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
        jsonObj_result.put("access_token", access_token);
        jsonObj_result.put("errcode","0");
        log.error("企微_Token参数入参转换_从缓存读取--->end");
        return jsonObj_result;
    }




}

