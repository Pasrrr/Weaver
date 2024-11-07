package esteem.jun.wanxiang.action.bean;

/**
 * @className: Result
 * @author: jun
 * @date: 2021-12-13 19:10
 * @Depiction:
 **/
public class ResultMesage {
    private String message;
    private String flag;
    private String djbh;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ResultMesage{" +
                "message='" + message + '\'' +
                ", flag='" + flag + '\'' +
                ", djbh='" + djbh + '\'' +
                '}';
    }

    public String getDjbh() {
        return djbh;
    }

    public void setDjbh(String djbh) {
        this.djbh = djbh;
    }
}
