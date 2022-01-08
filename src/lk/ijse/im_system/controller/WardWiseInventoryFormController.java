package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.view.table_model.InventoryWardWiseTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class WardWiseInventoryFormController extends FieldDataForm implements LoadTableEvent {

    public Label lblSelectedWard;
    public TextField txtSearch;
    public JFXDatePicker datePicker;

    public TableView<InventoryWardWiseTM> tblWardWiseInventory;
    public TableColumn colWardNo;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colCategory;
    public TableColumn colLastModified;

    String cmbWardSelected = null;
    String cmbCategorySelected = null;
    String cmbInvSelected = null;
    String date = null;

    public void initialize() {
        try {
            initTable();
            loadCategory();
            loadWardNo();
            loadDescription();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbCategorySelected = newValue;
        });

        cmbWard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbWardSelected = newValue;
            //lblSelectedWard.setText(newValue+" - "+ WardController.getWardTitle(newValue));
        });

        cmbDescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbInvSelected = newValue;
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            date = String.valueOf(newValue);
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    search(newValue);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        colWardNo.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colLastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));

        loadTable();
    }

    ObservableList<InventoryWardWiseTM> wardWiseInventoryList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        tblWardWiseInventory.getItems().clear();
        wardWiseInventoryList = InventoryWithWardController.getAllInventoryWithWards(wardWiseInventoryList);
        tblWardWiseInventory.setItems(wardWiseInventoryList);
    }

    public void searchInventoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblWardWiseInventory.getItems().clear();
        search(txtSearch.getText());
    }

    List<InventoryWardWiseTM> searchList = new ArrayList<>();

    private void search(String value) throws SQLException, ClassNotFoundException {
        searchList = InventoryController.searchInventory(txtSearch.getText());
        tblWardWiseInventory.setItems(FXCollections.observableArrayList(searchList));
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        searchList.clear();
        filterTable(cmbWardSelected,cmbCategorySelected,cmbInvSelected,date);
    }

    FilteredList<InventoryWardWiseTM> filteredData = null;
    SortedList<InventoryWardWiseTM> sortedData = null;
    ArrayList<InventoryWardWiseTM> uniqueList = null;

    private void filterTable(String cmbWardSelected, String cmbCategorySelected, String cmbInvSelected, String date) throws SQLException, ClassNotFoundException {

        if (cmbWardSelected == null && cmbInvSelected == null && date == null) {
            //loadTable();
            return;
        }

        wardWiseInventoryList = InventoryWithWardController.getAllInventoryWithWards(wardWiseInventoryList);

        if (cmbWardSelected != null && cmbCategorySelected == null && cmbInvSelected == null && date == null) { // if only Ward is selected
            lblSelectedWard.setText(cmbWardSelected+" - "+ WardController.getWardTitle(cmbWardSelected));
            filteredData = new FilteredList<>(
                    wardWiseInventoryList, a -> a.getWardNo().equals(cmbWardSelected));

        } else if (cmbCategorySelected != null && cmbWardSelected == null && cmbInvSelected == null && date == null) { // if only category is selected
            filteredData = new FilteredList<>(
                    wardWiseInventoryList, b -> b.getCategory().equals(cmbCategorySelected));

        } else if (cmbInvSelected != null && cmbWardSelected == null && cmbCategorySelected == null && date == null) { // if only Inventory is selected
            filteredData = new FilteredList<>(wardWiseInventoryList, b -> b.getDescription().equals(cmbInvSelected));

        } else if (date != null && cmbWardSelected == null && cmbCategorySelected == null && cmbInvSelected == null) { // if only date is selected
            filteredData = new FilteredList<>(wardWiseInventoryList, b -> b.getLastModified().equals(date));

        } else if (cmbWardSelected != null && cmbCategorySelected != null && cmbInvSelected != null && date != null) { // if all are selected
            lblSelectedWard.setText(cmbWardSelected+" - "+ WardController.getWardTitle(cmbWardSelected));
            filteredData = new FilteredList<>(
                    wardWiseInventoryList,
                    b -> (b.getWardNo().equals(cmbWardSelected) && b.getCategory().equals(cmbCategorySelected)
                            && b.getInvNo().equals(cmbInvSelected) && b.getLastModified().equals(date)));

        } else if (cmbWard != null && cmbCategory != null) { // if only Ward and Category are selected
            lblSelectedWard.setText(cmbWardSelected+" - "+ WardController.getWardTitle(cmbWardSelected));
            filteredData = new FilteredList<>(
                    wardWiseInventoryList, a -> (a.getWardNo().equals(cmbWardSelected) && a.getCategory().equals(cmbCategorySelected)));

        } else if (cmbWard != null && cmbDescription != null) { // if only Ward and Inventory are selected
            lblSelectedWard.setText(cmbWardSelected+" - "+ WardController.getWardTitle(cmbWardSelected));
            filteredData = new FilteredList<>(
                    wardWiseInventoryList, a -> (a.getWardNo().equals(cmbWardSelected) && a.getInvNo().equals(cmbInvSelected)));

        } else if (cmbWard != null && date != null) { // if only Ward and Date are selected
            lblSelectedWard.setText(cmbWardSelected+" - "+ WardController.getWardTitle(cmbWardSelected));
            filteredData = new FilteredList<>(
                    wardWiseInventoryList, a -> (a.getWardNo().equals(cmbWardSelected) && a.getLastModified().equals(date)));
        }

        sortedData = new SortedList<>(filteredData);

        /**
         * Bind sorted result with tbl
         * */

        sortedData.comparatorProperty().bind(tblWardWiseInventory.comparatorProperty());

        /**
         * Remove duplicates from sortedData
         * */

        LinkedHashSet<InventoryWardWiseTM> hashSet = new LinkedHashSet<>(sortedData);
        uniqueList = new ArrayList<>(hashSet);

        /**
         * Apply filtered and sorted data to the tbl
         * */

        tblWardWiseInventory.setItems(FXCollections.observableArrayList(uniqueList));

    }

    private void clearFilters() throws SQLException, ClassNotFoundException {
        cmbCategory.getSelectionModel().clearSelection();
        cmbWard.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        txtSearch.clear();

        lblSelectedWard.setText("All Ward/Units");

        loadTable();
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        loadTable();
    }
}
