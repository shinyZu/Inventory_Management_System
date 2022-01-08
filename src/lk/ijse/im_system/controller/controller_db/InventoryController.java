package lk.ijse.im_system.controller.controller_db;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Inventory;
import lk.ijse.im_system.model.InventoryWithWard;
import lk.ijse.im_system.model.Order;
import lk.ijse.im_system.model.OrderDetail;
import lk.ijse.im_system.view.table_model.Category_InventoryTM;
import lk.ijse.im_system.view.table_model.InventoryTM;
import lk.ijse.im_system.view.table_model.InventoryWardWiseTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {

    // For table in CategoryForm
    public static ObservableList<Category_InventoryTM> getAllInventory(ObservableList<Category_InventoryTM> inventoryList) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "ORDER BY i.invNo");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            inventoryList.add(new Category_InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            ));
        }
        return inventoryList;
    }

    // For listView in CategoryForm
    public static ObservableList<Category_InventoryTM> getInventoryCategoryWise(String categorySelected, ObservableList<Category_InventoryTM> inventoryList) throws SQLException, ClassNotFoundException {
        //String categoryId = null;
        String categoryId = CategoryController.getCategoryId(categorySelected);

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "WHERE i.categoryId = ? \n" +
                        "order by i.invNo;");

        pstm.setObject(1,categoryId);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            inventoryList.add(new Category_InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4)
            ));
        }
        return inventoryList;
    }

    // For table in StoreInventoryForm
    public static ObservableList<InventoryTM> getAllInventoryWithCategory(ObservableList<InventoryTM> inventoryList) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "ORDER BY i.invNo");

        ResultSet rst = pstm.executeQuery();
        while (rst.next()) {
            inventoryList.add(new InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return inventoryList;
    }

    // NewInventoryForm
    public static boolean addInventory(Inventory inventory) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO Inventory VALUES (?,?,?,?,?,?)");

        pstm.setObject(1,inventory.getInventoryNo());
        pstm.setObject(2,inventory.getDescription());
        pstm.setObject(3,inventory.getQtyOnHand());
        pstm.setObject(4,inventory.getCategoryId());
        pstm.setObject(5,inventory.getLocationId());
        pstm.setObject(6,inventory.getNotifyLevel());

        if (pstm.executeUpdate() > 0) {
            return true;
        }
        return false;

    }

    // To auto generate next InventoryId of new Inventory to be added
    public static String getNextID() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT invNo FROM Inventory ORDER BY invNo DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "INV-000" + tempId;
            }else if (tempId <= 99){
                return "INV-00" + tempId;
            }else {
                return "INV-0" + tempId;
            }
        } else {
            return "INV-0001";
        }
    }

