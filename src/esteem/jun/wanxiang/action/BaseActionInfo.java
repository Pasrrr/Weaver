package esteem.jun.wanxiang.action;

import com.google.common.base.Joiner;
import esteem.jun.common.util.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-12-02 13:15
 * @Description:
 */
public class BaseActionInfo {
    private String requestId;
    private String formId = "-1";
    private String billId = "-1";
    private final String workflowId;
    private String ZBS;
    private String mainTableName = "";
    private final RequestManager requestManager;
    private final Map<String, String> mainTableMap = new HashMap();
    private final Set<String> errSet = new HashSet();

    public BaseActionInfo(RequestInfo request) {
        this.requestManager = request.getRequestManager();
        this.requestId = request.getRequestid();
        this.workflowId = request.getWorkflowid();
        this.mainTableName = this.requestManager.getBillTableName();
        this.billId = Util.null2String(this.requestManager.getBillid());
        this.formId = Util.null2String(this.requestManager.getFormid());
        this.mainTableMap.putAll(CommonUtil.getMainDataByRequestId(this.requestId, this.mainTableName));
    }

    public String getMainTableValue(String fieldName) {
        return Util.null2String(this.mainTableMap.get(Util.null2String(fieldName).toLowerCase()));
    }

    public String getDetailTableValue(Map<String, String> rowData, String fieldName) {
        return Util.null2String((String)rowData.get(Util.null2String(fieldName).toLowerCase()));
    }

    public List<Map<String, String>> getDetailTableData(int dt) {
        return CommonUtil.getDetailDataByMainId(Util.getIntValue(this.billId), this.mainTableName + "_dt" + dt);
    }

    public void addErrMessage(String message) {
        this.errSet.add(message);
    }

    public String handleResultAndGetFlag() {
        if (CollectionUtils.isEmpty(this.errSet)) {
            return "1";
        } else {
            String errMessage = Joiner.on("<br />").skipNulls().join(this.errSet.iterator()).trim();
            errMessage = StringUtils.isBlank(errMessage) ? "执行流程流转错误，请联系管理员！" : errMessage;
            this.requestManager.setMessagecontent(errMessage);
            return "0";
        }
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getZBS() {return this.ZBS;}

    public String getFormId() {
        return this.formId;
    }

    public String getBillId() {
        return this.billId;
    }

    public String getWorkflowId() {
        return this.workflowId;
    }

    public String getMainTableName() {
        return this.mainTableName;
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }

    public Set<String> getErrSet() {
        return this.errSet;
    }


}