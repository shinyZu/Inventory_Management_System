package lk.ijse.im_system.controller.controller_db;

import javafx.collections.ObservableList;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Order;
import lk.ijse.im_system.model.OrderDetail;
import lk.ijse.im_system.view.table_model.WardWise_Order_Condemn_DetailsTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderController {

    public static String getNextOrderID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT orderId FROM Orders ORDER BY orderId DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "ORD-000" + tempId;
            }else if (tempId <= 99){
                return "ORD-00" + tempId;
            }else {
                return "ORD-0" + tempId;
            }
        } else {
            return "ORD-0001";
        }
    }

    public static ObservableList<WardWise_Order_Condemn_DetailsTM> getWardOrderDetails(ObservableList<WardWise_Order_Condemn_DetailsTM> orderDetailList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT o.orderId, o.wardNo, od.invNo, i.description, od.orderQty, o.orderDate\n" +
                        "FROM Orders o INNER JOIN OrderDetail od\n" +
                        "ON o.orderId = od.orderId\n" +
                        "INNER JOIN Inventory i\n" +
                        "ON od.invNo = i.invNo;")
                .executeQuery();

        while (rst.next()) {
             orderDetailList.add(new WardWise_Order_Condemn_DetailsTM(
                    rst.getString("orderId"),
                    rst.getString("wardNo"),
                    rst.getString("invNo"),
                    rst.getString("description"),
                    rst.getInt("orderQty"),
                    rst.getString("orderDate")
            ));
        }
        return orderDetailList;
    }

    public static boolean confirmOrder(Order newOrder, ArrayList<OrderDetail> orderInvDetails) throws SQLException {

        Connection conn = null;

        try {
            conn = DbConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstm = conn.prepareStatement("INSERT INTO Orders VALUES (?,?,?)");

            pstm.setObject(1,newOrder.getOrderId());
            pstm.setObject(2,newOrder.getOrderDate());
            pstm.setObject(3,newOrder.getWardNo());

            if (pstm.executeUpdate() > 0) { // if the Orders table is temporary updated

                if (saveOrderDetails(orderInvDetails,newOrder)) { // if OrderDetail table is updated temporary
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

    private static boolean saveOrderDetails(ArrayList<OrderDetail> orderInvDetails, Order newOrder) throws SQLException, ClassNotFoundException {

        for (OrderDetail odt : orderInvDetails) {

            PreparedStatement pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("INSERT INTO OrderDetail VALUES (?,?,?)");

            pstm.setObject(1,odt.getOrderId());
            pstm.setObject(2,odt.getInvNo());
            pstm.setObject(3,odt.getOrderQty());

            if (pstm.executeUpdate() > 0) { // if OrderDetail table is affected

                if (InventoryController.updateQtyOfStoreInventory(odt.getInvNo(), odt.getOrderQty(), newOrder)) { // reduce the orderedQty from Store qtyOnHand
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

    public static String getDailyOrderCount(String date) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(orderId) FROM Orders WHERE orderDate = '"+date+"' ").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getMonthlyOrderCountOfWard(String loggedWardNo, String month) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(orderId) \n" +
                        "FROM Orders \n" +
                        "WHERE wardNo = ? \n" +
                        "AND orderDate LIKE '%"+'-'+month+'-'+"%'");

        pstm.setObject(1,loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getTotalOrders(String wardNo, String dateFrom, String dateTo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(orderId)\n" +
                        "FROM Orders\n" +
                        "WHERE orderDate BETWEEN ? AND ? \n" +
                        "AND wardNo = ? ");

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
