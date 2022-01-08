package lk.ijse.im_system.model;

public class Inventory {
    private String inventoryNo;
    private String description;
    private int qtyOnHand;
    private String categoryId;
    private String locationId;
    private int notifyLevel;

    public Inventory() {}

    public Inventory(String inventoryNo, String description, int qtyOnHand, String categoryId, String locationId, int notifyLevel) {
        this.setInventoryNo(inventoryNo);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setCategoryId(categoryId);
        this.setLocationId(locationId);
        this.setNotifyLevel(notifyLevel);
    }

    public String getInventoryNo() {
        return inventoryNo;
    }

    public void setInventoryNo(String inventoryNo) {
        this.inventoryNo = inventoryNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryNo='" + inventoryNo + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", categoryId='" + categoryId + '\'' +
                ", locationId='" + locationId + '\'' +
                ", notifyLevel=" + notifyLevel +
                '}';
    }
}
