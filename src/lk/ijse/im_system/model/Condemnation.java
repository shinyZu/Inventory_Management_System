package lk.ijse.im_system.model;

public class Condemnation {

    private String condemnId;
    private String wardNo;
    private String condemnDate;

    public Condemnation() {}

    public Condemnation(String condemnId, String wardNo, String condemnDate) {
        this.setCondemnId(condemnId);
        this.setWardNo(wardNo);
        this.setCondemnDate(condemnDate);
    }

    public String getCondemnId() {
        return condemnId;
    }

    public void setCondemnId(String condemnId) {
        this.condemnId = condemnId;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getCondemnDate() {
        return condemnDate;
    }

    public void setCondemnDate(String condemnDate) {
        this.condemnDate = condemnDate;
    }

    @Override
    public String toString() {
        return "Condemnation{" +
                "condemnId='" + condemnId + '\'' +
                ", wardNo='" + wardNo + '\'' +
                ", condemnDate='" + condemnDate + '\'' +
                '}';
    }
}
