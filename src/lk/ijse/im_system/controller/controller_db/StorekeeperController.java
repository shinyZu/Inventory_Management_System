package lk.ijse.im_system.controller.controller_db;

import lk.ijse.im_system.db.DbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StorekeeperController {

    public static String getStorekeeperCount() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(userName) FROM StorekeeperLogin;").executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
