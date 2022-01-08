package lk.ijse.im_system.controller.controller_db;

import javafx.collections.ObservableList;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.CondemnDetail;
import lk.ijse.im_system.model.Condemnation;
import lk.ijse.im_system.model.OrderDetail;
import lk.ijse.im_system.view.table_model.WardWise_Order_Condemn_DetailsTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CondemnController {
    public static String getNextCondemnID() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT condemnId FROM Condemnation ORDER BY condemnId DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "CDM-000" + tempId;
            }else if (tempId <= 99){
                return "CDM-00" + tempId;
            }else {
                return "CDM-0" + tempId;
            }
        } else {
            return "CDM-0001";
        }
    }

    public static ObservableList<WardWise_Order_Condemn_DetailsTM> getWardCondemnDetails(ObservableList<WardWise_Order_Condemn_DetailsTM> condemnDetailList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT c.condemnId, c.wardNo, cd.invNo, i.description, cd.condemnQTY, c.condemnDate\n" +
                        "FROM Condemnation c INNER JOIN CondemnDetail cd\n" +
                        "ON c.condemnId = cd.condemnId\n" +
                        "INNER JOIN Inventory i\n" +
                        "ON cd.invNo = i.invNo;")
                .executeQuery();

        while (rst.next()) {
            condemnDetailList.add(new WardWise_Order_Condemn_DetailsTM(
                    rst.getString("condemnId"),
                    rst.getString("wardNo"),
                    rst.getString("invNo"),
                    rst.getString("description"),
                    rst.getInt("condemnQTY"),
                    rst.getString("condemnDate")
            ));
        }
        return condemnDetailList;
    }

    public static boolean confirmCondemn(Condemnation newCondemn, ArrayList<CondemnDetail> condemnInvDetails) throws SQLException {

        Connection conn = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstm = conn.prepareStatement("INSERT INTO Condemnation VALUES (?,?,?)");

            pstm.setObject(1,newCondemn.getCondemnId());
            pstm.setObject(2,newCondemn.getWardNo());
            pstm.setObject(3,newCondemn.getCondemnDate());

            if (pstm.executeUpdate() > 0) { // if data is sent to table Condemnation successfully (not saved bcz autoCommit is set to false)

                if (saveCondemnDetails(newCondemn, condemnInvDetails)) { // if data is sent/updated to tables CondemnDetail, InventoryWithWard without any issue
                    conn.commit();
                    return true;

                } else {
                    conn.rollback();
                    return false;
                }

            } else {
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

    private static boolean saveCondemnDetails(Condemnation newCondemn, ArrayList<CondemnDetail> condemnInvDetails) throws SQLException, ClassNotFoundException {

        for (CondemnDetail cdt : condemnInvDetails) {

            PreparedStatement pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("INSERT INTO CondemnDetail VALUES (?,?,?)");

            pstm.setObject(1,cdt.getCondemnId());
            pstm.setObject(2,cdt.getInvNo());
            pstm.setObject(3,cdt.getCondemnQty());

            if (pstm.executeUpdate() > 0) { // if CondemnDetail table is affected

                if (InventoryWithWardController.updateQtyOfWard(cdt.getInvNo(), cdt.getCondemnQty(), newCondemn)) { // reduce the condemnQty from Ward qtyOnHand
                    //

                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
        return true;
    }

    public static String getDailyCondemnCount(String date) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(condemnId) FROM Condemnation WHERE condemnDate = '"+date+"'").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getMonthlyCondemnCount(String loggedWardNo, String month) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(condemnId)\n" +
                        "FROM Condemnation\n" +
                        "WHERE wardNo = 'WD-001'\n" +
                        "AND condemnDate LIKE '%-"+month+"-%'").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getTotalCondemns(String wardNo, String dateFrom, String dateTo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(condemnId)\n" +
                        "FROM Condemnation\n" +
                        "WHERE condemnDate BETWEEN ? AND ? \n" +
                        "AND wardNo = ?  ");

        pstm.setObject(1,dateFrom);
        pstm.setObject(2,dateTo);
        pstm.setObject(3,wardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
