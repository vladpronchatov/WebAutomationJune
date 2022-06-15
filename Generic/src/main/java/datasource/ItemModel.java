package datasource;

public class ItemModel {
    private String itemName;
    private String itemPrice;
    private String itemReview;

    private String itemTag;

    public ItemModel(){}

    public ItemModel(String itemName, String itemPrice, String itemReview) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemReview = itemReview;
    }
    public ItemModel(String itemName, String itemPrice, String itemReview, String itemTag) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemReview = itemReview;
        this.itemTag = itemTag;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemReview() {
        return itemReview;
    }

    public void setItemReview(String itemReview) {
        this.itemReview = itemReview;
    }
    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

}
