package lk.ijse.im_system.model;

public class CondemnDetail {

    private String condemnId;
    private String invNo;
    private int condemnQty;

    public CondemnDetail() {}

    public CondemnDetail(String condemnId, String invNo, int condemnQty) {
        this.setCondemnId(condemnId);
        this.setInvNo(invNo);
        this.setCondemnQty(condemnQty);
    }

    public String getCondemnId() {
        return condemnId;
    }

    public void setCondemnId(String condemnId) {
        this.condemnId = condemnId;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public int getCondemnQty() {
        return condemnQty;
    }

    public void setCondemnQty(int condemnQty) {
        this.condemnQty = condemnQty;
    }

    @Override
    public String toString() {
        return "CondemnDetail{" +
                "condemnId='" + condemnId + '\'' +
                ", invNo='" + invNo + '\'' +
                ", condemnQty=" + condemnQty +
                '}';
    }
}
