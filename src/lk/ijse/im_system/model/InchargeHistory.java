package lk.ijse.im_system.model;

public class InchargeHistory {

    private String wardNo;
    private String inchargeId;
    private String dateFrom;
    private String dateTo;

    public InchargeHistory() {}

    public InchargeHistory(String wardNo, String inchargeId, String dateFrom, String dateTo) {
        this.setWardNo(wardNo);
        this.setInchargeId(inchargeId);
        this.setDateFrom(dateFrom);
        this.setDateTo(dateTo);
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getInchargeId() {
        return inchargeId;
    }

    public void setInchargeId(String inchargeId) {
        this.inchargeId = inchargeId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "InchargeHistory{" +
                "wardNo='" + wardNo + '\'' +
                ", inchargeId='" + inchargeId + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                '}';
    }
}
