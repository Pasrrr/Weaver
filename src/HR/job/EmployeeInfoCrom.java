package HR.job;

import HR.util.JDUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.passwordprotection.domain.HrmResource;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class EmployeeInfoCrom extends BaseCronJob {
    private Log log = LogFactory.getLog(EmployeeInfoCrom.class.getName());
    @Override
    public void execute() {
        log.info("------------------------------金蝶人员信息同步------------------------------开始");
        JSONArray erremployee=new JSONArray();
        JSONArray successemployee=new JSONArray();
        String sql="SELECT * FROM `hrmresource` WHERE `STATUS` IN (0,1,2) AND SUBCOMPANYID1=6";
        RecordSet rsquery = new RecordSet();
        RecordSet rs_update = new RecordSet();
        rsquery.execute(sql);
        while (rsquery.next()){
            //身份证
            String CERTIFICATENUM= Util.null2String(rsquery.getString("CERTIFICATENUM"));
            JSONArray data=new JSONArray();
            JSONObject json = new JSONObject(new LinkedHashMap());
            json.put("idcardNo", CERTIFICATENUM);
            data.add(json);
            //查询金蝶人员信息
            String result= JDUtil.employeeFileQuery(data);
            JSONObject resultjson= JSON.parseObject(result);
            //查询
            String resultCode=resultjson.getString("resultCode");
            //查询失败
            if (resultCode.equals(0)){
                erremployee.add(Util.null2String(rsquery.getString("lastname")));
            } else {
                //更新人力资源表
                /**生日*/
                String birthday = resultjson.getJSONArray("personList").getJSONObject(0).getString("birthday");
                /**电话*/
                String cell = resultjson.getJSONArray("personList").getJSONObject(0).getString("cell");
                /**工号*/
                String number = resultjson.getJSONArray("personList").getJSONObject(0).getString("number");
                /**姓名*/
                String name = resultjson.getJSONArray("personList").getJSONObject(0).getString("name");
                /**合同开始时间*/
                String contStartTime = resultjson.getJSONArray("personList").getJSONObject(0).getString("contStartTime");
                /**合同结束时间*/
                String contEndTime = resultjson.getJSONArray("personList").getJSONObject(0).getString("contEndTime");
                /**性别  1 男 2 女*/
                String gender = resultjson.getJSONArray("personList").getJSONObject(0).getString("gender");

                String updateSql="update hrmresource set birthday='"+birthday+"',workcode ='"+number+"',startdate='"+contStartTime+"',enddate='"+contEndTime+"',sex='"+gender+"' where CERTIFICATENUM="+CERTIFICATENUM;

                rs_update.execute(updateSql);
                //成功人员清单
                successemployee.add(Util.null2String(rsquery.getString("lastname")));

            }
        }
        log.info("------------------------------金蝶人员信息同步------------------------------结束");
    }
}
