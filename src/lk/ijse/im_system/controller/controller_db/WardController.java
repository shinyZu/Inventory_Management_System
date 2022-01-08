package lk.ijse.im_system.controller.controller_db;

import javafx.collections.ObservableList;
import lk.ijse.im_system.controller.util.DateTime;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.InchargeHistory;
import lk.ijse.im_system.model.Ward;
import lk.ijse.im_system.view.table_model.Ward_InchargeDetailTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WardController {
    public static List<String> getAllWardNo(List<String> cmbWardNoList) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wardNo FROM Ward;").executeQuery();

        while (rst.next()) {
            cmbWardNoList.add(rst.getString(1));
        }
        return cmbWardNoList;
    }

    public static String getWardTitle(String cmbWardSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT title FROM Ward WHERE wardNo = ?");

        pstm.setObject(1,cmbWardSelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static ObservableList<Ward_InchargeDetailTM> getAllWardDetails(ObservableList<Ward_InchargeDetailTM> wardDetailList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo;").executeQuery();

        while (rst.next()) {
            String email = null;

            if (rst.getString("email") == null) {
                email = "not yet signed in";

            } else {
                email = rst.getString("email");
            }

            wardDetailList.add(new Ward_InchargeDetailTM(
                    rst.getString("wardNo"),
                    rst.getString("title"),
                    rst.getString("extensionNo"),
                    email,
                    rst.getString("inchargeName"),
                    rst.getString("inchargeId")
            ));
        }
        return wardDetailList;
    }

    public static boolean addNewWard(Ward newWard, InchargeHistory history) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO Ward VALUES (?,?,?,?)");

        pstm.setObject(1, newWard.getWardNo());
        pstm.setObject(2, newWard.getTitle());
        pstm.setObject(3, newWard.getExtensionNo());
        pstm.setObject(4, newWard.getInchargeId());

        if (pstm.executeUpdate() > 0) { // if data is sent to Ward table without any issue

            if (InchargeController.addNewHistory(history)) { // if data is sent to InchargeHistory table without any issue
                return true;
            }
        }
        return false;
    }

    public static String currentInchargeId = null;

    public static boolean updateInchargeIdOfWard(String inchargeIdSelected, String wardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT inchargeId FROM Ward WHERE wardNo = ?");
        pstm.setObject(1,wardNo);

        ResultSet rst = pstm.executeQuery();

        currentInchargeId = null;

        if (rst.next()) {
            currentInchargeId = rst.getString("inchargeId");

        } else {
            //
        }

        pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("UPDATE Ward SET inchargeId = ?" +
                        "WHERE wardNo = ?");

        pstm.setObject(1,inchargeIdSelected);
        pstm.setObject(2,wardNo);

        if (pstm.executeUpdate() > 0) {

            if (InchargeController.updateInchargeHistory(wardNo,currentInchargeId, DateTime.date, inchargeIdSelected)) {
                return true;
            }
        }
        return false;
    }

    public static String getEmail(String wardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT email FROM InchargeLogin WHERE wardNo = ? ");

        pstm.setObject(1,wardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString("email");
        }
        return null;
    }

    public static String getEmailPwd(String wardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT emailPassword FROM InchargeLogin WHERE wardNo = ? ");

        pstm.setObject(1,wardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString("emailPassword");
        }
        return null;
    }

    public static List<String> getAllTitles(List<String> cmbWardTitleList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT title, COUNT(wardNo) FROM Ward GROUP BY title").executeQuery();

        while (rst.next()) {
            cmbWardTitleList.add(rst.getString("title"));
        }
        return cmbWardTitleList;
    }

    public static List<Ward_InchargeDetailTM> searchWard(String text) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "WHERE w.title LIKE '%"+text+"%'" +
                        "OR wi.inchargeName LIKE '%"+text+"%'");

        List<Ward_InchargeDetailTM> searchList = new ArrayList<>();

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {

            String email = null;
            if (rst.getString("email") == null) {
                email = "not yet signed in";
            } else {
                email = rst.getString("email");
            }

            searchList.add(new Ward_InchargeDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    email,
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return searchList;
    }

    public static ObservableList<Ward_InchargeDetailTM> searchWardFromFilterList( String text, ObservableList<Ward_InchargeDetailTM> filteredList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "WHERE w.title LIKE '%"+text+"%'\n" +
                        "OR wi.inchargeName LIKE '%"+text+"%'");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {

            String email = null;
            if (rst.getString("email") == null) {
                email = "not yet signed in";
            } else {
                email = rst.getString("email");
            }

            filteredList.add(new Ward_InchargeDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    email,
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return filteredList;
    }

    //--------------------------------------------------------------Filter Ward Details-----------------------------------------

    public static ObservableList<Ward_InchargeDetailTM> getFilteredListByTitle(ObservableList<Ward_InchargeDetailTM> wardDetailList, String cmbTitleSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "WHERE title = ? ");

        pstm.setObject(1,cmbTitleSelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardDetailList.add(new Ward_InchargeDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardDetailList;
    }

    public static ObservableList<Ward_InchargeDetailTM> getFilteredListByWardNo(ObservableList<Ward_InchargeDetailTM> wardDetailList, String cmbWardNoSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "WHERE w.wardNo = ? ");

        pstm.setObject(1,cmbWardNoSelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardDetailList.add(new Ward_InchargeDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardDetailList;
    }

    public static ObservableList<Ward_InchargeDetailTM> getFilteredListByIncharge(ObservableList<Ward_InchargeDetailTM> wardDetailList, String cmbInchargeNameSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId\n" +
                        "FROM Ward w INNER JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "WHERE wi.inchargeName = ? ");

        pstm.setObject(1,cmbInchargeNameSelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardDetailList.add(new Ward_InchargeDetailTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardDetailList;
    }

    public static String getWardCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(wardNo) FROM Ward").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------------------------------------

    public static String getExtensionNo(String wardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT extensionNo FROM Ward WHERE wardNo = ?");

        pstm.setObject(1,wardNo);

        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getNextWardNo() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wardNo FROM Ward ORDER BY wardNo DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "WD-00" + tempId;
            }else if (tempId <= 99){
                return "WD-0" + tempId;
            }else {
                return "WD-" + tempId;
            }
        } else {
            return "WD-001";
        }
    }
}
