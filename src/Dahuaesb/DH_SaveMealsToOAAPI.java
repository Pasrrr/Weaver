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
 * @title: DH_SaveMealsToOAAPI
 * @description: 保存大华餐补消费金额至OA
 * @date 2023/4/22 12:13
 */
public class DH_SaveMealsToOAAPI {
    private static Log log = LogFactory.getLog(DH_SaveMealsToOAAPI.class);

    public Map execute(Map param) {
        log.info("大华_保存餐补消费金额至OA--->begin");
        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
       // String errMsg = "0";
       // String code = "";
        Boolean success = false;
        int totalnum = 0;
        RecordSet rs_update = new RecordSet();
        RecordSet rs_query = new RecordSet();
        JSONArray errarr = new JSONArray();
        String mainarrstr = Util.null2String(param.get("mainarr"));
        if (mainarrstr.equals("")) {
            jsonObj_result.put("errMsg", "入参为空,请核准后重试!");
            jsonObj_result.put("success", success);
            jsonObj_result.put("errarr", errarr);
            return jsonObj_result;

        } else {
            JSONArray mainarr = JSONArray.parseArray(mainarrstr);
            Iterator ir = mainarr.iterator();
            while (ir.hasNext()) {
                try {

                    JSONObject mainobj = (JSONObject) ir.next();
                    //工号
                    String workcode = mainobj.getString("WORKCODE");
                    //id
                    String userid = getUserIDByWorkcode(workcode);
                    //姓名
                    //String lastname = mainobj.getString("LASTNAME");
                    //消费余额
                    Double surplus = mainobj.getDouble("SURPLUS");
                    BigDecimal bd_surplus = new BigDecimal(surplus);
                    //消费金额
                    Double consumption = mainobj.getDouble("CONSUMPTION");
                    BigDecimal bd_consumption = new BigDecimal(consumption);
                    //消费余额+消费金额
                    BigDecimal field15num = bd_consumption.add(bd_surplus);
                    log.info("消费余额:" + bd_surplus + ",消费金额:" + bd_consumption + ",消费余额+消费金额:" + field15num);

                    String querysql = "select field15 from cus_fielddata where   scopeid =- 1 and scope = 'HrmCustomFieldByInfoType' and id= " + userid;
                    log.info("querysql:" + querysql);

                    String money_cb = "";
                    rs_query.execute(querysql);
                    if (rs_query.next()) {
                        //获取当前餐补数
                        money_cb = Util.null2o(rs_query.getString("field15"));
                        log.info("money_cb:" + money_cb);
                    }

                    BigDecimal bd_money_cb = new BigDecimal(money_cb);
                    int issame = bd_money_cb.compareTo(field15num);
                    if (issame == 0) {
                        //校验通过 更新消费余额到field15
                        log.info("校验通过 更新消费余额到field15:" + issame);
                        //更新餐补金额sql
                        String updatesql = "update cus_fielddata set field15 = '" + bd_surplus + "' where id ='" + userid +
                                "'  and scopeid =- 1 and scope = 'HrmCustomFieldByInfoType'\n";
                        log.info("更新餐补金额sql:--->" + updatesql);
                        rs_update.execute(updatesql);
                        totalnum++;
                    } else {
                        JSONObject errobj = new JSONObject();
                        errobj.put("workcode",workcode);
                        errobj.put("reason","消费金额验证错误");
                        errarr.add(errobj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        jsonObj_result.put("errMsg", "回写成功!受影响人员个数:"+totalnum);
        jsonObj_result.put("success", "true");
        jsonObj_result.put("errarr", errarr);

        return jsonObj_result;
    }

    //工号转ID
    public String getUserIDByWorkcode(String workcode) {
        RecordSet rs = new RecordSet();
        String id = "";
        rs.execute("select id from hrmresource where workcode = '" + workcode + "'");
        if (rs.next()) {
            id = Util.null2String(rs.getString("id"));
        }
        return id;
    }
}