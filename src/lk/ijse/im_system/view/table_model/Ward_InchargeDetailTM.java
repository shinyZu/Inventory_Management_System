package lk.ijse.im_system.view.table_model;

import java.util.Objects;

public class Ward_InchargeDetailTM {
    private String wardNo;
    private String title;
    private String extensionNo;
    private String email;
    private String inchargeName;
    private String inchargeId;

    public Ward_InchargeDetailTM() {}

    public Ward_InchargeDetailTM(String wardNo, String title, String extensionNo, String email, String inchargeName, String inchargeId) {
        this.setWardNo(wardNo);
        this.setTitle(title);
        this.setExtensionNo(extensionNo);
        this.setEmail(email);
        this.setInchargeName(inchargeName);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInchargeName() {
        return inchargeName;
    }

    public void setInchargeName(String inchargeName) {
        this.inchargeName = inchargeName;
    }

    public String getInchargeId() {
        return inchargeId;
    }

    public void setInchargeId(String inchargeId) {
        this.inchargeId = inchargeId;
    }

    @Override
    public String toString() {
        return "Ward_InchargeDetailTM{" +
                "wardNo='" + wardNo + '\'' +
                ", title='" + title + '\'' +
                ", extensionNo='" + extensionNo + '\'' +
                ", email='" + email + '\'' +
                ", inchargeName='" + inchargeName + '\'' +
                ", inchargeId='" + inchargeId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ward_InchargeDetailTM that = (Ward_InchargeDetailTM) o;
        return Objects.equals(wardNo, that.wardNo) &&
                Objects.equals(title, that.title) &&
                Objects.equals(extensionNo, that.extensionNo) &&
                Objects.equals(email, that.email) &&
                Objects.equals(inchargeName, that.inchargeName) &&
                Objects.equals(inchargeId, that.inchargeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wardNo, title, extensionNo, email, inchargeName, inchargeId);
    }
}
