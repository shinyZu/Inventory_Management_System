package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class InventoryTM {

    private String inventoryNo;
    private String description;
    private int qtyOnHand;
    private String location;
    private String category;

    public InventoryTM() {}

    public InventoryTM(String inventoryNo, String description, int qtyOnHand, String location, String category) {
        this.setInventoryNo(inventoryNo);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setLocation(location);
        this.setCategory(category);
    }

    public InventoryTM(String s) {
        this.inventoryNo = s;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "InventoryTM{" +
                "inventoryNo='" + inventoryNo + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", location='" + location + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryTM that = (InventoryTM) o;
        return qtyOnHand == that.qtyOnHand &&
                Objects.equals(inventoryNo, that.inventoryNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(location, that.location) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryNo, description, qtyOnHand, location, category);
    }
}
