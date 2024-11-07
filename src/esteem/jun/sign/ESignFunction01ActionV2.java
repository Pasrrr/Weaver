package esteem.jun.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ess.v20201111.models.*;
import esteem.jun.wanxiang.action.BaseActionInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
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
public class ESignFunction01ActionV2 extends BaseBean implements Action {
    private Log log = LogFactory.getLog(ESignFunction01ActionV2.class.getName());
    public static final Map<String,String> status_flag= new HashMap<String,String>();
    //yDwiLUUckpkcg9vvUECtMwfEgEOo1u7c
    private String OperatorId;
    //AKyDwiuUUckpog784mUERg3wzy272MnWtZ
    private String SecretId;
    // SKJDC6DEniR7uEPBUvjAZVoJXjDNVTTTWu
    private String SecretKey;
    // file.test.ess.tencent.cn
    private String Endpoint1;
    // ess.test.ess.tencent.cn
    private String Endpoint2;

    private String companyName;

    private String sign;

    private String lastname;

    private String mobile;

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
        List<String> Qswjids=new ArrayList();
        List<String> FlowIDs=new ArrayList();
        List<String> createFlowByFilesResults=new ArrayList();

        //用印类型 0-电子章 1-实体章
        String gzlx=baseActionInfo.getMainTableValue("gzlx");
        log.info(gzlx);
        if(gzlx.equals(status_flag.get("bs0"))){
            try {
                // 相关状态
                String ejkzt =baseActionInfo.getMainTableValue("ejkzt");
                //取文件
                String qswjfj  = Util.null2String(baseActionInfo.getMainTableValue("xgzfjwds"));
                if("".equals(qswjfj)){
                    qswjfj = baseActionInfo.getMainTableValue("fj");
                }
                String sql ="";
                //拆分附件
                String[] docIdlist =qswjfj.split(",");
                if(ejkzt.equals(status_flag.get("bs0"))){
                    for (int i=0;i < docIdlist.length;i++) {
                        log.info("############################################上传文件############################################begin");
                        User user = HrmUserVarify.getUser(requestInfo.getRequestManager().getRequest(), null);
                        Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(docIdlist[i], user);
                        log.info(fileInfo.toString());
                        String filetype = Util.null2String(fileInfo.get("filetype"));
                        UploadFilesRequest uploadFilesRequest = new UploadFilesRequest();
                        // 文件对应业务类型 此文件用来发起合同流程，文件类型支持.pdf/.doc/.docx/.jpg/.png/.xls.xlsx/.html，如果非pdf文件需要通过创建文件转换任务转换后才能使用
                        uploadFilesRequest.setBusinessType("DOCUMENT");
                        // 文件类型 pdf
                        uploadFilesRequest.setFileType(filetype);
                        // 上传文件集合
                        List<UploadFile> uploadFiles = new ArrayList();
                        UploadFile uploadFile = new UploadFile();
                        uploadFile.setFileBody(fileInfo.get("content"));
                        uploadFile.setFileName(fileInfo.get("fileName"));
                        uploadFiles.add(uploadFile);
                        UploadFile[] uploadFileArr = new UploadFile[uploadFiles.size()];
                        uploadFileArr = uploadFiles.toArray(uploadFileArr);
                        uploadFilesRequest.setFileInfos(uploadFileArr);
                        // 上传人员信息
                        Caller caller = new Caller();
                        caller.setOperatorId(OperatorId);
                        uploadFilesRequest.setCaller(caller);
                        log.info(UploadFilesResponse.toJsonString(uploadFilesRequest));
                        String result = ESigTreasureUtil.uploadFiles(uploadFilesRequest, SecretId, SecretKey, Endpoint1);
                        JSONObject uploadFilesResponse = JSON.parseObject(result);
                        JSONArray qswjids = uploadFilesResponse.getJSONObject("Response").getJSONArray("FileIds");
                        List<String> qswjidsList = JSONObject.parseArray(qswjids.toJSONString(), String.class);
                        //qswjid-签署文件ID ejkzt-E接口状态 ejkfhjg-E接口返回结果
                        Qswjids.add(qswjidsList.stream().collect(Collectors.joining(",")));
                        //sql = "update " + tableName + " set qswjid=?,ejkzt=?,ejkfhjg=? where requestId ='" + requestId + "'";
                        //recordSet.executeUpdate(sql, new String[]{qswjidsList.stream().collect(Collectors.joining(",")), "2", result});
                        //ejkzt = "2";
                        log.info("############################################上传文件############################################end");


                        log.info("############################################创建签署流程############################################begin");
                        //签署文件流程
                        CreateFlowByFilesRequest createFlowByFilesRequest = createFlowByFilesRequest(tableName, requestId,fileInfo.get("fileName"), Qswjids.get(i));
                        log.info(CreateFlowByFilesResponse.toJsonString(createFlowByFilesRequest));
                        String createFlowByFilesResult = ESigTreasureUtil.CreateFlowByFiles(createFlowByFilesRequest, SecretId, SecretKey, Endpoint2);
                        log.info("createFlowByFilesResult-->" + createFlowByFilesResult);

                        JSONObject createFlowByFilesResponse = JSON.parseObject(createFlowByFilesResult);

                        String flowId = createFlowByFilesResponse.getJSONObject("Response").getString("FlowId");
                        //ejkzt-E接口状态,ejkfhjg-E接口返回结果 FlowID-FlowID
                        FlowIDs.add(flowId);
                        createFlowByFilesResults.add(createFlowByFilesResult);
                        //sql = "update " + tableName + " set ejkzt=?,ejkfhjg=?,flowid=? where requestId ='" + requestId + "'";
                        //recordSet.executeUpdate(sql, new String[]{"3", createFlowByFilesResult, flowId});
                        log.info("############################################创建签署流程############################################end");

                    }
                    sql = "update " + tableName + " set qswjid=?,ejkzt=?,ejkfhjg=?,flowid=? where requestId ='" + requestId + "'";
                    recordSet.executeUpdate(sql, new String[]{Qswjids.toString(), "3",createFlowByFilesResults.toString(), FlowIDs.toString()});
                }

            } catch (TencentCloudSDKException e) {
                log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
                requestInfo.getRequestManager().setMessage("111100");
                requestInfo.getRequestManager().setMessagecontent(e.toString());
                return Action.FAILURE_AND_CONTINUE;
            } catch (Exception e) {
                log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
                requestInfo.getRequestManager().setMessage("111100");
                requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
                return Action.FAILURE_AND_CONTINUE;
            }
            return Action.SUCCESS;
        } else{
            return Action.SUCCESS;
    }

    }


    public CreateFlowByFilesRequest createFlowByFilesRequest(String tableName,String requestId,String filename,String qswjid){
        RecordSet recordSet = new RecordSet();
        String sql="select * from "+tableName+" where requestid ='"+requestId+"'";
        recordSet.execute(sql);
        recordSet.next();
        String sqr = Util.null2String(recordSet.getString("sqr"));
        Map<String,String> hrMap = CommonUtil.getHRMapById(sqr);

        //String qswjid = Util.null2String(recordSet.getString("qswjid"));
        /*内容明细 合同名称*/
        String nrmx = Util.null2String(recordSet.getString("nrmx"));
        /*页数*/
        long ys = recordSet.getInt("ys");
        /*签署类型*/
        String qslx = Util.null2String(recordSet.getString("qslx"));
        /*是否加盖骑缝章*/
        String sfjgqfz = Util.null2String(recordSet.getString("sfjgqfz"));
        /*对方签署人*/
        String ApproverName = Util.null2String(recordSet.getString("ApproverName"));
        /*对方签署人手机号*/
        String ApproverMobile = Util.null2String(recordSet.getString("ApproverMobile"));
        /*对方签署单位*/
        String OrganizationName = Util.null2String(recordSet.getString("OrganizationName"));
        /*申请日期*/
        String sqrq=Util.null2String(recordSet.getString("sqrq")).split(" ")[0];

        CreateFlowByFilesRequest req = new CreateFlowByFilesRequest();
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(OperatorId);

        req.setOperator(userInfo);
        // 合同流程的名称
        req.setFlowName(filename+"-"+hrMap.get("lastname")+"-"+sqrq);
        req.setSignBeanTag(1L);

        List<ApproverInfo> approverInfos=new ArrayList();
        // 单方签署
        ApproverInfo approverInfo1=new ApproverInfo();
        approverInfo1.setApproverType(0L);
        //approverInfo1.setApproverName(hrMap.get("lastname"));
        approverInfo1.setApproverName(lastname);

        //approverInfo1.setApproverMobile(hrMap.get("mobile"));
        approverInfo1.setApproverMobile(mobile);
        // 默认企业名称
        approverInfo1.setOrganizationName(companyName);
        int length=1;
        if("0".equals(sfjgqfz)){
            length=2;
        }
        ComponentLimit[] componentLimits = new ComponentLimit[length];
        ComponentLimit componentLimit=new ComponentLimit();
        componentLimit.setComponentType("SIGN_SEAL");
        //String[] componentValue = {"yDwi3UUckpo8lh0xUuWDfozxA8nxssrZ"};
        String[] componentValue = sign.split(",");
        componentLimit.setComponentValue(componentValue);

        componentLimits[0]=componentLimit;
        // 需要骑缝章
        if("0".equals(sfjgqfz)){
            componentLimit=new ComponentLimit();
            componentLimit.setComponentType("SIGN_PAGING_SEAL");
            componentLimit.setComponentValue(componentValue);
            componentLimits[1]=componentLimit;

        }
        approverInfo1.setAddSignComponentsLimits(componentLimits);
        approverInfos.add(approverInfo1);
        // 双方签署
        if("1".equals(qslx)){
            ApproverInfo approverInfo2=new ApproverInfo();
            /*签署类型 0：企业*/
            approverInfo2.setApproverType(0L);
            /*签署方经办人的姓名*/
            approverInfo2.setApproverName(ApproverName);
            /*签署方经办人手机号码*/
            approverInfo2.setApproverMobile(ApproverMobile);
            /*组织名称*/
            approverInfo2.setOrganizationName(OrganizationName);
            approverInfo2.setAddSignComponentsLimits(componentLimits);
            approverInfos.add(approverInfo2);
        }
        ApproverInfo[] approverArr = new ApproverInfo[approverInfos.size()];
        approverArr = approverInfos.toArray(approverArr);
        req.setApprovers(approverArr);
        //本合同流程需包含的PDF文件资源编号列表，通过UploadFiles接口获取PDF文件资源编号
        req.setFileIds(qswjid.split(","));
        return req;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        OperatorId = operatorId;
    }

    public String getSecretId() {
        return SecretId;
    }

    public void setSecretId(String secretId) {
        SecretId = secretId;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    public String getEndpoint1() {
        return Endpoint1;
    }

    public void setEndpoint1(String endpoint1) {
        Endpoint1 = endpoint1;
    }

    public String getEndpoint2() {
        return Endpoint2;
    }

    public void setEndpoint2(String endpoint2) {
        Endpoint2 = endpoint2;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}