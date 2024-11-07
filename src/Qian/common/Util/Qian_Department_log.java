package Qian.common.Util;

public class Qian_Department_log {
    /**部门ID*/
    private String Departmentid;
    /**部门名称*/
    private String Departmentname;
    /**上级部门*/
    private String ParentDeptId;
    /**电子签部门ID*/
    private String QianDeptId;

    public Qian_Department_log(String departmentid, String departmentname, String parentDeptId, String qianDeptId) {
        this.Departmentid = departmentid;
        this.Departmentname = departmentname;
        this.ParentDeptId = parentDeptId;
        this.QianDeptId = qianDeptId;
    }

    public String getDepartmentid() {
        return Departmentid;
    }

    public void setDepartmentid(String departmentid) {
        Departmentid = departmentid;
    }

    public String getDepartmentname() {
        return Departmentname;
    }

    public void setDepartmentname(String departmentname) {
        Departmentname = departmentname;
    }

    public String getParentDeptId() {
        return ParentDeptId;
    }

    public void setParentDeptId(String parentDeptId) {
        ParentDeptId = parentDeptId;
    }

    public String getQianDeptId() {
        return QianDeptId;
    }

    public void setQianDeptId(String qianDeptId) {
        QianDeptId = qianDeptId;
    }
}
