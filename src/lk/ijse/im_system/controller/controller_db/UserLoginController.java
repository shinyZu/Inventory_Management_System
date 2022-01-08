package lk.ijse.im_system.controller.controller_db;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lk.ijse.im_system.controller.util.Notification;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Incharge;
import lk.ijse.im_system.model.InchargeLogin;
import lk.ijse.im_system.model.StorekeeperLogin;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.bouncycastle.jcajce.provider.digest.SHA1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginController {

    public static String getWardNo(String username) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wardNo FROM InchargeLogin WHERE userName = ?");

        pstm.setObject(1,username);

        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getInchargeMail(String loggedWardNo) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT email FROM InchargeLogin WHERE wardNo = ?");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getInchargePassword(String loggedWardNo) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT userPassword FROM InchargeLogin WHERE wardNo = ?");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static boolean verifyMailPassword(String senderMail, String emailPassword) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT emailPassword FROM InchargeLogin WHERE email = ?");
        
        pstm.setObject(1,senderMail);
        ResultSet rst = pstm.executeQuery();
        
        if (rst.next()) {
            return rst.getString(1).equals(emailPassword);
        }
        return false;
    }

    public boolean verifyUser(String username, String tPassword, String fPassword) throws SQLException, ClassNotFoundException {

        String inchargeQuery = "SELECT * FROM InchargeLogin WHERE userName = ?";
        String storekeeperQuery = "SELECT * FROM StorekeeperLogin WHERE userName = ?";

        if (username.equals("") /*|| fPassword.equals("") ||tPassword.equals("")*/) {
            return false;

        } else if (fPassword.equals("")) {
            if (tPassword.equals("")) {
                return false;
            } else {
                //
            }
        }

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(inchargeQuery);
        pstm.setObject(1,username);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            if (rst.getObject("userType").equals("INCHARGE")) {
                if (checkCredentials(rst, username, fPassword, tPassword)) {
                    return true;
                }
            }
        }

        pstm = DbConnection.getInstance().getConnection().prepareStatement(storekeeperQuery);
        pstm.setObject(1,username);
        rst = pstm.executeQuery();

        if (rst.next()) {
            if (rst.getObject("userType").equals("STOREKEEPER")) {
                if (checkCredentials(rst, username, fPassword, tPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkCredentials(ResultSet rst, String username, String fPassword, String tPassword) throws SQLException, ClassNotFoundException {

        String checkIncharge = "SELECT userPassword FROM InchargeLogin WHERE userName = ? AND userPassword = SHA1(?)";
        String checkStorekeeper = "SELECT userPassword FROM StorekeeperLogin WHERE userName = ? AND userPassword = SHA1(?)";

        PreparedStatement pstm = null;

        if (rst.getString("userType").equals("INCHARGE")) {

            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement(checkIncharge);

        } else {
            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement(checkStorekeeper);

        }
        pstm.setObject(1, username);
        pstm.setObject(2, fPassword);

        ResultSet rst2 = pstm.executeQuery();

        if (rst2.next()) {
            return true;
        }
        return false;
    }

    public static boolean verifyIncharge(String username, String tPassword, String fPassword) throws SQLException, ClassNotFoundException {

        String inchargeQuery = "SELECT * FROM InchargeLogin WHERE userName = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(inchargeQuery);
        pstm.setObject(1,username);
        ResultSet rst = pstm.executeQuery();

        if (username.equals("") /*|| fPassword.equals("") ||tPassword.equals("")*/) {
            return false;

        } else if (fPassword.equals("")) {
            if (tPassword.equals("")) {
                return false;
            } else {
                //
            }
        }

        if (rst.next()) {
            if (rst.getObject("userType").equals("INCHARGE")) {
                if (checkCredentials(rst, username, fPassword, tPassword)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean verifyStorekeeper(String username, String tPassword, String fPassword) throws SQLException, ClassNotFoundException {

        String storekeeperQuery = "SELECT * FROM StorekeeperLogin WHERE userName = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(storekeeperQuery);
        pstm.setObject(1,username);

        ResultSet rst = pstm.executeQuery();

        if (username.equals("") /*|| fPassword.equals("") ||tPassword.equals("")*/) {
            return false;

        } else if (fPassword.equals("")) {
            if (tPassword.equals("")) {
                return false;
            } else {
                //
            }
        }

        if (rst.next()) {
            if (rst.getObject("userType").equals("STOREKEEPER")) {
                if (checkCredentials(rst, username, fPassword, tPassword)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInchargeUsernameAlreadyTaken(String username) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM InchargeLogin WHERE userName = ?");

        pstm.setObject(1,username);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static boolean isPwdAlreadyTaken(String userPassword) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM InchargeLogin WHERE userPassword = SHA1(?)");

        pstm.setObject(1,userPassword);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static boolean isStorekeeperUsernameAlreadyTaken(String username) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM StorekeeperLogin WHERE userName = ?");

        pstm.setObject(1,username);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static boolean isSKPwdAlreadyTaken(String userPassword) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM StorekeeperLogin WHERE userPassword = SHA1(?)");

        pstm.setObject(1, userPassword);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static boolean isStorekeeperExists(String username) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM StorekeeperLogin WHERE userName = ?");

        pstm.setObject(1,username);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static boolean signUpAsNewIncharge(InchargeLogin newInchargeSignUp, String inchargeIdSelected, StackPane rootPane, AnchorPane anchorPane) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM InchargeLogin WHERE wardNo = ?");

        pstm.setObject(1,newInchargeSignUp.getWardNo());
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if already there is an Incharge logged in for the WardNo selected --> replace Incharge(handover ward)

            if(isInchargeAssignedToWard(newInchargeSignUp,inchargeIdSelected)) { //if selected Incharge is already assigned to a Ward
                Notification.showErrorDialogBox(rootPane,anchorPane,"lk/ijse/im_system/view/assets/images/alert_glass.png","Incharge Already Assigned to a Ward.\nSelect another Incharge");

            } else { // if selected Incharge is not assigned to a ward --> can replace with the Incharge of the selected Ward

                if (rst.getString("email").equals(newInchargeSignUp.getEmail()) && rst.getString("emailPassword").equals(newInchargeSignUp.getEmailPassword())) {
                    return InchargeController.replaceIncharge(newInchargeSignUp,inchargeIdSelected);

                } else { // if new email and password doesn't match with the old
                    Notification.showErrorDialogBox(rootPane,anchorPane,"lk/ijse/im_system/view/assets/images/alert_glass.png","Invalid Email or Password. Try again...");
                }
            }

        } else { // if there is no any login details for the selected WardNo -->  new InchargeLogin for new Ward

            if (isSelectedInchargeIdMatchSelectedWardNo(newInchargeSignUp,inchargeIdSelected)) {

                pstm = DbConnection.getInstance().getConnection()
                        .prepareStatement("INSERT INTO InchargeLogin VALUES (?,SHA1(?),?,?,?,?)");

                pstm.setObject(1,newInchargeSignUp.getUserName());
                pstm.setObject(2,newInchargeSignUp.getUserPassword());
                pstm.setObject(3,newInchargeSignUp.getUserType().name());
                pstm.setObject(4,newInchargeSignUp.getWardNo());
                pstm.setObject(5,newInchargeSignUp.getEmail());
                pstm.setObject(6,newInchargeSignUp.getEmailPassword());

                return pstm.executeUpdate() > 0;

            } else {
                //new Alert(Alert.AlertType.WARNING,"Invalid Incharge ID...",ButtonType.OK).show();
                Notification.showErrorDialogBox(rootPane,anchorPane,"lk/ijse/im_system/view/assets/images/alert_glass.png","Invalid Incharge ID...");
            }
        }
        return false;
    }


    private static boolean isSelectedInchargeIdMatchSelectedWardNo(InchargeLogin newInchargeSignUp, String inchargeIdSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wardNo FROM Ward WHERE inchargeId = ?");

        pstm.setObject(1,inchargeIdSelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return true; // if selected incharge is assigned to selected ward
        }

        return false;
    }

    private static boolean isInchargeAssignedToWard(InchargeLogin newInchargeSignUp, String inchargeIdSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wardNo FROM Ward WHERE inchargeId = ? ");

        pstm.setObject(1,inchargeIdSelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if selected Incharge is already assigned to a Ward when adding a New Ward

        }else { // if selected Incharge is not assigned to Ward
            return false;
        }
        return true;
    }

    public static boolean signUpAsNewStorekeeper(StorekeeperLogin newStorekeeperSignUp) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection().
                prepareStatement("INSERT INTO StorekeeperLogin VALUES (?,SHA1(?),?)");

        pstm.setObject(1,newStorekeeperSignUp.getUserName());
        pstm.setObject(2,newStorekeeperSignUp.getUserPassword());
        pstm.setObject(3,newStorekeeperSignUp.getUserType().name());

        return pstm.executeUpdate() > 0;
    }

}
