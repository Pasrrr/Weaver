package esteem.jun.wanxiang.req;

/**
 *
 * @Author: Cao
 * @Date: 2024-1-15
 * @Description:索赔单
 */
public class PunishmentDetailReq {
    /**ID*/
    private String IDs;
    /**项目*/
    private String Project;
    /**数量（或工时、金额）*/
    private String Number;
    /**单价*/
    private String Price;
    /**小计*/
    private String SumPrice;

    @Override
    public String toString() {
        return "PunishmentDetailReq{" +
                "IDs='" + IDs + '\'' +
                ", Project='" + Project + '\'' +
                ", Number='" + Number + '\'' +
                ", Price='" + Price + '\'' +
                ", SumPrice='" + SumPrice + '\'' +
                '}';
    }

    public String getIDs() {
        return IDs;
    }

    public void setIDs(String IDs) {
        this.IDs = IDs;
    }

    public String getProject() {
        return Project;
    }

    public void setProject(String project) {
        Project = project;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(String sumPrice) {
        SumPrice = sumPrice;
    }

}
