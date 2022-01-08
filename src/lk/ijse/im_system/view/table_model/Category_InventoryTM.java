package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class Category_InventoryTM {
    private String inventoryNo;
    private String description;
    private int qtyOnHand;
    private String location;

    public Category_InventoryTM() {}

    public Category_InventoryTM(String inventoryNo, String description, int qtyOnHand, String location) {
        this.setInventoryNo(inventoryNo);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setLocation(location);
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

    @Override
    public String toString() {
        return "Category_InventoryTM{" +
                "inventoryNo='" + inventoryNo + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category_InventoryTM that = (Category_InventoryTM) o;
        return qtyOnHand == that.qtyOnHand &&
                Objects.equals(inventoryNo, that.inventoryNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryNo, description, qtyOnHand, location);
    }
}
