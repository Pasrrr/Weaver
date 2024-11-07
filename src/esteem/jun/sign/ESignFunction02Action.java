package esteem.jun.sign;

import com.tencentcloudapi.ess.v20201111.models.UploadFile;
import esteem.jun.wanxiang.action.BaseActionInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2023-02-13 9:29
 * @Description: 新增字段 ejkzt
 */
public class ESignFunction02Action extends BaseBean implements Action {
    private Log log = LogFactory.getLog(ESignFunction02Action.class.getName());
    public static final Map<String,String> status_flag= new HashMap<String,String>();

    // private String yyWorkflowid;

    static{
        //未执行
        status_flag.put("bs0","0");
        //文件已上传
        status_flag.put("bs1","1");

        //文件准备完成
        status_flag.put("bs2","2");
        //创建签署文件流程完成
        status_flag.put("bs3","3");
        //获取签署链接完成
        status_flag.put("bs4","4");
        //签署完成
        status_flag.put("bs5","5");
        //签署后文件下载完成
        status_flag.put("bs6","6");
    }

    public String execute(RequestInfo requestInfo) {
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        String workflowId =requestInfo.getWorkflowid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:"+requestId+"执行开始!");
        String tableName = requestInfo.getRequestManager().getBillTableName();
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        RecordSet recordSet = new RecordSet();
        try {
            // 相关状态
            String ejkzt =baseActionInfo.getMainTableValue("ejkzt");
            String sql ="";
            if(ejkzt.equals(status_flag.get("bs1"))){
                log.info("############################################创建签署文件流程############################################begin");




                log.info("############################################创建签署文件流程############################################end");
            }
        } catch (Exception e) {
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
            //return baseActionInfo.handleExceptionResult("系统异常,请联系系统管理员!");
        }finally {
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";执行结束!");
        }
        return Action.SUCCESS;
    }

/*    public String getYyWorkflowid() {
        return yyWorkflowid;
    }

    public void setYyWorkflowid(String yyWorkflowid) {
        this.yyWorkflowid = yyWorkflowid;
    }*/
}