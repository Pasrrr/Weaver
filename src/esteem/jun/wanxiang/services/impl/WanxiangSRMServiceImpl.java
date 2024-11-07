package esteem.jun.wanxiang.services.impl;

import esteem.jun.common.util.CommonUtil;
import esteem.jun.wanxiang.action.bean.ResultMesage;
import esteem.jun.wanxiang.req.*;
import esteem.jun.wanxiang.services.WanxiangSRMService;
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

public class WanxiangSRMServiceImpl implements WanxiangSRMService {

    private Log log = LogFactory.getLog(WanxiangSRMServiceImpl.class.getName());
    String sql = " select hrmresource.id from hrmresource join cus_fielddata on hrmresource.id =cus_fielddata.id and cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = ?";
    @Override
    public ResultMesage creatsupplierRequisitionWorkFlow(SupplierRequisitionReq supplierRequisitionReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang","gysxz.workflowid");
        log.info("------------------------供应商新增流程(SRM)----------------------------begin");
        log.info("参数信息：" + supplierRequisitionReq.toString());

        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            if (StringUtils.isEmpty(supplierRequisitionReq.getAccount())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }

            //String creater = "3013";
            Map<String, String> hrMap = CommonUtil.getHRMapByloginid(supplierRequisitionReq.getAccount());
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**创建人字段*/
            mainInfoList.add("sqr");
            /**部门*/
            mainInfoList.add("sqrbm");
            /**创建日期*/
            mainInfoList.add("sqsj");
            /**供应商名称*/
            mainInfoList.add("NAME1");
            /**供应商分组*/
            mainInfoList.add("KTOKK");
            /**纳税人登记号*/
            mainInfoList.add("STCD5");
            /**企业性质*/
            mainInfoList.add("ANRED");
            /**国家*/
            mainInfoList.add("LAND1");
            /**地区*/
            mainInfoList.add("ORT02");
            /**城市*/
            mainInfoList.add("ORT01");
            /**邮政编码*/
            mainInfoList.add("PSTLZ");
            /**供应商联系人*/
            mainInfoList.add("TELF1");
            /**供应商电话*/
            mainInfoList.add("TELF2");
            /**供应商邮箱*/
            mainInfoList.add("TELBX");
            /**是否为客户*/
            mainInfoList.add("KUNNR");
            /**银行名称*/
            mainInfoList.add("BANKA");
            /**银行账号*/
            mainInfoList.add("BANKN");
            /**银行代码*/
            mainInfoList.add("BANKL");
            /**订单货币*/
            mainInfoList.add("WAERS");
            /**付款条件*/
            mainInfoList.add("ZTERM");
            /**付款条件文本*/
            mainInfoList.add("VTEXT");
            /**SRM流程ID*/
            mainInfoList.add("TaskID");
            /**Account*/
            mainInfoList.add("account");

            Map<String, String> mainFieldsMap = new HashMap<>();
            /**创建人字段*/
            mainFieldsMap.put("sqr",createrId);
            /**部门*/
            mainFieldsMap.put("sqrbm", null2String(hrMap.get("departmentid")));
            /**创建日期*/
            mainFieldsMap.put("sqsj", null2String(df.format(now)));
            /**供应商名称*/
            mainFieldsMap.put("NAME1",null2String(supplierRequisitionReq.getNAME1()));
            /**供应商分组*/
            mainFieldsMap.put("KTOKK",null2String(supplierRequisitionReq.getKTOKK()));
            /**纳税人登记号*/
            mainFieldsMap.put("STCD5",null2String(supplierRequisitionReq.getSTCD5()));
            /**企业性质*/
            mainFieldsMap.put("ANRED",null2String(supplierRequisitionReq.getANRED()));
            /**国家*/
            mainFieldsMap.put("LAND1",null2String(supplierRequisitionReq.getLAND1()));
            /**地区*/
            mainFieldsMap.put("ORT02",null2String(supplierRequisitionReq.getORT02()));
            /**城市*/
            mainFieldsMap.put("ORT01",null2String(supplierRequisitionReq.getORT01()));
            /**邮政编码*/
            mainFieldsMap.put("PSTLZ",null2String(supplierRequisitionReq.getPSTLZ()));
            /**供应商联系人*/
            mainFieldsMap.put("TELF1",null2String(supplierRequisitionReq.getTELF1()));
            /**供应商电话*/
            mainFieldsMap.put("TELF2",null2String(supplierRequisitionReq.getTELF2()));
            /**供应商邮箱*/
            mainFieldsMap.put("TELBX",null2String(supplierRequisitionReq.getTELBX()));
            /**是否为客户*/
            mainFieldsMap.put("KUNNR",null2String(supplierRequisitionReq.getKUNNR()));
            /**银行名称*/
            mainFieldsMap.put("BANKA",null2String(supplierRequisitionReq.getBANKA()));
            /**银行账号*/
            mainFieldsMap.put("BANKN",null2String(supplierRequisitionReq.getBANKN()));
            /**银行代码*/
            mainFieldsMap.put("BANKL",null2String(supplierRequisitionReq.getBANKL()));
            /**订单货币*/
            mainFieldsMap.put("WAERS",null2String(supplierRequisitionReq.getWAERS()));
            /**付款条件*/
            mainFieldsMap.put("ZTERM",null2String(supplierRequisitionReq.getZTERM()));
            /**付款条件文本*/
            mainFieldsMap.put("VTEXT",null2String(supplierRequisitionReq.getVTEXT()));
            /**SRM流程ID*/
            mainFieldsMap.put("TaskID",null2String(supplierRequisitionReq.getTaskID()));
            /**account*/
            mainFieldsMap.put("account",null2String(supplierRequisitionReq.getAccount()));

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
        return resultMesage;
    }


