package esteem.jun.SRM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class CallbackResultsApproveActionSRM extends BaseBean implements Action {

    private Log log = LogFactory.getLog(CallbackResultsApproveActionSRM.class.getName());

    private String hclx;
    /**单据类型 1-第一次回调 2-处罚单第二次回调*/
    @Override
    public String execute(RequestInfo requestInfo) {
        String Result="";
        JSONObject result=new JSONObject();
        long startTime = System.currentTimeMillis();
        String url= Prop.getPropValue("wanxiang", "srm.Approve.url");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String Account = baseActionInfo.getMainTableValue("Account");//采购员账号
        String TaskID= baseActionInfo.getMainTableValue("TaskID");//SRM的TaskID
        String requestId = requestInfo.getRequestid();

    try{
        log.info("-------------------------SRM回调接口begin---------------------------------------");
        JSONObject data =new JSONObject();
        JSONObject FromData =new JSONObject();
        JSONObject Values=new JSONObject();
        data.put("Account",Account);
        data.put("Taskid",TaskID);
        data.put("PostAction","确认");
        log.info(hclx);
        if ("1".equals(hclx)){
            Values.put("ISOASP","是");
            Values.put("TotalAmount",baseActionInfo.getMainTableValue("Sum"));
            Values.put("TotalExpenses",baseActionInfo.getMainTableValue("PriceSum"));
            Values.put("OASPComments","按原计划进行处罚");
            FromData.put("Values",Values);
            JSONArray ChildTable=new JSONArray();
            JSONObject BD_SUP_Punish_DTL=new JSONObject();
            JSONArray DTL1Values=new JSONArray();
            BD_SUP_Punish_DTL.put("childTN","BD_SUP_Punish_DTL");
            JSONObject BD_SUP_Punish_DTL2=new JSONObject();
            JSONArray DTL2Values=new JSONArray();
            BD_SUP_Punish_DTL2.put("childTN","BD_SUP_Punish_DTL2");
            List<Map<String,String>> DTL1 =baseActionInfo.getDetailTableData(1);
            for (int i=0;i<DTL1.size();i++){
                log.info(DTL1.get(i).toString());
                JSONObject DTL1M=new JSONObject();
                DTL1M.put("ID",DTL1.get(i).get("ids"));
                DTL1M.put("Number",DTL1.get(i).get("nunmber"));
                DTL1M.put("Price",DTL1.get(i).get("rsfs1"));
                DTL1M.put("SumPrice",DTL1.get(i).get("rsfs2"));
                log.info("DTL1M:"+DTL1M.toJSONString());
                DTL1Values.add(DTL1M);
            }
            BD_SUP_Punish_DTL.put("Values",DTL1Values);
            List<Map<String,String>> DTL2 =baseActionInfo.getDetailTableData(2);
            for (int i=0;i<DTL2.size();i++){
                log.info(DTL2.get(i).toString());
                JSONObject DTL2M=new JSONObject();
                DTL2M.put("ID",DTL2.get(i).get("ids"));
                DTL2M.put("Number",DTL2.get(i).get("rsfs6"));
                DTL2M.put("Price",DTL2.get(i).get("rsfs7"));
                DTL2M.put("SumPrice",DTL2.get(i).get("rsfs8"));
                log.info("DTL2M:"+DTL2M.toJSONString());
                DTL2Values.add(DTL2M);
            }
            BD_SUP_Punish_DTL2.put("Values",DTL2Values);
            ChildTable.add(BD_SUP_Punish_DTL);
            ChildTable.add(BD_SUP_Punish_DTL2);
            log.info("ChildTable:"+ChildTable.toJSONString());
            FromData.put("ChildTable",ChildTable);
            FromData.put("MainTable","PL_COMMON_HEAD_P");
            data.put("FromData",FromData);
            data.put("ProcessType","Process");
            data.put("Comments","测试意见");
            log.info("data："+data.toJSONString());
            Result= APIUtil.Callback(data.toString(),url);
            result = JSON.parseObject(Result);
            log.info("Result:"+Result);
        log.info("Result:"+Result);
        }else {
            Values.put("OASP","是");
            Values.put("TotalAmount",baseActionInfo.getMainTableValue("Sum"));
            Values.put("TotalExpenses",baseActionInfo.getMainTableValue("PriceSum"));
            Values.put("OASPComments","按原计划进行处罚");
            FromData.put("Values",Values);
            JSONArray ChildTable=new JSONArray();
            JSONObject BD_SUP_Punish_DTL=new JSONObject();
            JSONArray DTL1Values=new JSONArray();
            BD_SUP_Punish_DTL.put("childTN","BD_SUP_Punish_DTL");
            JSONObject BD_SUP_Punish_DTL2=new JSONObject();
            JSONArray DTL2Values=new JSONArray();
            BD_SUP_Punish_DTL2.put("childTN","BD_SUP_Punish_DTL2");
            List<Map<String,String>> DTL1 =baseActionInfo.getDetailTableData(1);
            for (int i=0;i<DTL1.size();i++){
                log.info(DTL1.get(i).toString());
                JSONObject DTL1M=new JSONObject();
                DTL1M.put("ID",DTL1.get(i).get("ids"));
                DTL1M.put("Number",DTL1.get(i).get("nunmber"));
                DTL1M.put("Price",DTL1.get(i).get("rsfs1"));
                DTL1M.put("SumPrice",DTL1.get(i).get("rsfs2"));
                log.info("DTL1M:"+DTL1M.toJSONString());
                DTL1Values.add(DTL1M);
            }
            BD_SUP_Punish_DTL.put("Values",DTL1Values);
            List<Map<String,String>> DTL2 =baseActionInfo.getDetailTableData(2);
            for (int i=0;i<DTL2.size();i++){
                log.info(DTL2.get(i).toString());
                JSONObject DTL2M=new JSONObject();
                DTL2M.put("ID",DTL2.get(i).get("ids"));
                DTL2M.put("Number",DTL2.get(i).get("rsfs6"));
                DTL2M.put("Price",DTL2.get(i).get("rsfs7"));
                DTL2M.put("SumPrice",DTL2.get(i).get("rsfs8"));
                log.info("DTL2M:"+DTL2M.toJSONString());
                DTL2Values.add(DTL2M);
            }
            BD_SUP_Punish_DTL2.put("Values",DTL2Values);
            ChildTable.add(BD_SUP_Punish_DTL);
            ChildTable.add(BD_SUP_Punish_DTL2);
            log.info("ChildTable:"+ChildTable.toJSONString());
            FromData.put("ChildTable",ChildTable);
            FromData.put("MainTable","BD_SUP_Punish");
            data.put("FromData",FromData);
            data.put("ProcessType","Process");
            data.put("Comments","测试意见");
            log.info("data："+data.toJSONString());
            Result= APIUtil.Callback(data.toString(),url);
            result = JSON.parseObject(Result);
            log.info("Result:"+Result);
        }


        UF_SAP_PRO_LOG uf_sap_pro_log = new UF_SAP_PRO_LOG("SRM触发回调接口", "6", data.toJSONString(), Result, Result, "", requestId, "OA", "-1", result.getString("Code"), (System.currentTimeMillis() - startTime) + "ms", InetAddress.getLocalHost().getHostAddress(), url);
        ModeDataUtil.SaveSapProLogInfo(uf_sap_pro_log);
        log.info("-------------------------SRM回调接口end---------------------------------------");
        return Action.SUCCESS;
    } catch (Exception e) {
        requestInfo.getRequestManager().setMessage("111100");
        requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
        log.info("触发流程:SRM触发回调接口" + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
        return Action.FAILURE_AND_CONTINUE;
    }
    }


}
