package esteem.jun.wanxiang.action;

import com.alibaba.fastjson.JSONObject;
import esteem.jun.common.util.APIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:Cao
 * @Date:2024-1-17
 * @Description:SRM审批回调接口
 */
public class SRMCallbackAction {
    private Log log = LogFactory.getLog(SRMCallbackAction.class.getName());
    private String logstatus;

    public String execute(RequestInfo requestInfo) {

        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        JSONObject Info = new JSONObject();
        Info.put("Taskid",baseActionInfo.getMainTableValue("Taskid"));
        Info.put("Account",baseActionInfo.getRequestManager().getUser().getLoginid());
        Info.put("PostAction","确认");
        JSONObject FromData = new JSONObject();
        FromData.put("MainTable","PL_COMMON_HEAD_P");
        JSONObject Value = new JSONObject();
        Value.put("ISOASP","是");
        FromData.put("Value",Value);
        Info.put("FromData",FromData);
        Info.put("ProcessType","Process");
        log.info("回传信息："+Info.toString());
        String result= APIUtil.CreateDepartment(Info.toString());
        return Action.SUCCESS;
    }
}
