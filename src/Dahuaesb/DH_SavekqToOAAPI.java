package Dahuaesb;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.workflow.msg.Message;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

/**
 * @author test
 * @title: DH_SavekqToOAAPI
 * @description: 大华-OA考勤信息写入OA API
 * @date 2023/5/07 12:13
 */
public class DH_SavekqToOAAPI {
    private static Log log = LogFactory.getLog(DH_SavekqToOAAPI.class);

    public Map execute(Map param) {
        log.info("大华-OA考勤信息写入OA API--->begin");
        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
        Message message=new Message();

        int totalnum = 0;

//        String method = Util.null2String(param.get("method"));//alarm.msg
//        String subsystem = Util.null2String(param.get("subsystem"));//evo-accesscontrol
//        String category = Util.null2String(param.get("category"));//alarm
        //消息体
        String info = Util.null2String(param.get("info"));
        if (info.equals("")) {
            jsonObj_result.put("errMsg", "info消息体为空,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result;
        }
        JSONObject infoobj = JSONObject.parseObject(info);
        String extend = Util.null2String(infoobj.get("extend"));
        if (extend.equals("")) {
            jsonObj_result.put("errMsg", "extend对象为空,请核准后重试!");
            jsonObj_result.put("success", "false");
            return jsonObj_result;
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
            return jsonObj_result;
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
            return jsonObj_result;
        } else {
            userid = getUseridBycode(personCode);
        }
        //开门类型
        String openType = Util.null2String(extendobj.get("openType"));//61
        //写入考勤表
        String msg1 = addKQInfos(userid, carddate, cardtime);
        log.error("写入考勤表"+msg1);
        //写入线程表 - 同一天只需写入一条
        String msg2 = addKQPool(userid, carddate);
        log.error("写入线程表"+msg2);

//        }


        jsonObj_result.put("errMsg", "回写成功!");
        jsonObj_result.put("success", "true");
        return jsonObj_result;
    }

    //写入OA考勤表-hrmschedulesign
    public String addKQInfos(String userid, String carddate, String cardtime) {
        //通过这个hrmschedulesign表写入，除了插入hrmschedulesign原始表，
        // 还要插入kq_format_pool考勤报表计算线程表，
        // 这个表里面只用插入人员id和日期。比如说hrmschedulesign插入了1天的打卡数据，
        // 那么不管几条，就往这个表kq_format_pool插入一条数据，
        // resourceid和kqdate字段数据就可以了，这样系统考勤日历对应的表就会有数据的。
        // 考勤日历对应的表是kq_format_detail。
        RecordSet rs = new RecordSet();
        try {
//            String insertsql = "INSERT INTO  hrmschedulesign (" +
//                    "USERID , USERTYPE , SIGNTYPE , SIGNDATE , SIGNTIME ,  " +
//                    "clientAddress , ISINCOM , SIGNFROM , LONGITUDE , LATITUDE , " +
//                    "ADDR , ISIMPORT ,SUUID , timeZone , belongdate , " +
//                    "memo , deviceInfo )" +
//                    "VALUES" +
//                    "(" + userid + ",'1','2','" + carddate + "','" + cardtime + "'," +
//                    "'','1','','',''," +
//                    "'',NULL,NULL,'GMT+8',''," +
//                    "NULL,'' " +
//                    ")";
            String insertsql = "INSERT INTO  hrmschedulesign (" +
                    "USERID , SIGNDATE , SIGNTIME)" +
                    "VALUES" +
                    "(" + userid + ",'" + carddate + "','" + cardtime + "')";
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
        //String nowdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        String querysql = "SELECT COUNT(*) as totalnum from kq_format_pool where resourceid = '" + userid + "' and kqdate = '" + carddate + "'";
//        log.error("querysql:"+querysql);
//        rs.execute(querysql);
//        if (rs.next()) {
//            int totalnum = rs.getInt("totalnum");
//            if (totalnum == 0) {
//                String insertsql = "INSERT INTO kq_format_pool " +
//                        "(resourceid, kqdate, status, created) " +
//                        "VALUES ( " + userid + ", '" + carddate + "', 1, '" + nowdatetime + "')";
                String insertsql = "INSERT INTO kq_format_pool " +
                        "(resourceid, kqdate) " +
                        "VALUES ( " + userid + ", '" + carddate + "')";
                log.error("kq_format_pool_insertsql:"+insertsql);

                rs.execute(insertsql);
                return "写入OA考勤线程表成功!";
//            } else {
//                return "重复写入OA考勤表,本次跳过!";
//            }
//        }
        //return "写入OA考勤线程表失败,未检索到考勤信息!";
    }

    //获取人员ID
    public String getUseridBycode(String personCode) {
        RecordSet rs = new RecordSet();
        String querysql = "SELECT id from HRMRESOURCE where workcode = '" + personCode + "'";
        rs.execute(querysql);
        if (rs.next()) {
            return rs.getString("id");
        }
        return "";
    }


}