package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class InventoryInWardTM {
    private String invNo;
    private String description;
    private int beforeQTY;
    private String lastModified;
    private int afterQTY;
    private String location;
    private String category;

    public InventoryInWardTM() {}

    public InventoryInWardTM(String invNo, String description, int beforeQTY, String lastModified, int afterQTY, String location, String category) {
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setBeforeQTY(beforeQTY);
        this.setLastModified(lastModified);
        this.setAfterQTY(afterQTY);
        this.setLocation(location);
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

    public int getBeforeQTY() {
        return beforeQTY;
    }

    public void setBeforeQTY(int beforeQTY) {
        this.beforeQTY = beforeQTY;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getAfterQTY() {
        return afterQTY;
    }

    public void setAfterQTY(int afterQTY) {
        this.afterQTY = afterQTY;
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
        return "InventoryInWardTM{" +
                "invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", beforeQTY=" + beforeQTY +
                ", lastModified='" + lastModified + '\'' +
                ", afterQTY=" + afterQTY +
                ", location='" + location + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryInWardTM that = (InventoryInWardTM) o;
        return beforeQTY == that.beforeQTY &&
                afterQTY == that.afterQTY &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(location, that.location) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invNo, description, beforeQTY, lastModified, afterQTY, location, category);
    }
}
