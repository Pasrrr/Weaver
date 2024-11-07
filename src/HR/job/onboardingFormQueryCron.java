package HR.job;

import HR.util.JDUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class onboardingFormQueryCron extends BaseCronJob {

    private Log log = LogFactory.getLog(onboardingFormQueryCron.class.getName());

    @Override
    public void execute() {
        log.info("------------------------------入职单查询同步------------------------------开始");
        //查询日期当天
        Date date=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.format(date);
        JSONObject data=new JSONObject();
        data.put("bizDateStart",df.format(date));
        data.put("bizDateEnd",df.format(date));
        log.info("data:"+data.toString());
        //查询金蝶人员信息
        String result= JDUtil.onboardingFormQuery(data);
        JSONObject resultjson= JSON.parseObject(result);

        String resultCode=resultjson.getString("resultCode");
        if (resultCode.equals("1")){
            JSONArray onboardingForm=resultjson.getJSONArray("onboardingForm");
            for (int i=0;i<onboardingForm.size();i++){
                JSONObject emploeeinfo=onboardingForm.getJSONObject(i);
                /**员工编号*/
                String empNumber=emploeeinfo.getString("empNumber");
                /**员工姓名*/
                String empName=emploeeinfo.getString("empName");
                /**手机号*/
                String cell=emploeeinfo.getString("cell");
                /**性别*/
                String gender=emploeeinfo.getString("gender");
                /**身份证号*/
                String idcardNo=emploeeinfo.getString("idcardNo");
                /**护照*/
                String passportNO=emploeeinfo.getString("passportNO");
                /**出生日期*/
                String birthday=emploeeinfo.getString("birthday");
                /**入职时间*/
                String enrollDate=emploeeinfo.getString("enrollDate");
                /**参加工作日期*/
                String workDate=emploeeinfo.getString("workDate");
                /**民族*/
                String folk=emploeeinfo.getString("folk");
                /**政治面貌*/
                String political=emploeeinfo.getString("political");
                /**入职部门名称*/
                String adminName=emploeeinfo.getString("adminName");
                /**入职岗位*/
                String positioName=emploeeinfo.getString("positioName");
                /**籍贯*/
                String nativePlace=emploeeinfo.getString("nativePlace");
                /**职级*/
                String positionLV=emploeeinfo.getString("positionLV");
                /**劳动合同开始时间*/
                String contStartTime=emploeeinfo.getString("contStartTime");
                /**劳动合同结束时间*/
                String contEndTime=emploeeinfo.getString("contEndTime");
                /**婚姻状况*/
                String marriage=emploeeinfo.getString("marriage");
                /**参加工作时间*/
                String jobDate=emploeeinfo.getString("jobDate");
                /**用工关系状态*/
                String empType=emploeeinfo.getString("empType");


            }
        }


    }
}
