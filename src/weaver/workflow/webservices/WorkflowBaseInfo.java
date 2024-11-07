
package weaver.workflow.webservices;

import java.io.Serializable;

public class WorkflowBaseInfo implements Serializable {
    private static final long serialVersionUID = 9179821848679465088L;
    private String workflowId;
    private String workflowName;
    private String workflowTypeId;
    private String workflowTypeName;

    public WorkflowBaseInfo() {
    }

    public String getWorkflowId() {
        return this.workflowId;
    }

    public void setWorkflowId(String var1) {
        this.workflowId = var1;
    }

    public String getWorkflowName() {
        return this.workflowName;
    }

    public void setWorkflowName(String var1) {
        this.workflowName = var1;
    }

    public String getWorkflowTypeId() {
        return this.workflowTypeId;
    }

    public void setWorkflowTypeId(String var1) {
        this.workflowTypeId = var1;
    }

    public String getWorkflowTypeName() {
        return this.workflowTypeName;
    }

    public void setWorkflowTypeName(String var1) {
        this.workflowTypeName = var1;
    }

    @Override
    public String toString() {
        return "WorkflowBaseInfo{" +
                "workflowId='" + workflowId + '\'' +
                ", workflowName='" + workflowName + '\'' +
                ", workflowTypeId='" + workflowTypeId + '\'' +
                ", workflowTypeName='" + workflowTypeName + '\'' +
                '}';
    }
}