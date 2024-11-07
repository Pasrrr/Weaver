package esteem.jun.wanxiang.pojo;

import java.util.List;

public class RespBeanPage {
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    private long size;
    private List<?> data;


}
