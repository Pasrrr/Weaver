package esteem.jun.wanxiang.pojo;


public class FiledData {

    private Integer id;

    private Integer field1;

    private Integer field2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getField1() {
        return field1;
    }

    public void setField1(Integer field1) {
        this.field1 = field1;
    }

    public Integer getField2() {
        return field2;
    }

    public void setField2(Integer field2) {
        this.field2 = field2;
    }

    @Override
    public String toString() {
        return "FiledData{" +
                "id=" + id +
                ", field1=" + field1 +
                ", field2=" + field2 +
                '}';
    }
}
