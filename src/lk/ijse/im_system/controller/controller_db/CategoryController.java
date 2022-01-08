package lk.ijse.im_system.controller.controller_db;

import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryController {

    public static List<String> getAllCategories(List<String> categoryList) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT categoryTitle FROM Category").executeQuery();

        while (rst.next()) {
            categoryList.add(rst.getString(1));
        }
        return categoryList;
    }

    public static String getNextID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT categoryId FROM Category ORDER BY categoryId DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "CTG-00" + tempId;
            }else if (tempId <= 99){
                return "CTG-0" + tempId;
            }else {
                return "CTG-" + tempId;
            }
        } else {
            return "CTG-001";
        }
    }

    public static boolean addCategory(Category category) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO Category VALUES (?,?)");

        pstm.setObject(1,category.getCategoryId());
        pstm.setObject(2,category.getCategoryTitle());

        if (pstm.executeUpdate() > 0) {
            return true;
        }
        return false;
    }

    public static String getCategoryId(String categorySelected) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT categoryId FROM Category WHERE categoryTitle = ?");

        pstm.setObject(1,categorySelected);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getCategoryName(String categoryId) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT categoryTitle FROM Category WHERE categoryId = ? ");

        pstm.setObject(1,categoryId);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static boolean deleteCategory(String categoryIdSelected) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM Category WHERE categoryId = ?");

        pstm.setObject(1,categoryIdSelected);
        return pstm.executeUpdate() > 0;
    }

    public static String getCategoryCount() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(categoryId) FROM Category").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }


    public static String getCategoryCountInWard(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(DISTINCT c.categoryId)\n" +
                        "FROM InventoryWithWard iw INNER JOIN Inventory i\n" +
                        "ON iw.invNo = i.invNo\n" +
                        "INNER JOIN Category c\n" +
                        "ON i.categoryId = c.categoryId\n" +
                        "WHERE iw.wardNo = ? ");

        pstm.setObject(1,loggedWardNo);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;

    }

    public static boolean isCategoryExists(Category category) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT categoryTitle FROM Category WHERE categoryTitle = ?");

        pstm.setObject(1,category.getCategoryTitle());

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if category already exists
            return true;

        }
        return false;
    }
}
