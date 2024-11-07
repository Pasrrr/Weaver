package HR.job;

import HR.util.QWUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2024-2-28 16:16
 * @Description:企微花名册生日 同步
 */
public class SynBirthdayInfoCron extends BaseCronJob {

    private Log log = LogFactory.getLog(SynBirthdayInfoCron.class.getName());
    @Override
    public void execute() {
        try {
            log.info("————————————————————————企微人事助手花名册更新begin——————————————————————");
            List<String> successuser=new ArrayList<>();
            List<Map<String,String>> erruser=new ArrayList<>();
            String access_token= QWUtil.GetTokenInfoCacheUtil();
            log.info("access_token:"+access_token);
            String sql ="SELECT * FROM `hrmresource` WHERE `STATUS` IN (0,1,2) AND SUBCOMPANYID1=6";
            RecordSet recordSet = new RecordSet();
            recordSet.execute(sql);
            List<Map<String, String>> hrmbd=new ArrayList<>();
            while (recordSet.next()){
                if (Util.null2String(recordSet.getString("birthday")).equals("")){
                    Map<String,String> erruserreason=new HashMap<>();
                    erruserreason.put("name",Util.null2String(recordSet.getString("lastname")));
                    erruserreason.put("errmsg","生日字段为空");
                    erruser.add(erruserreason);
                }else {
                    JSONObject data=new JSONObject();
                    data.put("userid",recordSet.getString("id"));
                    List<Map<String, String>> update_items=new ArrayList<>();
                    Map update_items_detial=new JSONObject();
                    update_items_detial.put("fieldid",11005);
                    update_items_detial.put("sub_idx",0);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = dateFormat.parse(Util.null2String(recordSet.getString("birthday")));
                    long timestamp = date.getTime();
                    update_items_detial.put("value_int64",String.valueOf(timestamp / 1000));
                    update_items.add(update_items_detial);
                    data.put("update_items",update_items);
                    String result=QWUtil.update_staff_info(access_token,data.toString());
                    JSONObject resultobj=JSONObject.parseObject(result);
                    if (resultobj.get("errmsg").equals("ok")){
                        successuser.add(recordSet.getString("lastname"));
                    }else {
                        Map<String,String> erruserreason=new HashMap<>();
                        erruserreason.put("name",recordSet.getString("lastname"));
                        erruserreason.put("errmsg",resultobj.getString("errmsg"));
                        erruser.add(erruserreason);
                    }
                }
            }
            //log.info("人员清单："+hrmbd.toString());
            log.info("更新成功人员："+successuser.toString());
            log.info("更新失败人员："+erruser.toString());
            log.info("-----------------------企微人事助手花名册更新end-------------");
        }catch (Exception e) {
            e.printStackTrace();
        }
        }

}
