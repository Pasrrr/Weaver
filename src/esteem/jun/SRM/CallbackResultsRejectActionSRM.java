package esteem.jun.SRM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import esteem.jun.common.util.APIUtil;
import esteem.jun.common.util.ModeDataUtil;
import esteem.jun.common.util.UF_SAP_PRO_LOG;
import esteem.jun.wanxiang.action.BaseActionInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.net.InetAddress;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class CallbackResultsRejectActionSRM extends BaseBean implements Action {

    private Log log = LogFactory.getLog(CallbackResultsRejectActionSRM.class.getName());

    private String hclx;
    /**单据类型 1-第一次回调 2-处罚单第二次回调*/
    @Override
    public String execute(RequestInfo requestInfo) {
        String Result="";
        String url="";
        JSONObject result=new JSONObject();
        long startTime = System.currentTimeMillis();
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String Account = baseActionInfo.getMainTableValue("Account");//采购员账号
        String TaskID= baseActionInfo.getMainTableValue("TaskID");//SRM的TaskID
        String requestId = requestInfo.getRequestid();
    try{
        log.info("-------------------------SRM供应商回调接口begin---------------------------------------");
        log.info(hclx);
        JSONObject date =new JSONObject();
        JSONObject FromData =new JSONObject();
        JSONObject Values=new JSONObject();
        date.put("Account",Account);
        date.put("TaskID",TaskID);
        if ("1".equals(hclx)){
            date.put("Comments","拒绝");
            url= Prop.getPropValue("wanxiang", "srm.Reject.url");
            Result= APIUtil.Callback(date.toString(),url);
            result = JSON.parseObject(Result);
            log.info("Result:"+Result);
        }else {
            date.put("PostAction","确认");
            FromData.put("MainTable","BD_SUP_Punish");
            Values.put("OASPComments","");
            Values.put("OASP","是");
            FromData.put("Values",Values.toJSONString());
            date.put("Fromdata",FromData);
            url= Prop.getPropValue("wanxiang", "srm.Approve.url");
            Result= APIUtil.Callback(date.toString(),url);
            result = JSON.parseObject(Result);
            log.info("Result:"+Result);
        }


        UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("SRM触发回调接口", "6", date.toJSONString(), Result, Result, "", requestId, "OA", "-1", result.getString("Code"), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), url);
        ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
        log.info("-------------------------SRM供应商回调接口end---------------------------------------");
        return Action.SUCCESS;
    } catch (Exception e) {
        requestInfo.getRequestManager().setMessage("111100");
        requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
        log.info("触发流程:SRM触发回调接口" + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
        return Action.FAILURE_AND_CONTINUE;
    }
    }
}
