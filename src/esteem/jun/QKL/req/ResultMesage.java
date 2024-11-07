package esteem.jun.QKL.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/24
 * @Description:
 */
public class ResultMesage {
    /**响应代码*/
    private String returnCode;
    /**响应描述*/
    private String returnDesc;

    @Override
    public String toString() {
        return "ResultMessage{" +
                "returnCode='" + returnCode + '\'' +
                ", returnDesc='" + returnDesc + '\'' +
                '}';
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }
}