    public ResultMesage creatPunishmentWorkFlow(PunishmentReq punishmentReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang","spd.workflowid");
        log.info("------------------------索赔单(SRM)----------------------------begin");
        log.info("参数信息：" + punishmentReq.toString());

        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            if (StringUtils.isEmpty(punishmentReq.getAccount())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }

            //String creater = "3013";

            Map<String, String> hrMap = CommonUtil.getHRMapByloginid(punishmentReq.getAccount());
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**创建人字段*/
            mainInfoList.add("sqr");
            /**部门*/
            mainInfoList.add("szbm");
            /**创建日期*/
            mainInfoList.add("sqsj");
            /**供应商名称*/
            mainInfoList.add("NAME1");
            /**供应商编码*/
            mainInfoList.add("RSFS1");
            /**索赔通知创建日期*/
            mainInfoList.add("RSFS2");
            /**供应商工厂代码*/
            mainInfoList.add("RSFS3");
            /**处罚单类型*/
            mainInfoList.add("RSFS4");
            /**索赔开始日期*/
            mainInfoList.add("RSFS5");
            /**索赔结束日期*/
            mainInfoList.add("RSFS6");
            /**QCR编号*/
            mainInfoList.add("RSFS7");
            /**问题零件号*/
            mainInfoList.add("RSFS8");
            /**问题零件名称*/
            mainInfoList.add("RSFS9");
            /**问题描述*/
            mainInfoList.add("RSFS10");
            /**SRM流程ID*/
            mainInfoList.add("TaskID");
            /**费用合计*/
            mainInfoList.add("PriceSum");
            /**明细合计*/
            mainInfoList.add("Sum");
            /**申请人账号*/
            mainInfoList.add("Account");


            Map<String, String> mainFieldsMap = new HashMap<>();
            /**创建人字段*/
            mainFieldsMap.put("sqr",createrId);
            /**申请人账号*/
            mainFieldsMap.put("Account",null2String(punishmentReq.getAccount()));
            /**部门*/
            mainFieldsMap.put("szbm", null2String(hrMap.get("departmentid")));
            /**创建日期*/
            mainFieldsMap.put("sqsj", null2String(df.format(now)));
            /**供应商名称*/
            mainFieldsMap.put("NAME1",null2String(punishmentReq.getSupName()));
            /**供应商编码*/
            mainFieldsMap.put("RSFS1",null2String(punishmentReq.getSupCode()));
            /**索赔通知创建日期*/
            mainFieldsMap.put("RSFS2",null2String(punishmentReq.getNoticeBeginDate()));
            /**供应商工厂代码*/
            mainFieldsMap.put("RSFS3",null2String(punishmentReq.getSupCode()));
            /**处罚单类型*/
            mainFieldsMap.put("RSFS4",null2String(punishmentReq.getExtendField7()));
            /**索赔开始日期*/
            mainFieldsMap.put("RSFS5",null2String(punishmentReq.getClaimantBeginDate()));
            /**索赔结束日期*/
            mainFieldsMap.put("RSFS6",null2String(punishmentReq.getClaimantEndDate()));
            /**QCR编号*/
            mainFieldsMap.put("RSFS7",null2String(punishmentReq.getQCRCode()));
            /**问题零件号*/
            mainFieldsMap.put("RSFS8",null2String(punishmentReq.getProblemPartNumber()));
            /**问题零件名称*/
            mainFieldsMap.put("RSFS9",null2String(punishmentReq.getProblemPartName()));
            /**问题描述*/
            mainFieldsMap.put("RSFS10",null2String(punishmentReq.getProblemDescribe()));
            /**SRM流程ID*/
            mainFieldsMap.put("TaskID",null2String(punishmentReq.getTaskID()));
            /**费用合计*/
            mainFieldsMap.put("PriceSum",null2String(punishmentReq.getPriceSum()));
            /**明细合计*/
            mainFieldsMap.put("Sum",null2String(punishmentReq.getSum()));

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            for (int i = 0; i < punishmentReq.getItem1().size(); i++) {
                PunishmentDetailReq punishmentDetailReq = punishmentReq.getItem1().get(i);
                log.info("参数信息：" + punishmentDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**项目*/
                dt1Map.put("NAME", null2String(punishmentDetailReq.getProject()));
                /**数量（或工时、金额）*/
                dt1Map.put("NUNMBER", null2String(punishmentDetailReq.getNumber()));
                /**单价*/
                dt1Map.put("RSFS1", null2String(punishmentDetailReq.getPrice()));
                /**小计*/
                dt1Map.put("RSFS2", null2String(punishmentDetailReq.getSumPrice()));
                /**IDS*/
                dt1Map.put("ids", null2String(punishmentDetailReq.getIDs()));
                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);
            List<Map<String, String>> detail2List = new ArrayList<>();
            for (int i = 0; i < punishmentReq.getItem2().size(); i++) {
                PunishmentDetail2Req punishmentDetail2Req = punishmentReq.getItem2().get(i);
                log.info("参数信息：" + punishmentDetail2Req.toString());
                Map<String, String> dt2Map = new HashMap<>();
                /**零件号*/
                dt2Map.put("RSFS4", null2String(punishmentDetail2Req.getPartNumber()));
                /**零件名称*/
                dt2Map.put("RSFS5", null2String(punishmentDetail2Req.getPartName()));
                /**数量*/
                dt2Map.put("RSFS6", null2String(punishmentDetail2Req.getNumber()));
                /**单价*/
                dt2Map.put("RSFS7", null2String(punishmentDetail2Req.getPrice()));
                /**小计*/
                dt2Map.put("RSFS8", null2String(punishmentDetail2Req.getSumPrice()));
                /**IDS*/
                dt2Map.put("ids", null2String(punishmentDetail2Req.getIDs()));
                detail2List.add(dt2Map);
            }
            detailLists.add(detail2List);
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

        return resultMesage;


    }


    public ResultMesage creatPunishmentChangeWorkFlow(PunishmentChangeReq punishmentChangeReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang","spbgd.workflowid");
        log.info("------------------------索赔变更单(SRM)----------------------------begin");
        log.info("参数信息：" + punishmentChangeReq.toString());

        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            if (StringUtils.isEmpty(punishmentChangeReq.getAccount())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapByloginid(punishmentChangeReq.getAccount());
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**创建人字段*/
            mainInfoList.add("sqr");
            /**部门*/
            mainInfoList.add("szbm");
            /**创建日期*/
            mainInfoList.add("sqsj");
            /**供应商名称*/
            mainInfoList.add("NAME1");
            /**供应商编码*/
            mainInfoList.add("RSFS1");
            /**索赔通知创建日期*/
            mainInfoList.add("RSFS2");
            /**供应商工厂代码*/
            mainInfoList.add("RSFS3");
            /**处罚单类型*/
            mainInfoList.add("RSFS4");
            /**索赔开始日期*/
            mainInfoList.add("RSFS5");
            /**索赔结束日期*/
            mainInfoList.add("RSFS6");
            /**QCR编号*/
            mainInfoList.add("RSFS7");
            /**问题零件号*/
            mainInfoList.add("RSFS8");
            /**问题零件名称*/
            mainInfoList.add("RSFS9");
            /**问题描述*/
            mainInfoList.add("RSFS10");
            /**SRM流程ID*/
            mainInfoList.add("TaskID");
            /**费用合计*/
            mainInfoList.add("PriceSum");
            /**明细合计*/
            mainInfoList.add("Sum");
            /**申请人账号*/
            mainInfoList.add("Account");
            /**变更理由*/
            mainInfoList.add("bgly");

            Map<String, String> mainFieldsMap = new HashMap<>();
            /**创建人字段*/
            mainFieldsMap.put("sqr",createrId);
            /**申请人账号*/
            mainFieldsMap.put("Account",null2String(punishmentChangeReq.getAccount()));
            /**部门*/
            mainFieldsMap.put("szbm", null2String(hrMap.get("departmentid")));
            /**创建日期*/
            mainFieldsMap.put("sqsj", null2String(df.format(now)));
            /**供应商名称*/
            mainFieldsMap.put("NAME1",null2String(punishmentChangeReq.getSupName()));
            /**供应商编码*/
            mainFieldsMap.put("RSFS1",null2String(punishmentChangeReq.getSupCode()));
            /**索赔通知创建日期*/
            mainFieldsMap.put("RSFS2",null2String(punishmentChangeReq.getNoticeBeginDate()));
            /**供应商工厂代码*/
            mainFieldsMap.put("RSFS3",null2String(punishmentChangeReq.getSupCode()));
            /**处罚单类型*/
            mainFieldsMap.put("RSFS4",null2String(punishmentChangeReq.getExtendField7()));
            /**索赔开始日期*/
            mainFieldsMap.put("RSFS5",null2String(punishmentChangeReq.getClaimantBeginDate()));
            /**索赔结束日期*/
            mainFieldsMap.put("RSFS6",null2String(punishmentChangeReq.getClaimantEndDate()));
            /**QCR编号*/
            mainFieldsMap.put("RSFS7",null2String(punishmentChangeReq.getQCRCode()));
            /**问题零件号*/
            mainFieldsMap.put("RSFS8",null2String(punishmentChangeReq.getProblemPartNumber()));
            /**问题零件名称*/
            mainFieldsMap.put("RSFS9",null2String(punishmentChangeReq.getProblemPartName()));
            /**问题描述*/
            mainFieldsMap.put("RSFS10",null2String(punishmentChangeReq.getProblemDescribe()));
            /**SRM流程ID*/
            mainFieldsMap.put("TaskID",null2String(punishmentChangeReq.getTaskID()));
            /**费用合计*/
            mainFieldsMap.put("PriceSum",null2String(punishmentChangeReq.getPriceSum()));
            /**明细合计*/
            mainFieldsMap.put("Sum",null2String(punishmentChangeReq.getSum()));
            /**变更理由*/
            mainFieldsMap.put("bgly",null2String(punishmentChangeReq.getReason()));

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            for (int i = 0; i < punishmentChangeReq.getItem1().size(); i++) {
                PunishmentChangeDetailReq punishmentDetailReq = punishmentChangeReq.getItem1().get(i);
                log.info("参数信息：" + punishmentDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**ID*/
                dt1Map.put("IDs", null2String(punishmentDetailReq.getIDs()));
                /**项目*/
                dt1Map.put("NAME", null2String(punishmentDetailReq.getProject()));
                /**数量（或工时、金额）*/
                dt1Map.put("NUNMBER", null2String(punishmentDetailReq.getNumber()));
                /**单价*/
                dt1Map.put("RSFS1", null2String(punishmentDetailReq.getPrice()));
                /**小计*/
                dt1Map.put("RSFS2", null2String(punishmentDetailReq.getSumPrice()));
                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);
            List<Map<String, String>> detail2List = new ArrayList<>();
            for (int i = 0; i < punishmentChangeReq.getItem2().size(); i++) {
                PunishmentChangeDetail2Req punishmentDetail2Req = punishmentChangeReq.getItem2().get(i);
                log.info("参数信息：" + punishmentDetail2Req.toString());
                Map<String, String> dt2Map = new HashMap<>();
                /**ID*/
                dt2Map.put("IDs", null2String(punishmentDetail2Req.getIDs()));
                /**零件号*/
                dt2Map.put("RSFS4", null2String(punishmentDetail2Req.getPartNumber()));
                /**零件名称*/
                dt2Map.put("RSFS5", null2String(punishmentDetail2Req.getPartName()));
                /**数量*/
                dt2Map.put("RSFS6", null2String(punishmentDetail2Req.getNumber()));
                /**单价*/
                dt2Map.put("RSFS7", null2String(punishmentDetail2Req.getPrice()));
                /**小计*/
                dt2Map.put("RSFS8", null2String(punishmentDetail2Req.getSumPrice()));
                detail2List.add(dt2Map);
            }
            detailLists.add(detail2List);
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
        log.info("------------------------索赔变更单(SRM)----------------------------end");
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
