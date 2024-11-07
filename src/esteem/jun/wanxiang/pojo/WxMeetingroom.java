package esteem.jun.wanxiang.pojo;

public class WxMeetingroom {
    private Integer id;
    private String name;
    private String roomdesc;

    @Override
    public String toString() {
        return "WxMeetingroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomdesc='" + roomdesc + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomdesc() {
        return roomdesc;
    }

    public void setRoomdesc(String roomdesc) {
        this.roomdesc = roomdesc;
    }
}
