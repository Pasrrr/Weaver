package LR.wanxiang.Req;

public class DeclassifyRequisitionReq {
    /**申请人邮箱*/
    private String email;
    /**文档ID*/
    private String Documentid;
    /**文档名称*/
    private String Documentname;
    /**文档类型*/
    private String Documenttype;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentid() {
        return Documentid;
    }

    public void setDocumentid(String documentid) {
        Documentid = documentid;
    }

    public String getDocumentname() {
        return Documentname;
    }

    public void setDocumentname(String documentname) {
        Documentname = documentname;
    }

    public String getDocumenttype() {
        return Documenttype;
    }

    public void setDocumenttype(String documenttype) {
        Documenttype = documenttype;
    }

    @Override
    public String toString() {
        return "DeclassifyRequisitionReq{" +
                "email='" + email + '\'' +
                ", Documentid='" + Documentid + '\'' +
                ", Documentname='" + Documentname + '\'' +
                ", Documenttype='" + Documenttype + '\'' +
                '}';
    }
}
