package lk.ijse.im_system.controller.util;


public enum User {
    INCHARGE,STOREKEEPER;

    private User userType;

    public String getUserType() {
        return this.userType.name();
    }

    public void setUserType(String userType) {
        this.userType = User.valueOf(userType);
    }

}
