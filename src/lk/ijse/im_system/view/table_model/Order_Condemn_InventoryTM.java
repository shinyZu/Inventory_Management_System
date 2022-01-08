package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class Order_Condemn_InventoryTM {

    private String  category;
    private String  invNo;
    private String  description;
    private int  modifiedQty;

    public Order_Condemn_InventoryTM() {}

    public Order_Condemn_InventoryTM(String category, String invNo, String description, int modifiedQty) {
        this.setCategory(category);
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setModifiedQty(modifiedQty);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getModifiedQty() {
        return modifiedQty;
    }

    public void setModifiedQty(int modifiedQty) {
        this.modifiedQty = modifiedQty;
    }

    @Override
    public String toString() {
        return "Order_Condemn_InventoryTM{" +
                "category='" + category + '\'' +
                ", invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", modifiedQty=" + modifiedQty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order_Condemn_InventoryTM that = (Order_Condemn_InventoryTM) o;
        return modifiedQty == that.modifiedQty &&
                Objects.equals(category, that.category) &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, invNo, description, modifiedQty);
    }
}
