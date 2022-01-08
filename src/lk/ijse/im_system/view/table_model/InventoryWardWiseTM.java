package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class InventoryWardWiseTM {
    private String wardNo;
    private String invNo;
    private String description;
    private int qtyOnHand;
    private String category;
    private String lastModified;

    public InventoryWardWiseTM() {}

    public InventoryWardWiseTM(String wardNo, String invNo, String description, int qtyOnHand, String category, String lastModified) {
        this.setWardNo(wardNo);
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setCategory(category);
        this.setLastModified(lastModified);
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
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

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "InventoryWardWiseTM{" +
                "wardNo='" + wardNo + '\'' +
                ", invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", category='" + category + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryWardWiseTM that = (InventoryWardWiseTM) o;
        return qtyOnHand == that.qtyOnHand &&
                Objects.equals(wardNo, that.wardNo) &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(lastModified, that.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wardNo, invNo, description, qtyOnHand, category, lastModified);
    }
}
