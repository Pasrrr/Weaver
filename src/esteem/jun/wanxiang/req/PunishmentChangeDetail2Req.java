package esteem.jun.wanxiang.req;

/**
 *
 * @Author: Cao
 * @Date: 2024-1-15
 * @Description:索赔单
 */
public class PunishmentChangeDetail2Req {
    /**ID*/
    private String IDs;
    /**零件号*/
    private String PartNumber;
    /**零件名称*/
    private String PartName;
    /**数量*/
    private String Number;
    /**单价*/
    private String Price;
    /**小计*/
    private String SumPrice;

    @Override
    public String toString() {
        return "PunishmentDetail2Req{" +
                "IDs='" + IDs + '\'' +
                ", PartNumber='" + PartNumber + '\'' +
                ", PartName='" + PartName + '\'' +
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

    public String getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(String partNumber) {
        PartNumber = partNumber;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
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
