package lk.ijse.im_system.model;

import lk.ijse.im_system.controller.util.User;

public class StorekeeperLogin {

    private String userName;
    private String userPassword;
    private User userType;

    public StorekeeperLogin() {}

    public StorekeeperLogin(String userName, String userPassword, User userType) {
        this.setUserName(userName);
        this.setUserPassword(userPassword);
        this.setUserType(userType);
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

    @Override
    public String toString() {
        return "StorekeeperLogin{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userType=" + userType +
                '}';
    }
}