//-----------------------------------------Filters to Store-----------------------------------------------------------------------

    public static ObservableList<InventoryTM> getFilteredListByCategory(ObservableList<InventoryTM> inventoryList, String cmbCategorySelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryTitle = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1,cmbCategorySelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            inventoryList.add(new InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return inventoryList;

    }

    public static ObservableList<InventoryTM> getFilteredListByInventory(ObservableList<InventoryTM> inventoryList, String cmbInventorySelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE i.description = ? \n" +
                        "ORDER BY i.invNo");

        pstm.setObject(1,cmbInventorySelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            inventoryList.add(new InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return inventoryList;
    }

    public static ObservableList<InventoryTM> getFilteredListByLocation(ObservableList<InventoryTM> inventoryList, String cmbLocationSelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle\n" +
                        "FROM Inventory i INNER JOIN Location l\n" +
                        "ON i.locationId = l.locationId\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE l.locationName = ? \n" +
                        "ORDER BY i.invNo;");

        pstm.setObject(1,cmbLocationSelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            inventoryList.add(new InventoryTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return inventoryList;
    }

//-----------------------------------------------------------------------------------------------------------------------------

    // For cmb in CategoryForm
    public static List<String> getInventoryNo(List<String> cmbInventoryNoList) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement("SELECT invNo FROM Inventory ORDER BY invNo");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbInventoryNoList.add(rst.getString(1));
        }
        return cmbInventoryNoList;
    }

    // For cmb in CategoryForm,StoreInventoryForm
    public static List<String> getInvDescription(List<String> cmbDescriptionList) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement("SELECT description FROM Inventory");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbDescriptionList.add(rst.getString(1));
        }
        return cmbDescriptionList;
    }

    // For cmb in WardOrderForm
    public static List<String> getAllInventoryNo(List<String> cmbInventoryNoList) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT invNo FROM Inventory ORDER BY invNo").executeQuery();

        while (rst.next()) {
            cmbInventoryNoList.add(rst.getString(1));
        }
        return cmbInventoryNoList;
    }

    public static boolean updateInventory(Inventory invDetailsForUpdate) throws SQLException, ClassNotFoundException {
        System.out.println(invDetailsForUpdate);
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("UPDATE Inventory SET qtyOnHand = ?, categoryId = ?, locationId = ?, notifyLevel = ?\n" +
                        "WHERE invNo = ?");

        pstm.setObject(1,invDetailsForUpdate.getQtyOnHand());
        pstm.setObject(2,invDetailsForUpdate.getCategoryId());
        pstm.setObject(3,invDetailsForUpdate.getLocationId());
        pstm.setObject(4,invDetailsForUpdate.getNotifyLevel());
        pstm.setObject(5,invDetailsForUpdate.getInventoryNo());

        return pstm.executeUpdate() > 0;
    }

    public static List<InventoryWardWiseTM> searchInventory(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified\n" +
                        "FROM InventoryWithWard w INNER JOIN Inventory i\n" +
                        "ON w.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE i.description LIKE '%"+text+"%'");

       // pstm.setObject(1,text);

        List<InventoryWardWiseTM> searchList = new ArrayList<>();

        ResultSet rst = pstm.executeQuery();
        while (rst.next()) {
            searchList.add(new InventoryWardWiseTM(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return searchList;
    }

    public static List<String> getInventoryNoByCategory(String cmbCategorySelected, List<String> cmbInventoryNoByCategoryList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.invNo \n" +
                        "FROM Inventory i INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryTitle = ? ");

        pstm.setObject(1,cmbCategorySelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbInventoryNoByCategoryList.add(rst.getString(1));
        }
        return cmbInventoryNoByCategoryList;
    }

    public static List<String> getDescripByCategory(String cmbCategorySelected, List<String> cmbDescripByCategoryList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT i.description \n" +
                        "FROM Inventory i INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryTitle = ? ");

        pstm.setObject(1,cmbCategorySelected);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbDescripByCategoryList.add(rst.getString(1));
        }
        return cmbDescripByCategoryList;
    }

    public static Inventory getInventoryOnInvNo(String cmbInventoryNoSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Inventory WHERE invNo = ? ");

        pstm.setObject(1,cmbInventoryNoSelected);
        ResultSet rst = pstm.executeQuery();

        if(rst.next()) {
            return new Inventory(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6)
            );
        }
        return null;
    }

    public static Inventory getInventoryOnDescrip(String cmbDescriptionSelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Inventory WHERE description = ? ");

        pstm.setObject(1,cmbDescriptionSelected);
        ResultSet rst = pstm.executeQuery();

        if(rst.next()) {
            return new Inventory(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6)

            );
        }
        return null;
    }

    public static String getOnlyDescription(String cmbInventoryNoSelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT description FROM Inventory WHERE invNo = ? ");

        pstm.setObject(1,cmbInventoryNoSelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getOnlyInvNo(String cmbDescriptionSelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT invNo FROM Inventory WHERE description = ? ");

        pstm.setObject(1,cmbDescriptionSelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static boolean updateQtyOfStoreInventory(String invNo, int orderQty, Order newOrder) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("UPDATE Inventory SET qtyOnHand = (qtyOnHand - ?) WHERE invNo = ? ");

        pstm.setObject(1,orderQty);
        pstm.setObject(2,invNo);

        if (pstm.executeUpdate() > 0) {

            if (InventoryWithWardController.addInventoryToWard(newOrder,invNo,orderQty)) {
                //

            } else {
                return false;
            }

        } else {
            return false;
        }
        return true;
    }

    public static String getNotifyLevel(InventoryTM inventorySelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT notifyLevel FROM Inventory WHERE invNo = ?");

        pstm.setObject(1,inventorySelected.getInventoryNo());
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getSurgicalItemCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(i.qtyOnHand)\n" +
                        "FROM Inventory i INNER JOIN  Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryId = 'CTG-001'\n" +
                        "GROUP BY i.categoryId").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getLinenCount() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(i.qtyOnHand)\n" +
                        "FROM Inventory i INNER JOIN  Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryId = 'CTG-002'\n" +
                        "GROUP BY i.categoryId;").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;

    }

    public static String getWoodenFurnCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(i.qtyOnHand)\n" +
                        "FROM Inventory i INNER JOIN  Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryId = 'CTG-003'\n" +
                        "GROUP BY i.categoryId;").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;

    }

    public static String getMetalFurnCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(i.qtyOnHand)\n" +
                        "FROM Inventory i INNER JOIN  Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE c.categoryId = 'CTG-004'\n" +
                        "GROUP BY i.categoryId").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getInventoryCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT SUM(qtyOnHand) FROM Inventory;").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static boolean isInventoryExists(Inventory inventory) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT description FROM Inventory WHERE description = ?");

        pstm.setObject(1,inventory.getDescription());

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if description already exists
            return true;

        }
        return false;
    }

    public static List<Inventory> getInventoryDetailsForRestock(List<Inventory> remainingQtyList) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM Inventory\n" +
                        "WHERE qtyOnHand < notifyLevel;");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            remainingQtyList.add(new Inventory(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6)
            ));
        }
        return remainingQtyList;
    }

    public static String getInvBelowLevelCount() throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(invNo) FROM Inventory\n" +
                        "WHERE qtyOnHand < notifyLevel;");

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static XYChart.Series<String,Integer> setValuesToBarChart(XYChart.Series<String,Integer> chart) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT c.categoryTitle, SUM(i.qtyOnHand)\n" +
                        "FROM Inventory i INNER JOIN  Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "GROUP BY i.categoryId;");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            chart.getData().add(new XYChart.Data<>(rst.getString(1),rst.getInt(2)));
        }
        return chart;
    }
}
