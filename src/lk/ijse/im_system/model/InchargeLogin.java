package lk.ijse.im_system.model;

import lk.ijse.im_system.controller.util.User;

import java.util.Objects;

public class InchargeLogin {
    private String userName;
    private String userPassword;
    private User userType;
    private String wardNo;
    private String email;
    private String emailPassword;

    public InchargeLogin() {}

    public InchargeLogin(String userName, String userPassword, User userType, String wardNo, String email, String emailPassword) {
        this.setUserName(userName);
        this.setUserPassword(userPassword);
        this.setUserType(userType);
        this.setWardNo(wardNo);
        this.setEmail(email);
        this.setEmailPassword(emailPassword);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public User getUserType() {
        return userType;
    }

    public void setUserType(User userType) {
        this.userType = userType;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    @Override
    public String toString() {
        return "InchargeLogin{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userType=" + userType +
                ", wardNo='" + wardNo + '\'' +
                ", email='" + email + '\'' +
                ", emailPassword='" + emailPassword + '\'' +
                '}';
    }
}
