package lk.ijse.im_system.controller.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.model.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FieldDataForm {

    public ComboBox<String> cmbCategory;
    public ComboBox<String> cmbCategory2;
    public ComboBox<String> cmbInventoryNo; // WardOrderForm
    public ComboBox<String> cmbInventoryNo2; // WardOrderForm
    public ComboBox<String> cmbLocation;
    public ComboBox<String> cmbLocation2;
    public ComboBox<String> cmbDescription;
    public ComboBox<String> cmbDescription2;
    public ComboBox<String> cmbWard;
    public ComboBox<String> cmbWardNo; // OrderForm, CondemnInventoryForm cmb 1
    public ComboBox<String> cmbWardTitle;
    public ComboBox<String> cmbInchargeName;

    List<String> cmbInventoryNoList = new ArrayList<>();
    public void loadInventoryNo() throws SQLException, ClassNotFoundException { // loads Inventory Numbers to combo box
        cmbInventoryNoList = InventoryController.getAllInventoryNo(cmbInventoryNoList);
        cmbInventoryNo.getItems().addAll(cmbInventoryNoList);
    }

    List<String> cmbCategoryList = new ArrayList<>();
    public void loadCategory() throws SQLException, ClassNotFoundException { // loads Category to combo box
        cmbCategoryList = CategoryController.getAllCategories(cmbCategoryList);
        cmbCategory.getItems().addAll(cmbCategoryList);
    }

    public void loadCategoryForEditCmb() throws SQLException, ClassNotFoundException { // loads Category to combo box on edit pane
       // cmbCategoryList = CategoryController.getAllCategories(cmbCategoryList);
        cmbCategory2.getItems().addAll(cmbCategoryList);
    }

    List<String> cmbDescriptionList = new ArrayList<>();
    public void loadDescription() throws SQLException, ClassNotFoundException { // loads Inventory Descriptions to combo box
        cmbDescriptionList = InventoryController.getInvDescription(cmbDescriptionList);
        cmbDescription.getItems().addAll(cmbDescriptionList);
    }

    public void loadDescriptionForFilter() throws SQLException, ClassNotFoundException { // loads Inventory Descriptions to combo box to filter from table
        cmbDescriptionList.clear();
        cmbDescriptionList = InventoryController.getInvDescription(cmbDescriptionList);
        cmbDescription2.getItems().clear();
        cmbDescription2.getItems().addAll(cmbDescriptionList);
    }

    List<String> cmbLocationList = new ArrayList<>();
    public void loadLocation() throws SQLException, ClassNotFoundException { // loads Location to combo box
        cmbLocationList.clear();
        cmbLocationList = LocationController.getAllLocationNames(cmbLocationList);
        cmbLocation.getItems().clear();
        cmbLocation.getItems().addAll(cmbLocationList);
    }

    List<String> cmbLocationListOfStore = new ArrayList<>();
    public void loadLocationsOfStore() throws SQLException, ClassNotFoundException { // loads Locations of Store to combo box
        cmbLocationListOfStore.clear();
        cmbLocationListOfStore = LocationController.getAllLocationsOfStore(cmbLocationListOfStore);
        cmbLocation.getItems().clear();
        cmbLocation.getItems().addAll(cmbLocationListOfStore);
        //cmbLocation2.getItems().addAll(cmbLocationListOfStore);
    }

    List<String> cmbLocationListOfWard = new ArrayList<>();
    public void loadLocationsOfWard(String loggedWardNo) throws SQLException, ClassNotFoundException { // loads Locations of Ward to combo box
        cmbLocationListOfWard.clear();
        cmbLocationListOfWard = LocationController.getAllLocationsOfWard(loggedWardNo,cmbLocationListOfWard);
        cmbLocation.getItems().clear();
        cmbLocation2.getItems().clear();
        cmbLocation.getItems().addAll(cmbLocationListOfWard);
        cmbLocation2.getItems().addAll(cmbLocationListOfWard);
    }

    public void loadLocationForEditCmb() throws SQLException, ClassNotFoundException { // loads Location to combo box on edit pane
        //cmbLocationList = new LocationController().getAllLocationNames(cmbLocationList);
        cmbLocationListOfStore.clear();
        cmbLocationListOfStore = LocationController.getAllLocationsOfStore(cmbLocationListOfStore);
        //cmbLocationList.clear();
        cmbLocation2.getItems().clear();
        cmbLocation2.getItems().addAll(cmbLocationListOfStore);
    }

    List<String> cmbWardNoList = new ArrayList<>();
    public void loadWardNo() throws SQLException, ClassNotFoundException { // loads Ward Numbers to combo box
        cmbWard.getItems().clear();
        cmbWardNoList.clear();
        cmbWardNoList = WardController.getAllWardNo(cmbWardNoList);
        cmbWard.getItems().addAll(cmbWardNoList);
    }

    public void loadWards() throws SQLException, ClassNotFoundException { // loads Ward Numbers to combo box in OrderForm, CondemnInventoryForm
        cmbWardNoList.clear();
        cmbWardNoList = WardController.getAllWardNo(cmbWardNoList);
        cmbWardNo.getItems().addAll(cmbWardNoList);
    }

    public void loadCmbDescripByCategoryToFilter(String cmbCategorySelected) throws SQLException, ClassNotFoundException { // loads Descriptions to combo box according to the Category selected
        cmbDescription2.getItems().clear();
        cmbDescripByCategoryList.clear();
        cmbDescripByCategoryList = InventoryController.getDescripByCategory(cmbCategorySelected,cmbDescripByCategoryList);
        cmbDescription2.getItems().addAll(cmbDescripByCategoryList);
    }

    // WardOrderForm, WardCondemnInventoryForm, OrderForm, CondemnInventoryForm
    public void filterComboBoxes(String cmbCategorySelected) throws SQLException, ClassNotFoundException {
        cmbInventoryNo.setDisable(false);
        cmbDescription.setDisable(false);

        loadCmbInvNoByCategory(cmbCategorySelected);
        loadCmbDescripByCategory(cmbCategorySelected);
    }

    List<String> cmbInventoryNoByCategoryList = new ArrayList<>();
    public void loadCmbInvNoByCategory(String cmbCategorySelected) throws SQLException, ClassNotFoundException { // loads Inventory Numbers to combo box according to the Category selected
        cmbInventoryNo.getItems().clear();
        cmbInventoryNoByCategoryList.clear();
        cmbInventoryNoByCategoryList = InventoryController.getInventoryNoByCategory(cmbCategorySelected,cmbInventoryNoByCategoryList);
        cmbInventoryNo.getItems().addAll(cmbInventoryNoByCategoryList);
    }

    List<String> cmbDescripByCategoryList = new ArrayList<>();
    public void loadCmbDescripByCategory(String cmbCategorySelected) throws SQLException, ClassNotFoundException { // loads Descriptions to combo box according to the Category selected
        cmbDescription.getItems().clear();
        cmbDescripByCategoryList.clear();
        cmbDescripByCategoryList = InventoryController.getDescripByCategory(cmbCategorySelected,cmbDescripByCategoryList);
        cmbDescription.getItems().addAll(cmbDescripByCategoryList);
    }

    List<String> cmbWardTitleList = new ArrayList<>();
    public void loadTitle() throws SQLException, ClassNotFoundException { // loads Ward Titles to combo box
        cmbWardTitleList = WardController.getAllTitles(cmbWardTitleList);
        cmbWardTitle.getItems().addAll(cmbWardTitleList);
    }

    List<String> cmbInchargeNameList = new ArrayList<>();
    public void loadInchargeNames() throws SQLException, ClassNotFoundException { // loads Incharge Names to combo box
        cmbInchargeNameList = InchargeController.getAllNames(cmbInchargeNameList);
        cmbInchargeName.getItems().addAll(cmbInchargeNameList);
    }

}
