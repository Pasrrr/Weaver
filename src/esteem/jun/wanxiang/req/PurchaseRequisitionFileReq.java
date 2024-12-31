package esteem.jun.wanxiang.req;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/12/16
 * @Description:
 */
public class PurchaseRequisitionFileReq {
    private String FILEDATA;

    private String FILENAME;

    @Override
    public String toString() {
        return "PurchaseRequisitionFileReq{" +
                "FILEDATA='" + FILEDATA + '\'' +
                ", FILENAME='" + FILENAME + '\'' +
                '}';
    }

    public String getFILEDATA() {
        return FILEDATA;
    }

    public void setFILEDATA(String FILEDATA) {
        this.FILEDATA = FILEDATA;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }
}
