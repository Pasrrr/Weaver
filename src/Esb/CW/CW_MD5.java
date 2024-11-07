package com.weaver.esb.wx123;

import java.util.*;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.sansec.util.encoders.Base64Encoder;
import org.apache.commons.codec.digest.DigestUtils;

public class CW_MD5 {
    private static Log log = LogFactory.getLog(CW_MD5.class);

    /**
     * @author test
     * @title: CW_MD5
     * @description: 用款审批MD5加密
     * @date 2023-8-7
     */
    public Map execute(Map<String,Object> params) {
        log.error("财务_MD5加密--->begin");
        JSONObject object_result= new JSONObject();
        String hexStr ="";
        hexStr=DigestUtils.md5Hex(params.toString());
/*        object_result.fromObject(params);*/
        return null;
    }

    public static String base64Encryption(String str) {

        if (str == null) {
            return null;
        }
        String encodeStr = "";
        try {
            Base64Encoder b64Encoder = new Base64Encoder();
/*            encodeStr = b64Encoder.encode(str.getBytes());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;

    }
}
