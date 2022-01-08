package lk.ijse.im_system.controller.controller_db;

import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LocationController {

    public static String getLocationNameById(String locationId) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationName FROM Location WHERE locationId = ?");

        pstm.setObject(1,locationId);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static String getLocationIdByName(String locationName) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationId FROM Location WHERE locationName = ?");

        pstm.setObject(1,locationName);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public static List<String> getAllLocationNames(List<String> cmbLocationList) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationName FROM Location");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbLocationList.add(rst.getString(1));
        }
        return cmbLocationList;
    }

    // To auto generate next LocationID of new Location to be added
    public static String getNextID() throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationId FROM Location ORDER BY locationId DESC LIMIT 1").executeQuery();

        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId =tempId+1;

            if (tempId <= 9){
                return "L-00" + tempId;
            }else if (tempId <= 99){
                return "L-0" + tempId;
            }else {
                return "L-" + tempId;
            }
        } else {
            return "L-001";
        }
    }

    public static boolean addLocation(Location newLocation) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("INSERT INTO Location VALUES (?,?,?)");

        pstm.setObject(1,newLocation.getLocationId());
        pstm.setObject(2,newLocation.getLocationName());

        if (newLocation.getCreatedBy() == null) {
            pstm.setObject(3,"Store");
        } else {
            pstm.setObject(3,newLocation.getCreatedBy());
        }

        return pstm.executeUpdate() > 0;

    }

    public static boolean isLocationExists(Location newLocation) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationName FROM Location WHERE locationName = ? AND createdBy = ?");

        pstm.setObject(1,newLocation.getLocationName());

        if (newLocation.getCreatedBy() == null) {
            pstm.setObject(2,"Store");
        } else {
            pstm.setObject(2,newLocation.getCreatedBy());
        }

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) { // if location already exists
            return true;

        }
        return false;
    }

    public static List<String> getAllLocationsOfStore(List<String> cmbLocationListOfStore) throws SQLException, ClassNotFoundException {

        ResultSet rst = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationName FROM Location WHERE createdBy = 'Store'").executeQuery();


        while (rst.next()) {
            cmbLocationListOfStore.add(rst.getString(1));
        }
        return cmbLocationListOfStore;
    }

    public static List<String> getAllLocationsOfWard(String loggedWardNo, List<String> cmbLocationListOfWard) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT locationName FROM Location WHERE createdBy = ? ");

        pstm.setObject(1,loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            cmbLocationListOfWard.add(rst.getString(1));
        }
        return cmbLocationListOfWard;
    }

    public static String getLocationCountOfWard(String loggedWardNo) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement("SELECT COUNT(locationId) from Location where createdBy = ? ");

        pstm.setObject(1,loggedWardNo);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
