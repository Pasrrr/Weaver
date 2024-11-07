package Dahuaesb;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.Map;

/**
 * @author test
 * @title: DH_SavePersonInfos
 * @description: 大华_人员批量导入定时任务
 * @date 2023-03-29 14:12
 */
public class DH_SavePersonBatchInfosCronJob {
    private static Log log = LogFactory.getLog(DH_SavePersonBatchInfosCronJob.class);

    public Map execute(Map param) {
        log.error("大华_人员批量导入定时任务--->begin");
        RecordSet rsquery = new RecordSet();

        //入参JSON对象
        JSONObject mainobj = new JSONObject();
        mainobj.put("service", "evo-thirdParty");
        JSONArray personList = new JSONArray();

        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
        //查询所有人员信息
        String queryUserSql = "SELECT\n" +
                "\ta.*,\n" +
                "\tb.field14 AS carnum,\n" +
                "\tb.field15 AS mealallowance\n" +
                "FROM\n" +
                "\thrmresource a\n" +
                "\tLEFT JOIN cus_fielddata b ON a.id = b.id \n" +
                "WHERE\n" +
                "\tb.scope = 'HrmCustomFieldByInfoType' \n" +
                "\tAND b.scopeid =- 1  \n" +
                "\tAND a.STATUS in (0,1,2,3) \n" +
                "\tAND a.workcode != '' \n" +
                "\tAND a.accounttype != 1\n" +
                "\tlimit 10";
        rsquery.execute(queryUserSql);
        while (rsquery.next()) {
            JSONObject personobj = new JSONObject();
            String id = Util.null2String(rsquery.getInt("id"));
            String lastname = Util.null2String(rsquery.getString("lastname"));//人员姓名
            String workcode = Util.null2String(rsquery.getString("workcode"));//人员编码
            String departmentid = Util.null2String(rsquery.getString("departmentid"));//部门ID
            String phone = Util.null2String(rsquery.getString("mobile"));//电话
            String email = Util.null2String(rsquery.getString("email"));//电子邮箱
            String birthday = Util.null2String(rsquery.getString("birthday"));//生日
            String sex = Util.null2String(rsquery.getString("sex"));//性别
            String carnum = Util.null2String(rsquery.getString("carnum"));//车牌号码
            // String mealallowance = Util.null2String(rsquery.getString("mealallowance"));//餐补金额
//            String icnum = Util.null2String(rsquery.getString("icnum"));//IC卡号
            //
            personobj.put("id", id);
            personobj.put("name", lastname);
            personobj.put("code", workcode);
            personobj.put("paperType", "-1");
            personobj.put("paperNumber", workcode);
            personobj.put("paperAddress", "杭州");
            personobj.put("departmentId", departmentid);
            personobj.put("sex", sex);
            personobj.put("phone", phone);
            personobj.put("email", email);
            personobj.put("birthday", birthday);
            //车辆数组
            JSONArray carsarr = new JSONArray();
            if (!carnum.equals("")) {
                carsarr.add(new JSONObject().put("carNum", carnum));
            }
            personobj.put("cars", carsarr.toJSONString());
            //自定义字段对象
//            fieldExtobj.put("mealallowance",mealallowance);
//            fieldExtobj.put("icnum",icnum);

            personList.add(personobj);
        }
        mainobj.put("personList", personList);
        log.error("personList:--->" + personList);

        //接口调用开始
        EsbService service = EsbClient.getService();
        String result = service.execute("DH_GetAuthorization", "");
        JSONObject resultobj = JSONObject.parseObject(result);
        String code = Util.null2String(resultobj.get("code"));
        log.error("resultobj:" + resultobj);
        String authorization = "";
        if (!code.equals("100")) {
            jsonObj_result.put("flag", "E");
            jsonObj_result.put("msg", "鉴权接口调用失败");
        } else {
            JSONObject data = JSON.parseObject(Util.null2String(resultobj.get("data")));
            authorization = data.getString("Authorization");
            log.error("authorization:" + authorization);
            if (!authorization.equals("")) {
                mainobj.put("Authorization", authorization);
                //调用人员批量新增接口
                EsbService service2 = EsbClient.getService();
                String result2 = service2.execute("DH_InsertPersonBatchAPI", mainobj.toJSONString());
                JSONObject resultobj2 = JSONObject.parseObject(result2);
                String code2 = Util.null2String(resultobj2.get("code"));
                if (!code2.equals("100")) {
                    jsonObj_result.put("flag", "E");
                    jsonObj_result.put("msg", "人员批量新增接口调用失败");

                } else {
                    log.error("resultobj2:" + resultobj2);
                    jsonObj_result.put("flag", "S");
                    jsonObj_result.put("msg", "人员批量新增接口调用成功");
                }
            } else {
                jsonObj_result.put("flag", "E");
                jsonObj_result.put("msg", "authorization为空");
            }
        }


        log.error("大华_人员批量导入定时任务--->end");
        return jsonObj_result;
    }


}
