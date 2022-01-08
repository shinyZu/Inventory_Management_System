package lk.ijse.im_system.controller.controller_db;

import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Incharge;
import lk.ijse.im_system.model.InchargeHistory;
import lk.ijse.im_system.model.InchargeLogin;
import lk.ijse.im_system.model.Ward;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InchargeController {

    public static List<String> getNewInchargeId(List<String> cmbInchargeIdList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, wi.inchargeId, inc.wardNo\n" +
                        "FROM Ward w LEFT JOIN InchargeLogin inc\n" +
                        "ON w.wardNo = inc.wardNo\n" +
                        "RIGHT JOIN WardIncharge wi\n" +
                        "ON w.inchargeId = wi.inchargeId\n" +
                        "WHERE inc.wardNo IS NULL;").executeQuery();

        while (rst.next()) {
            cmbInchargeIdList.add(rst.getString(2));
        }
        return cmbInchargeIdList;
    }

    public static String getNextID() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT inchargeId FROM WardIncharge ORDER BY inchargeId DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "ING-00" + tempId;
            }else if (tempId <= 99){
                return "ING-0" + tempId;
            }else {
                return "ING-" + tempId;
            }
        } else {
            return "ING-001";
        }
    }

    public static boolean addNewWardWithIncharge(Ward newWard, Incharge newIncharge, InchargeHistory history) throws SQLException {

        Connection conn = null;

        try {
            conn = DbConnection.getInstance().getConnection();

            conn.setAutoCommit(false);

            PreparedStatement pstm = conn.prepareStatement("INSERT INTO WardIncharge VALUES (?,?)");

            pstm.setObject(1,newIncharge.getInchargeId());
            pstm.setObject(2,newIncharge.getInchargeName());

            if (pstm.executeUpdate() > 0) { // if the data is sent to WardIncharge table, but doesnt save permanently bcz autoCommit is set to false

                if (WardController.addNewWard(newWard,history)) { // if data is sent to Ward table without any issue
                    conn.commit();
                    return true;

                } else { // if some issue occurred while updating Ward table
                    conn.rollback();
                    return false;
                }

            } else { // if some issue occurred while updating WardIncharge table
                conn.rollback();
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
        return false;


    }

    public static boolean addNewHistory(InchargeHistory history) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO InchargeHistory VALUES (?,?,?,?)");

        pstm.setObject(1,history.getWardNo());
        pstm.setObject(2,history.getInchargeId());
        pstm.setObject(3,history.getDateFrom());
        pstm.setObject(4,history.getDateTo());

        return pstm.executeUpdate() > 0;
    }

    public static boolean addNewInchargeWithoutWard(Incharge newIncharge) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO WardIncharge VALUES (?,?)");

        pstm.setObject(1,newIncharge.getInchargeId());
        pstm.setObject(2,newIncharge.getInchargeName());

        return pstm.executeUpdate() > 0;
    }

    public static boolean replaceIncharge(InchargeLogin newInchargeSignUp, String inchargeIdSelected) throws SQLException {

        Connection conn = null;

        try {
            conn = DbConnection.getInstance().getConnection();

            conn.setAutoCommit(false);

            PreparedStatement pstm = conn.prepareStatement("UPDATE InchargeLogin SET userName = ?, userPassword = ?, userType = ? " +
                    "WHERE wardNo = ?");

            pstm.setObject(1, newInchargeSignUp.getUserName());
            pstm.setObject(2, newInchargeSignUp.getUserPassword());
            pstm.setObject(3, newInchargeSignUp.getUserType().name());
            pstm.setObject(4, newInchargeSignUp.getWardNo());

            if (pstm.executeUpdate() > 0) {

                if (WardController.updateInchargeIdOfWard(inchargeIdSelected,newInchargeSignUp.getWardNo())) {
                    conn.commit();
                    return true;

                } else {
                    conn.rollback();
                    return false;
                }

            } else { // if some issue occurred while updating
                conn.rollback();
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
        return false;
    }

    public static boolean updateInchargeHistory(String wardNo, String currentInchargeId, String date, String newInchargeId) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("UPDATE InchargeHistory SET dateTo = ?" +
                        "WHERE wardNo = ? AND inchargeId = ? ");

        pstm.setObject(1,date);
        pstm.setObject(2,wardNo);
        pstm.setObject(3,currentInchargeId);

        if (pstm.executeUpdate() > 0) {

            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("INSERT INTO InchargeHistory (wardNo, inchargeId, dateFrom) VALUES (?,?,?)");

            pstm.setObject(1,wardNo);
            pstm.setObject(2,newInchargeId);
            pstm.setObject(3,date);

        }
        return pstm.executeUpdate() > 0;
        //return false;
    }

    public static List<String> getAllNames(List<String> cmbInchargeNameList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT inchargeName FROM WardIncharge ").executeQuery();

        while (rst.next()) {
            cmbInchargeNameList.add(rst.getString("inchargeName"));
        }
        return cmbInchargeNameList;
    }

    public static String[] getInchargeId(String wardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT wi.inchargeId, wi.inchargeName\n" +
                        "FROM WardIncharge wi INNER JOIN Ward w\n" +
                        "ON wi.inchargeId = w.inchargeId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "ORDER BY wi.inchargeId DESC LIMIT 1");

        pstm.setObject(1,wardNo);
        ResultSet rst = pstm.executeQuery();

        String[] idName = new String[2];

        if (rst.next()) {
            idName[0] = rst.getString(1);
            idName[1] = rst.getString(2);
        }
        return idName;
    }

    public static String getServiceStartDate(String inchargeId) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT dateFrom FROM InchargeHistory WHERE inchargeId = ? ");

        pstm.setObject(1,inchargeId);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;

    }
}
