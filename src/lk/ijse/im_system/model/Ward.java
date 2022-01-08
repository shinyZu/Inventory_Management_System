package lk.ijse.im_system.model;

public class Ward {

    private String wardNo;
    private String title;
    private String extensionNo;
    private String inchargeId;

    public Ward() {}

    public Ward(String wardNo, String title, String extensionNo, String inchargeId) {
        this.setWardNo(wardNo);
        this.setTitle(title);
        this.setExtensionNo(extensionNo);
        this.setInchargeId(inchargeId);
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtensionNo() {
        return extensionNo;
    }

    public void setExtensionNo(String extensionNo) {
        this.extensionNo = extensionNo;
    }

    public String getInchargeId() {
        return inchargeId;
    }

    public void setInchargeId(String inchargeId) {
        this.inchargeId = inchargeId;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "wardNo='" + wardNo + '\'' +
                ", title='" + title + '\'' +
                ", extensionNo='" + extensionNo + '\'' +
                ", inchargeId='" + inchargeId + '\'' +
                '}';
    }
}
