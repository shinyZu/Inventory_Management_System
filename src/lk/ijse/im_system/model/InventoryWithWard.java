package lk.ijse.im_system.model;

import java.util.Objects;

public class InventoryWithWard {
    private String wardNo;
    private String invNo;
    private int beforeQty;
    private int afterQty;
    private String lastModified;
    private String location;
    private int notifyLevel;

    public InventoryWithWard() {}

    public InventoryWithWard(String wardNo, String invNo, int beforeQty, int afterQty, String lastModified, String location, int notifyLevel) {
        this.setWardNo(wardNo);
        this.setInvNo(invNo);
        this.setBeforeQty(beforeQty);
        this.setAfterQty(afterQty);
        this.setLastModified(lastModified);
        this.setLocation(location);
        this.setNotifyLevel(notifyLevel);
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

    public int getBeforeQty() {
        return beforeQty;
    }

    public void setBeforeQty(int beforeQty) {
        this.beforeQty = beforeQty;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    @Override
    public String toString() {
        return "InventoryWithWard{" +
                "wardNo='" + wardNo + '\'' +
                ", invNo='" + invNo + '\'' +
                ", beforeQty=" + beforeQty +
                ", afterQty=" + afterQty +
                ", lastModified='" + lastModified + '\'' +
                ", location='" + location + '\'' +
                ", notifyLevel=" + notifyLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryWithWard that = (InventoryWithWard) o;
        return beforeQty == that.beforeQty &&
                afterQty == that.afterQty &&
                notifyLevel == that.notifyLevel &&
                Objects.equals(wardNo, that.wardNo) &&
                Objects.equals(invNo, that.invNo) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wardNo, invNo, beforeQty, afterQty, lastModified, location, notifyLevel);
    }
}
