package LR.wanxiang.services.impl;

import LR.wanxiang.Req.DeclassifyRequisitionReq;
import LR.wanxiang.services.WanxiangLRService;
import esteem.jun.common.util.CommonUtil;
import esteem.jun.wanxiang.action.bean.ResultMesage;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.workflow.webservices.*;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static weaver.general.Util.null2String;

public class WanxiangLRServiceImpl implements WanxiangLRService {

    private Log log = LogFactory.getLog(WanxiangLRServiceImpl.class.getName());
    String sql = " select hrmresource.id from hrmresource where email = ?";
    @Override
    public ResultMesage creatdeclassifyRequisitionWorkFlow(DeclassifyRequisitionReq declassifyRequisitionReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("LR","jmsq.workflowid");
        log.info("------------------------解密申请流程(LR)----------------------------begin");
        log.info("参数信息：" + declassifyRequisitionReq.toString());
        log.info("workflowid"+workflowid);

        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            if (StringUtils.isEmpty(declassifyRequisitionReq.getEmail())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }

            String creater = "3013";
/*            recordSet.executeQuery(sql, null2String(declassifyRequisitionReq.getEmail()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }*/
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**创建人字段*/
            mainInfoList.add("sqr");
            /**部门*/
            mainInfoList.add("bm");
            /**创建日期*/
            mainInfoList.add("sqsj");
            /**邮箱*/
            mainInfoList.add("email");
            /**文档id*/
            mainInfoList.add("Documentid");
            /**文档名称*/
            mainInfoList.add("Documentname");
            /**文档类型*/
            mainInfoList.add("Documenttype");

            Map<String, String> mainFieldsMap = new HashMap<>();
            /**创建人字段*/
            mainFieldsMap.put("sqr",createrId);
            /**部门*/
            mainFieldsMap.put("sqrbm", null2String(hrMap.get("departmentid")));
            /**创建日期*/
            mainFieldsMap.put("sqsj", null2String(df.format(now)));
            /**供应商名称*/
            mainFieldsMap.put("email",null2String(declassifyRequisitionReq.getEmail()));
            /**供应商分组*/
            mainFieldsMap.put("Documentid",null2String(declassifyRequisitionReq.getDocumentid()));
            /**纳税人登记号*/
            mainFieldsMap.put("Documentname",null2String(declassifyRequisitionReq.getDocumentname()));
            /**企业性质*/
            mainFieldsMap.put("Documenttype",null2String(declassifyRequisitionReq.getDocumenttype()));

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        }catch (Exception e) {
                e.printStackTrace();
                resultMesage.setFlag("1");
                resultMesage.setMessage("Interface exception");
                return resultMesage;
            }
        log.info("------------------------解密流程----------------------------end");
        return resultMesage;
    }


    public String creatWorkFlow(String workflowid, List<String> mainInfoList, Map<String, String> mainFieldsMap, List<List<Map<String, String>>> detailLists, String createrId, String createrName) throws UnsupportedEncodingException {

        RecordSet recordSet = new RecordSet();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String sql1 = "select * from workflow_base where  id='" + workflowid + "'";
        log.info("执行sql1-->" + sql1);
        recordSet.execute(sql1);
        recordSet.next();
        String createWorkFlowName = null2String(recordSet.getString("workflowname"));
        log.info("workflowname"+createWorkFlowName+"createrName"+createrName);
        //String createRequestName = createWorkFlowName + "-" + hrMap.get("lastname") + "-" + df.format(now);
        String createRequestName = createWorkFlowName + "-" + createrName + "-" + df.format(now);
        /*------------------------------------------创建流程--------------------------------------------------------------*/
        log.info("------------------------------------------创建流程--------------------------------------------------------------");
        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        workflowRequestInfo.setRequestName(createRequestName);//请求标题
        workflowRequestInfo.setRequestLevel("0");//请求重要级别
        workflowRequestInfo.setCreatorId(createrId);
        workflowRequestInfo.setIsnextflow("0");
        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        /**流程ID*/
        workflowBaseInfo.setWorkflowId(workflowid);
        /**流程名称*/
        workflowBaseInfo.setWorkflowName(createWorkFlowName);
        /**工作流信息*/
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);
        WorkflowMainTableInfo workflowMainTableInfo = new WorkflowMainTableInfo();
        WorkflowRequestTableRecord[] workflowRequestTableRecord = new WorkflowRequestTableRecord[1];
        WorkflowRequestTableField[] WorkflowRequestTableField = new WorkflowRequestTableField[mainInfoList.size()];
        for (int i = 0; i < mainInfoList.size(); i++) {
            WorkflowRequestTableField[i] = new WorkflowRequestTableField();
            String fieldName = mainInfoList.get(i);
            WorkflowRequestTableField[i].setFieldName(fieldName);//字段名
            WorkflowRequestTableField[i].setFieldValue(mainFieldsMap.get(fieldName));//字段值
            WorkflowRequestTableField[i].setView(true);
            WorkflowRequestTableField[i].setEdit(true);
        }
        workflowRequestTableRecord[0] = new WorkflowRequestTableRecord();
        workflowRequestTableRecord[0].setWorkflowRequestTableFields(WorkflowRequestTableField);
        workflowMainTableInfo.setRequestRecords(workflowRequestTableRecord);
        workflowRequestInfo.setWorkflowMainTableInfo(workflowMainTableInfo);
        if (detailLists != null) {
            WorkflowDetailTableInfo[] workflowDetailTableInfo = new WorkflowDetailTableInfo[detailLists.size()];//根据传入数据生成N张明细表
            for (int i = 0; i < detailLists.size(); i++) {
                List<Map<String, String>> tempList = detailLists.get(i);//每个明细表中的记录
                WorkflowRequestTableRecord[] workflowDetailTableRecord = new WorkflowRequestTableRecord[tempList.size()];//生成N行数据（N条记录）
                for (int j = 0; j < tempList.size(); j++) {
                    Map<String, String> temp = tempList.get(j);
                    WorkflowRequestTableField[] WorkflowDetailTableField = new WorkflowRequestTableField[temp.size()];//生成N个字段
                    int n = 0;
                    for (Map.Entry<String, String> entrySet : temp.entrySet()) {
                        //遍历map，给每个字段赋予字段名及值，设置显示属性
                        WorkflowDetailTableField[n] = new WorkflowRequestTableField();
                        WorkflowDetailTableField[n].setFieldName(entrySet.getKey());
                        WorkflowDetailTableField[n].setFieldValue(entrySet.getValue());
                        WorkflowDetailTableField[n].setView(true);
                        WorkflowDetailTableField[n].setEdit(true);
                        ++n;
                    }
                    workflowDetailTableRecord[j] = new WorkflowRequestTableRecord();//每行明细数据
                    workflowDetailTableRecord[j].setWorkflowRequestTableFields(WorkflowDetailTableField);//设置每行记录的字段
                }
                workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();//每张明细表
                workflowDetailTableInfo[i].setWorkflowRequestTableRecords(workflowDetailTableRecord);//设置每张明细表的记录
            }
            workflowRequestInfo.setWorkflowDetailTableInfos(workflowDetailTableInfo);//设置所有明细表
        }

        WorkflowService workflowService = new WorkflowServiceImpl();
        String _ret = workflowService.doCreateWorkflowRequest(workflowRequestInfo, Integer.parseInt(createrId));
        return _ret;
    }

}
