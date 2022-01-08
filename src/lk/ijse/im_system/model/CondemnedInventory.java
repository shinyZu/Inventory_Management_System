package lk.ijse.im_system.model;

public class CondemnedInventory {

    private String invNo;
    private String description;
    private int totalQtyCondemned;

    public CondemnedInventory() {}

    public CondemnedInventory(String invNo, String description, int totalQtyCondemned) {
        this.setInvNo(invNo);
        this.setDescription(description);
        this.setTotalQtyCondemned(totalQtyCondemned);
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

    public int getTotalQtyCondemned() {
        return totalQtyCondemned;
    }

    public void setTotalQtyCondemned(int totalQtyCondemned) {
        this.totalQtyCondemned = totalQtyCondemned;
    }

    @Override
    public String toString() {
        return "CondemnedInventory{" +
                "invNo='" + invNo + '\'' +
                ", description='" + description + '\'' +
                ", totalQtyCondemned=" + totalQtyCondemned +
                '}';
    }
}
