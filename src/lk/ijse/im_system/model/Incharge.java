package lk.ijse.im_system.model;

public class Incharge {
    private String inchargeId;
    private String inchargeName;

    public Incharge() {}

    public Incharge(String inchargeId, String inchargeName) {
        this.setInchargeId(inchargeId);
        this.setInchargeName(inchargeName);
    }

    public String getInchargeId() {
        return inchargeId;
    }

    public void setInchargeId(String inchargeId) {
        this.inchargeId = inchargeId;
    }

    public String getInchargeName() {
        return inchargeName;
    }

    public void setInchargeName(String inchargeName) {
        this.inchargeName = inchargeName;
    }

    @Override
    public String toString() {
        return "Incharge{" +
                "inchargeId='" + inchargeId + '\'' +
                ", inchargeName='" + inchargeName + '\'' +
                '}';
    }
}
