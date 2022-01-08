package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class Inventory_QtyTM {
    private String invNo;
    private String description;
    private int beforeQty;
    private int modifiedQty;
    private int afterQty;
    private String lastModified;
    private String category;

    public Inventory_QtyTM() {}

    public Inventory_QtyTM(String invNo, String description, int beforeQty, int modifiedQty, int afterQty, String lastModified, String category) {
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setBeforeQty(beforeQty);
        this.setModifiedQty(modifiedQty);
        this.setAfterQty(afterQty);
        this.setLastModified(lastModified);
        this.setCategory(category);
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

    public int getBeforeQty() {
        return beforeQty;
    }

    public void setBeforeQty(int beforeQty) {
        this.beforeQty = beforeQty;
    }

    public int getModifiedQty() {
        return modifiedQty;
    }

    public void setModifiedQty(int modifiedQty) {
        this.modifiedQty = modifiedQty;
    }

    public int getAfterQty() {
        return afterQty;
    }

    public void setAfterQty(int afterQty) {
        this.afterQty = afterQty;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Inventory_QtyTM{" +
                "invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", beforeQty=" + beforeQty +
                ", modifiedQty=" + modifiedQty +
                ", afterQty=" + afterQty +
                ", lastModified='" + lastModified + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory_QtyTM that = (Inventory_QtyTM) o;
        return beforeQty == that.beforeQty &&
                modifiedQty == that.modifiedQty &&
                afterQty == that.afterQty &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invNo, description, beforeQty, modifiedQty, afterQty, lastModified, category);
    }
}
