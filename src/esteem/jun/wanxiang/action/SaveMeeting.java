package esteem.jun.wanxiang.action;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class SaveMeeting extends BaseBean implements Action {
    private static Log log = LogFactory.getLog(SaveMeeting.class);

    @Override
    public String execute(RequestInfo requestInfo) {
        log.info("-----------会议室预约(提前15分钟)------------");
        String requestID=requestInfo.getRequestid();
        String requestName=requestInfo.getRequestManager().getRequestname();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        JSONObject body=new JSONObject();
        body.put("personCode",baseActionInfo.getMainTableValue("zjrsfz"));
        body.put("personName",baseActionInfo.getMainTableValue("caller"));
        body.put("startTime",baseActionInfo.getMainTableValue("begindate")+" "+adjustTime(baseActionInfo.getMainTableValue("begintime"),15));
        body.put("endTime",baseActionInfo.getMainTableValue("enddate")+" "+baseActionInfo.getMainTableValue("endtime"));
        body.put("channelCodes",baseActionInfo.getMainTableValue("hystdh"));
        EsbService service = EsbClient.getService();
        //调用ESB事件
        //String Authorization = service.execute("DH_GetAuthorization", "");
        //JSONObject Authorizationobj = JSONObject.parseObject(Authorization);
        //body.put("Authorization",Authorizationobj.getJSONObject("data").get("Authorization"));
        EsbService esbService= EsbClient.getService();
        String result=esbService.execute("DH_Savemeeting",body.toJSONString());
        log.info(body.toString());
        log.info(result);
        JSONObject resultobj = JSONObject.parseObject(result);
        String Code=resultobj.getString("code");
        if (!Code.equals("0")){
            requestInfo.getRequestManager().setMessage(Code);
            requestInfo.getRequestManager().setMessagecontent("接口运行错误:"+resultobj.getString("msg"));
            return Action.FAILURE_AND_CONTINUE;
        }
        return Action.SUCCESS;
    }

    public static String adjustTime(String timeStr, int minutes) {
        // 分割字符串获取小时和分钟部分
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        // 减去指定的分钟数
        minute -= minutes;

        // 处理可能的借位
        if (minute < 0) {
            // 借位
            hour -= 1;
            minute += 60;
        }

        // 处理小时部分的负值
        if (hour < 0) {
            hour += 24; // 假设不会跨越日期，所以借位后小时不会小于0
        }

        // 格式化调整后的时间
        return String.format("%d:%02d", hour, minute);
    }
}
