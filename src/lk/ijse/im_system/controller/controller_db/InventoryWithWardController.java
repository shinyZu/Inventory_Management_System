package lk.ijse.im_system.controller.controller_db;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.im_system.controller.MainFormController;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Condemnation;
import lk.ijse.im_system.model.InventoryWithWard;
import lk.ijse.im_system.model.Order;
import lk.ijse.im_system.view.table_model.Category_InventoryTM;
import lk.ijse.im_system.view.table_model.InventoryInWardTM;
import lk.ijse.im_system.view.table_model.InventoryWardWiseTM;
import lk.ijse.im_system.view.table_model.Inventory_QtyTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryWithWardController {

    // For table in WardWiseInventoryForm
    public static ObservableList<InventoryWardWiseTM> getAllInventoryWithWards(ObservableList<InventoryWardWiseTM> wardWiseInventoryList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified\n" +
                        "FROM InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON w.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "on i.categoryId = c.categoryId").executeQuery();

        while (rst.next()) {
            wardWiseInventoryList.add(new InventoryWardWiseTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardWiseInventoryList;

    }

    public static ObservableList<InventoryWardWiseTM> getFilteredListByCategory(ObservableList<InventoryWardWiseTM> wardWiseInventoryList, String cmbCategorySelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified\n" +
                        "FROM InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON w.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryTitle = ?");

        pstm.setObject(1, cmbCategorySelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardWiseInventoryList.add(new InventoryWardWiseTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardWiseInventoryList;
    }

    public static ObservableList<InventoryWardWiseTM> getFilteredListByWard(ObservableList<InventoryWardWiseTM> wardWiseInventoryList, String cmbWardSelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified\n" +
                        "FROM InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON w.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ?");

        pstm.setObject(1, cmbWardSelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardWiseInventoryList.add(new InventoryWardWiseTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return wardWiseInventoryList;

    }

    // For Table in CategoryInWardsFormController
    public static ObservableList<Category_InventoryTM> getAllInventoryOfWard(ObservableList<Category_InventoryTM> wardInventoryList, String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.afterQty, w.location\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "WHERE w.wardNo = ?\n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1, loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new Category_InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            ));
        }
        return wardInventoryList;
    }

    // For table in CategoryInWardsFormController when a Category is selected from the list
    public static ObservableList<Category_InventoryTM> getInventoryCategoryWise(String loggedWardNo, String categorySelected, ObservableList<Category_InventoryTM> wardInventoryList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.afterQty, w.location\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND c.categoryTitle = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1, loggedWardNo);
        pstm.setObject(2, categorySelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new Category_InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            ));
        }
        return wardInventoryList;
    }

    // For table in WardInventoryFormController
    public static ObservableList<InventoryInWardTM> getAllInventoryForWard(ObservableList<InventoryInWardTM> wardInventoryList, String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1, loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return wardInventoryList;
    }

    public static boolean updateLocationInWard(String invNo, String cmbLocationSelected, int spinnerValue) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("UPDATE InventoryWithWard SET location = ?, notifyLevel = ? WHERE wardNo = ? AND invNo = ?");

        pstm.setObject(1,cmbLocationSelected);
        pstm.setObject(2,spinnerValue);
        pstm.setObject(3,MainFormController.loggedWardNo);
        pstm.setObject(4,invNo);


        return pstm.executeUpdate() > 0;
    }

//--------------------------------------------------Filters for Ward---------------------------------------------

    public static ObservableList<InventoryInWardTM> getFilteredListByCategoryForWard(String loggedWardNo, ObservableList<InventoryInWardTM> wardInventoryList, String cmbCategorySelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND c.categoryTitle = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,cmbCategorySelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return wardInventoryList;
    }

    public static ObservableList<InventoryInWardTM> getFilteredListByInventoryForWard(String loggedWardNo, ObservableList<InventoryInWardTM> wardInventoryList, String cmbInventorySelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND i.description = ? \n" +
                        "ORDER BY w.invNo;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,cmbInventorySelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return wardInventoryList;
    }

    public static ObservableList<InventoryInWardTM> getFilteredListByLocationForWard(String loggedWardNo, ObservableList<InventoryInWardTM> wardInventoryList, String cmbLocationSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND w.location = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,cmbLocationSelected);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            wardInventoryList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return wardInventoryList;
    }

