package Dahuaesb;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author test
 * @title: DH_SaveMealsToOAAPI
 * @description: 大华_OA餐补发放接口
 * @date 2023/4/22 12:13
 */
public class DH_SaveThirdRechargeToDHAPI {
    private static Log log = LogFactory.getLog(DH_SaveThirdRechargeToDHAPI.class);

    public Map execute(Map param) {
        log.info("大华_OA餐补发放接口--->begin");
        RecordSet rs_query = new RecordSet();
        //计数器
        int success_num = 0;
        StringBuilder success_sb = new StringBuilder();
        int error_num = 0;
        StringBuilder error_sb = new StringBuilder();
        //餐补规则 serialid=5,6 -> 24元 serialid=7 -> 18.5元, serialid=8,9 -> 12元
        Map<String, String> serialidmap = new HashMap<>();
        serialidmap.put("5", "24");
        serialidmap.put("6", "24");
        serialidmap.put("7", "18.5");
        serialidmap.put("8", "12");
        serialidmap.put("9", "12");
        //响应对象
        JSONObject jsonObj_result = new JSONObject();
        JSONArray paramarr = new JSONArray();
        //ESB服务
        String querysql = "SELECT * from kq_format_total where attendancedays > 0.5 and kqdate = DATE_SUB(CURDATE(),INTERVAL 1 DAY) and serialid in (5,6,7,8,9)";
        log.error("querysql:" + querysql);
        rs_query.execute(querysql);
        while (rs_query.next()) {
            JSONObject obj = new JSONObject();
            //人员id
            String resourceid = Util.null2String(rs_query.getString("resourceid"));
            //人员工号
            String certificatenum = getCertificatenumById(resourceid);
            //serialid=5,6 -> 24元 serialid=7 -> 18.5元, serialid=8,9 -> 12元
            String serialid = Util.null2String(rs_query.getString("serialid"));
            //餐补金额
            String recharge = Util.null2String(serialidmap.get(serialid));
            obj.put("personCode", certificatenum);
            obj.put("cardCashToRecharge", recharge);
            paramarr.add(obj);
            //log.error("obj:--->"+obj);
        }
        JSONObject paramobj = new JSONObject();
        paramobj.put("body", paramarr);
        String params = paramobj.toString(); //事件请求参数
        log.error("DH_SaveThirdRechargeToDHAPI_params:" + params);
        EsbService service = EsbClient.getService();
        String result = service.execute("DH_SaveThirdRechargeAPI", params);
        log.error("result:"+result);
        JSONObject resultobj = JSONObject.parseObject(result);
        String code = Util.null2String(resultobj.get("code"));
        if (!code.equals("100")) {
            String msg = Util.null2String(resultobj.get("msg"));
            jsonObj_result.put("errMsg", msg);
            jsonObj_result.put("success", "false");
            return jsonObj_result;
        } else {
            log.error("更新餐补金额成功");
        }

        jsonObj_result.put("errMsg", "更新餐补金额成功");
        jsonObj_result.put("success", "true");
        return jsonObj_result;
    }

    //ID查询身份证
    public String getCertificatenumById(String ID) {
        RecordSet rs = new RecordSet();
        String certificatenum = "";
        rs.execute("select certificatenum from hrmresource where id = '" + ID + "'");
        if (rs.next()) {
            certificatenum = Util.null2String(rs.getString("CERTIFICATENUM"));
        }
        return certificatenum;
    }
}