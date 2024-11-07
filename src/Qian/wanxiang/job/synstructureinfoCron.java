package Qian.wanxiang.job;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2023-11-24 16:16
 * @Description:组织架构信息同步
 */
public class synstructureinfoCron extends BaseCronJob {
    private Log log = LogFactory.getLog(synstructureinfoCron.class.getName());

    /**日志开启状态 1开启 0关闭*/
    private String logstatus;

    public void execute() {
        long startTime = System.currentTimeMillis();
        log.info("------------------------------同步组织架构------------------------------开始");
        /**通过配置文件获取创建人*/
        String UserId= Prop.getPropValue("qian","UserID");
        log.info("UserID:"+UserId);
        /**测试创建人*/
        //String UserId="yDwiLUUckpkcg9vvUECtMwfEgEOo1u7c";
        /**Action-创建部门接口代码*/
        String Action="CreateIntegrationDepartment";
        /**查询sql*/
        String sqlparentDept="SELECT * FROM `hrmdepartment` WHERE SUBCOMPANYID1=6 AND canceled IS NULL AND SUPDEPID=0";
        String sqlchirdrenDept="SELECT * FROM `hrmdepartment` WHERE SUBCOMPANYID1=6 AND canceled IS NULL AND SUPDEPID!=0 AND ALLSUPDEPID IS NOT NULL";
        try {
            RecordSet ParentDept = new RecordSet();
            RecordSet ChirdrenDept =new RecordSet();
            ParentDept.execute(sqlparentDept);
            while(ParentDept.next()){
                String DeptOpenId = ParentDept.getString("id");
                String DeptName = ParentDept.getString("departmentname");
                String ParentDeptOpenId = ParentDept.getString("supdepid");
                JSONObject adddeptuser = new JSONObject();
                JSONObject adddept = new JSONObject();
                adddeptuser.put("UserId", UserId);
                adddept.put("Operator", adddeptuser);
                adddept.put("DeptName", DeptName);
                adddept.put("DeptOpenId",DeptOpenId);
                log.info("入参："+adddept.toString());
                String result = "";//QianAPIUtil.CreateDepartment(adddept.toString(), Action);
                log.info("result:"+result);
                JSONObject resultjson = JSONObject.parseObject(result);
                JSONObject resulterr = resultjson.getJSONObject("Response").getJSONObject("Error");
                //log.info("resulterr:"+resulterr);
            }
            log.info("一级部门导入完毕");
            ParentDept.execute(sqlchirdrenDept);
            while(ChirdrenDept.next()){
                String DeptOpenId = ChirdrenDept.getString("id");
                String DeptName = ChirdrenDept.getString("departmentname");
                String ParentDeptOpenId = ChirdrenDept.getString("supdepid");
                JSONObject adddeptuser = new JSONObject();
                JSONObject adddept = new JSONObject();
                adddeptuser.put("UserId", UserId);
                adddept.put("Operator", adddeptuser);
                adddept.put("DeptName", DeptName);
                adddept.put("ParentDeptOpenId", ParentDeptOpenId);
                adddept.put("DeptOpenId",DeptOpenId);
                log.info("入参："+adddept.toString());
                String result =""; //QianAPIUtil.CreateDepartment(adddept.toString(), Action);
                log.info("result:"+result);
                JSONObject resultjson = JSONObject.parseObject(result);
                JSONObject resulterr = resultjson.getJSONObject("Response").getJSONObject("Error");
                log.info("resulterr:"+resulterr);
/*                try {
                    if (!resulterr.equals(null)) {
                        log.info("resulterr:"+resulterr);
                    }else {
                        Qian_Department_log qian_department_log = new Qian_Department_log(DeptOpenId, DeptName, ParentDeptOpenId, resultjson.getJSONObject("Response").getString("deptid"));
                        ModeDataUtil.SaveQianDepartmentInfo(qian_department_log);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("------------------------------同步组织架构------------------------------结束");
    }
}