//------------------------------------------------------------------------------------------------------------------

    public static List<InventoryInWardTM> searchInventory(String loggedWardNo, String text) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND i.description LIKE '%"+text+"%'");

        pstm.setObject(1,loggedWardNo);

        List<InventoryInWardTM> searchList = new ArrayList<>();

        ResultSet rst = pstm.executeQuery();
        while (rst.next()) {
            searchList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return searchList;
    }

    public static ObservableList<InventoryInWardTM> searchInventoryFromFilterList(String loggedWardNo, String text, ObservableList<InventoryInWardTM> filteredList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN InventoryWithWard w\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? \n" +
                        "AND i.description LIKE '%"+text+"%'");

        pstm.setObject(1,loggedWardNo);

        //List<InventoryInWardTM> searchList = new ArrayList<>();

        ResultSet rst = pstm.executeQuery();
        while (rst.next()) {
            filteredList.add(new InventoryInWardTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return filteredList;
    }

//------------------------------------------------------------------------------------------------------------------

    public static boolean deleteInventory(String invIdForDelete) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM Inventory WHERE invNo = ?");

        pstm.setObject(1,invIdForDelete);

        return pstm.executeUpdate() > 0;
    }

    public static ObservableList<Inventory_QtyTM> getOrderInventoryDetailsWithQty(ObservableList<Inventory_QtyTM> qtyListWithOrderQty) throws SQLException, ClassNotFoundException {

        String wardNo = MainFormController.loggedWardNo;

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.invNo, i.description, w.beforeQty, w. afterQty, w.lastModified, c.categoryTitle\n" +
                        "From InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? ");

        pstm.setObject(1, wardNo);

        ResultSet rst = pstm.executeQuery();

        String invNo = null;
        int beforeQty = 0;
        int afterQty = 0;
        String orderDate = null;

        while (rst.next()) {
            beforeQty = rst.getInt("beforeQty");
            afterQty = rst.getInt("afterQty");

            if (beforeQty < afterQty ) { // if its an Order

                invNo = rst.getString("invNo");
                orderDate = rst.getString("lastModified");

                pstm = DbConnection.getInstance().getConnection()
                        .prepareStatement("SELECT o.wardNo, od.invNo, od.orderQTY, o.orderDate\n" +
                                "FROM OrderDetail od INNER JOIN Orders o\n" +
                                "ON od.orderId = o.orderId\n" +
                                "WHERE o.wardNo = ? AND o.orderDate = ? AND od.invNo = ?");

                pstm.setObject(1,wardNo);
                pstm.setObject(2,orderDate);
                pstm.setObject(3,invNo);

                ResultSet rst2 = pstm.executeQuery();

                while (rst2.next()) {
                    qtyListWithOrderQty.add(new Inventory_QtyTM(
                            rst.getString("invNo"),
                            rst.getString("description"),
                            rst.getInt("beforeQty"),
                            rst2.getInt("orderQty"),
                            rst.getInt("afterQty"),
                            rst.getString("lastModified"),
                            rst.getString("categoryTitle")
                    ));
                }

            }

        }
        return qtyListWithOrderQty;
    }

    public static ObservableList<Inventory_QtyTM> getCondemnInventoryDetailsWithQty(ObservableList<Inventory_QtyTM> qtyListWithCondemnQty) throws SQLException, ClassNotFoundException {

        String wardNo = MainFormController.loggedWardNo;

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.invNo, i.description, w.beforeQty, w. afterQty, w.lastModified, c.categoryTitle\n" +
                        "From InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON i.invNo = w.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE w.wardNo = ? ");

        pstm.setObject(1, wardNo);

        ResultSet rst = pstm.executeQuery();

        String invNo = null;
        int beforeQty = 0;
        int afterQty = 0;
        String condemnDate = null;

        while (rst.next()) {
            beforeQty = rst.getInt("beforeQty");
            afterQty = rst.getInt("afterQty");

            if (beforeQty > afterQty) { // if its a Condemn

                invNo = rst.getString("invNo");
                condemnDate = rst.getString("lastModified");

                pstm = DbConnection.getInstance().getConnection()
                        .prepareStatement("SELECT c.wardNo, cd.invNo, cd.condemnQTY, c.condemnDate\n" +
                                "FROM CondemnDetail cd INNER JOIN Condemnation c\n" +
                                "ON cd.condemnId = c.condemnId\n" +
                                "WHERE c.wardNo = ? AND c.condemnDate = ? AND cd.invNo = ?");

                pstm.setObject(1, wardNo);
                pstm.setObject(2, condemnDate);
                pstm.setObject(3, invNo);

                ResultSet rst2 = pstm.executeQuery();

                while (rst2.next()) {
                    qtyListWithCondemnQty.add(new Inventory_QtyTM(
                            rst.getString("invNo"),
                            rst.getString("description"),
                            rst.getInt("beforeQty"),
                            rst2.getInt("condemnQty"),
                            rst.getInt("afterQty"),
                            rst.getString("lastModified"),
                            rst.getString("categoryTitle")
                    ));
                }

            }
        }
        return qtyListWithCondemnQty;
    }

    public static int getQtyWithWardOfInv(String loggedWardNo, String cmbInventoryNoSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT afterQty FROM InventoryWithWard \n" +
                        "WHERE wardNo = ? AND invNo = ?");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,cmbInventoryNoSelected);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getInt(1);
        }

        return 0;
    }

    public static boolean addInventoryToWard(Order newOrder, String invNo, int orderQty) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT invNo, beforeQty, afterQty \n" +
                        "FROM InventoryWithWard " +
                        "WHERE wardNo = ? AND invNo = ?");

        pstm.setObject(1,newOrder.getWardNo());
        pstm.setObject(2,invNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if its an already existing Inventory in Ward --> add the qty

            int newBeforeQty = 0;
            int newAfterQty = 0;

            newBeforeQty = rst.getInt(3);
            newAfterQty = rst.getInt(3) + orderQty;

            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("UPDATE InventoryWithWard SET beforeQty = ?, afterQty = ?, lastModified = ? " +
                            "WHERE wardNo = ? AND invNo = ?");

            pstm.setObject(1,newBeforeQty);
            pstm.setObject(2,newAfterQty);
            pstm.setObject(3,newOrder.getOrderDate());
            pstm.setObject(4,newOrder.getWardNo());
            pstm.setObject(5,invNo);

        } else { // if its not an already existing Inventory --> add the Inventory as a new Inventory for ward

            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("INSERT INTO InventoryWithWard VALUES (?,?,?,?,?,?,?)");

            pstm.setObject(1,newOrder.getWardNo());
            pstm.setObject(2,invNo);
            pstm.setObject(3,0);
            pstm.setObject(4,orderQty);
            pstm.setObject(5,newOrder.getOrderDate());
            pstm.setObject(6,"-");
            pstm.setObject(7,5);

        }
        return pstm.executeUpdate() > 0;
    }

    public static boolean updateQtyOfWard(String invNo, int condemnQty, Condemnation newCondemn) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT invNo, beforeQty, afterQty \n" +
                        "FROM InventoryWithWard " +
                        "WHERE wardNo = ? AND invNo = ?");

        pstm.setObject(1,newCondemn.getWardNo());
        pstm.setObject(2,invNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if it exists Inventory in Ward --> reduce the condemnQty from afterQTy

            int newBeforeQty = 0;
            int newAfterQty = 0;

            newBeforeQty = rst.getInt(3);
            newAfterQty = rst.getInt(3) - condemnQty;

            pstm = DbConnection.getInstance().getConnection()
                    .prepareStatement("UPDATE InventoryWithWard SET beforeQty = ?, afterQty = ?, lastModified = ? " +
                            "WHERE wardNo = ? AND invNo = ?");

            pstm.setObject(1, newBeforeQty);
            pstm.setObject(2, newAfterQty);
            pstm.setObject(3, newCondemn.getCondemnDate());
            pstm.setObject(4, newCondemn.getWardNo());
            pstm.setObject(5, invNo);

        }
        return pstm.executeUpdate() > 0;
    }

    public static String getNotifyLevel(InventoryInWardTM inventorySelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT notifyLevel FROM InventoryWithWard WHERE invNo = ?");

        pstm.setObject(1,inventorySelected.getInvNo());
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getSurgicalItemCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(iw.afterQty)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? \n" +
                        "AND c.categoryId = ? \n" +
                        "GROUP BY c.categoryId;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,CategoryController.getCategoryId("Surgical Items"));

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getLinenCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(iw.afterQty)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? \n" +
                        "AND c.categoryId = ? \n" +
                        "GROUP BY c.categoryId;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,CategoryController.getCategoryId("Linen"));

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getWoodenFurnCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(iw.afterQty)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? \n" +
                        "AND c.categoryId = ? \n" +
                        "GROUP BY c.categoryId;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,CategoryController.getCategoryId("Wooden Furniture"));

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getMetalFurnCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(iw.afterQty)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? \n" +
                        "AND c.categoryId = ? \n" +
                        "GROUP BY c.categoryId;");

        pstm.setObject(1,loggedWardNo);
        pstm.setObject(2,CategoryController.getCategoryId("Metal Furniture"));

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getInventoryCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(afterQty)\n" +
                        "FROM InventoryWithWard\n" +
                        "WHERE wardNo = ? ");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getInvBelowLevelCount(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(invNo) FROM InventoryWithWard\n" +
                        "WHERE wardNo = ? \n" +
                        "AND afterQty < notifyLevel;");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static boolean isInventoryExistsWithWard(String loggedWardNo) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM InventoryWithWard WHERE wardNo = '"+loggedWardNo+"'").executeQuery();

        if (rst.next()) {
            return true;
        }
        return false;
    }

    public static List<InventoryWithWard> getInventoryDetailsForRestock(String loggedWardNo, List<InventoryWithWard> remainingQtyList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM InventoryWithWard\n" +
                        "WHERE wardNo = ? \n" +
                        "AND afterQty < notifyLevel;");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            remainingQtyList.add(new InventoryWithWard(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getInt(7)
            ));
        }
        return remainingQtyList;
    }

    public static XYChart.Series<String, Integer> setValuesToBarChart(XYChart.Series<String, Integer> chart, String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT c.categoryTitle, SUM(iw.afterQty)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? \n" +
                        "GROUP BY c.categoryId;");

        pstm.setObject(1,loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            chart.getData().add(new XYChart.Data<>(rst.getString(1),rst.getInt(2)));
        }
        return chart;
    }
}
