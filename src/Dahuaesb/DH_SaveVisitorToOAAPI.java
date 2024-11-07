package Dahuaesb;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * @author test
 * @title: DH_SaveVisitorToOAAPI
 * @description: 大华-OA访客签到信息API
 * @date 2023/4/22 12:13
 */
public class DH_SaveVisitorToOAAPI {
    private static Log log = LogFactory.getLog(DH_SaveVisitorToOAAPI.class);

    public Map execute(Map param) {
        log.info("大华-OA访客签到信息API--->begin");
        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();

        Boolean success = false;
        int totalnum = 0;
        RecordSet rs_update = new RecordSet();
        RecordSet rs_query = new RecordSet();
        //流程单号
        String tablename = "formtable_main_" + Util.null2String(param.get("formid"));
        String orderNumber = Util.null2String(param.get("OrderNumber"));
        String querysql = "select id as mainid from " + tablename + " where  dh='" + orderNumber + "'";
        log.info("querysql:"+querysql);
        rs_query.execute(querysql);
        if (rs_query.next()) {
            //获取主表数据id
            String mainid = rs_query.getString("mainid");
            log.info("mainid:"+mainid);

            if (mainid.equals("")) {
                jsonObj_result.put("errMsg", "流程[mainid]获取失败,请核准OA[流程编号]是否有误!");
                jsonObj_result.put("success", "flase");
                jsonObj_result.put("errarr", "");
                return jsonObj_result;
            } else {
                String visitorDate = Util.null2String(param.get("VisitorDate"));
                if (visitorDate.equals("")) {
                    jsonObj_result.put("errMsg", "入参[VisitorDate]为空,请核准后重试!");
                    jsonObj_result.put("success", success);
                    return jsonObj_result;
                } else {
                    JSONArray visitorDatearr = JSONArray.parseArray(visitorDate);
                    Iterator ir = visitorDatearr.iterator();
                    while (ir.hasNext()) {
                        try {
                            JSONObject visitorDateobj = (JSONObject) ir.next();
                            //访客姓名
                            String visitorName = visitorDateobj.getString("VisitorName");
                            //访客身份证
                            String personNumber = visitorDateobj.getString("PersonNumber");
                            //刷卡时间 yyyy-MM-dd HH:mm:ss
                            String swingTime = visitorDateobj.getString("swingTime");
                            //刷卡点位（通道名称）
                            String swingpositon = visitorDateobj.getString("swingpositon");
                            //进出门类型：进门，出门
                            String enterOrExit = visitorDateobj.getString("enterOrExit");
                            //更新数据 - begin
                            //更新
                            String updatesql = "INSERT INTO " + tablename + "_dt2 " +
                                    "( mainid, VisitorName, PersonNumber, swingTime, swingpositon, enterOrExit) " +
                                    "VALUES ( " + mainid + ", '" + visitorName + "', '" + personNumber + "', '" + swingTime + "', '" + swingpositon + "', '" + enterOrExit + "')";
                            log.info("updatesql:"+updatesql);

                            rs_update.execute(updatesql);
                            //更新数据 - end
                            totalnum++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            jsonObj_result.put("errMsg", "系统异常,请联系管理员!");
                            jsonObj_result.put("success", "flase");
                            jsonObj_result.put("errarr", "");
                            return jsonObj_result;
                        }
                    }
                }
            }

        } else {
            jsonObj_result.put("errMsg", "表单在OA不存在,请查证后重试!");
            jsonObj_result.put("success", "flase");
            return jsonObj_result;
        }

        jsonObj_result.put("errMsg", "回写成功!受影响人员个数:" + totalnum);
        jsonObj_result.put("success", "true");

        return jsonObj_result;
    }

}