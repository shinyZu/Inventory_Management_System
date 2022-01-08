package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.LocationController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.controller.util.Notification;
import lk.ijse.im_system.model.Inventory;
import lk.ijse.im_system.view.table_model.InventoryTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class StoreInventoryFormController extends FieldDataForm implements LoadTableEvent {

    public TableView<InventoryTM> tblInventory;
    public TableColumn colInventoryNo;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colLocation;
    public TableColumn colCategory;

    public Label lblInvNo;
    public Label lblDescription;
    public Label lblQtyOnHand;

    public TextField txtInvNo;
    public TextField txtDescription;
    public TextField txtQtyOnHand;

    public JFXCheckBox chkBoxAddQty;
    public JFXCheckBox chkBoxRemoveQty;
    public Spinner<Integer> spinnerQTY;
    public Spinner<Integer> spinnerQTY2;

    InventoryTM inventorySelected;
    String cmbCategorySelected;
    String cmbInventorySelected;
    String cmbLocationSelected;
    String cmbLocationSelectedForEdit;

    SpinnerValueFactory<Integer> qtyValueFactory = null;
    SpinnerValueFactory<Integer> qtyLevelValueFactory = null;

    public void initialize()  {
        try {
            initTable();
            loadDescription();
            loadLocationsOfStore();
            loadLocationForEditCmb();
            loadCategory();
            loadCategoryForEditCmb();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerQTY.setValueFactory(qtyValueFactory);

        qtyLevelValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerQTY2.setValueFactory(qtyLevelValueFactory);

        tblInventory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                //
            } else {
                inventorySelected = newValue;
            }
        });

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbCategorySelected = newValue;
        });

        cmbDescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbInventorySelected = newValue;
        });

        cmbLocation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbLocationSelected = newValue;
        });

        cmbLocation2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbLocationSelectedForEdit = newValue;
        });
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        colInventoryNo.setCellValueFactory(new PropertyValueFactory<>("inventoryNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadTable();
    }

    ObservableList<InventoryTM> inventoryList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException { // loads the table with all Inventory in the Store
        tblInventory.getItems().clear();
        inventoryList = InventoryController.getAllInventoryWithCategory(inventoryList);
        tblInventory.setItems(inventoryList);
    }

    public void addNewInventoryOnAction(ActionEvent actionEvent) throws IOException {
        /**
         * should create a object of FXMLLoader without using the static class in order to catch the relevant Controller
         * */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewInventoryForm.fxml"));
        Parent parent = loader.load();

        NewInventoryFormController controller = loader.getController();
        controller.setEvent(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        int newQtyOnHand = 0;
        int qtyOnHand = 0;
        int spinnerValue = Integer.parseInt(spinnerQTY.getEditor().getText());
        int spinnerValue2 = Integer.parseInt(spinnerQTY2.getEditor().getText());

        ButtonType yes= new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no= new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (!txtInvNo.getText().equals("")) {
            qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        } else if (txtInvNo.getText().equals("")) {
            new Alert(Alert.AlertType.INFORMATION,"No Inventory Selected").show();
            return;
        }

        if (/*spinnerQTY.getValue() == 0 && */spinnerValue == 0 && spinnerValue2 == Integer.parseInt(InventoryController.getNotifyLevel(inventorySelected)) && cmbLocation2.getValue().equals(inventorySelected.getLocation()) && cmbCategory2.getValue().equals(inventorySelected.getCategory())) {

            new Alert(Alert.AlertType.INFORMATION, "No changes have been done to Update", ButtonType.OK).show();
            //Notification.showInfoMsg("lk/ijse/im_system/view/assets/images/info.png","Please Select an Option To Change Quantity");

        }else if (spinnerValue != 0 && !(chkBoxAddQty.isSelected() || chkBoxRemoveQty.isSelected())) {

            new Alert(Alert.AlertType.INFORMATION, "Please Select an Option To Change Quantity", ButtonType.OK).show();
            //Notification.showInfoMsg("lk/ijse/im_system/view/assets/images/info.png","Please Select an Option To Change Quantity");

        }else if (spinnerValue != 0 && (chkBoxAddQty.isSelected() || chkBoxRemoveQty.isSelected())) { // if qtyOnHand is changed

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save changes?", yes, no);
            alert.setTitle("Confirmation Alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {

                if (chkBoxAddQty.isSelected()) {
                    newQtyOnHand = qtyOnHand + spinnerValue;

                } else if (chkBoxRemoveQty.isSelected()) {
                    newQtyOnHand = qtyOnHand - spinnerValue;
                }

                Inventory invDetailsForUpdate = new Inventory(
                        txtInvNo.getText(),
                        txtDescription.getText(),
                        newQtyOnHand,
                        CategoryController.getCategoryId(cmbCategory2.getValue()),
                        LocationController.getLocationIdByName(cmbLocation2.getValue()),
                        spinnerValue2

                );

                if (InventoryController.updateInventory(invDetailsForUpdate)) {
                    inventoryList.clear();
                    loadTable();
                    clearFields();
                    new Alert(Alert.AlertType.CONFIRMATION, "Inventory Details Updated Successfully!", ButtonType.OK).show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed To Update.Try Again...", ButtonType.OK).show();
                }
            } else {
                //
            }

        } else if (spinnerValue == 0 && (!chkBoxAddQty.isSelected() || !chkBoxRemoveQty.isSelected())){ // if edited either Category, Location or Notify Level details

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save changes?", yes, no);
            alert.setTitle("Confirmation Alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {

                Inventory invDetailsForUpdate = new Inventory(
                        txtInvNo.getText(),
                        txtDescription.getText(),
                        qtyOnHand,
                        CategoryController.getCategoryId(cmbCategory2.getValue()),
                        LocationController.getLocationIdByName(cmbLocation2.getValue()),
                        spinnerValue2
                );

                if (InventoryController.updateInventory(invDetailsForUpdate)) {
                    loadTable();
                    clearFields();
                    //new Alert(Alert.AlertType.CONFIRMATION, "Inventory Details Updated Successfully!", ButtonType.OK).show();
                    Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Inventory Details Updated Successfully!");

                } else {
                    //new Alert(Alert.AlertType.WARNING, "Failed To Update.Try Again...", ButtonType.OK).show();
                    Notification.showAlertMsg("lk/ijse/im_system/view/assets/images/alert_glass.png","Failed To Update.Try Again...");
                }
            } else {
                //
            }
        }
    }

    public void removeInventoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ButtonType yes= new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no= new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Delete this Inventory?", yes, no);
        alert.setTitle("Confirmation Alert");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {

            String invIdForDelete = inventorySelected.getInventoryNo();

            if (InventoryWithWardController.deleteInventory(invIdForDelete)) {
                loadTable();
                Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Inventory Deleted Successfully!");

            } else {
                Notification.showAlertMsg("lk/ijse/im_system/view/assets/images/alert_glass.png","Failed To Delete Inventory.Try Again...");
            }
        }

    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (cmbCategorySelected == null && cmbInventorySelected == null && cmbLocationSelected == null) {
            // loadTable();
            return;
        }

        tblInventory.getItems().clear();
        filteredList.clear();

        filterTableByCategory(cmbCategorySelected);
        filterTableByDescription(cmbInventorySelected);
        filterTableByLocation(cmbLocationSelected);

        LinkedHashSet<InventoryTM> hashSet = new LinkedHashSet<InventoryTM>(filteredList);
        ArrayList<InventoryTM> uniqueList = new ArrayList<InventoryTM>(hashSet);

        tblInventory.setItems(FXCollections.observableArrayList(uniqueList));
    }

    ObservableList<InventoryTM> filteredList = FXCollections.observableArrayList();

    private void filterTableByCategory(String cmbCategorySelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryController.getFilteredListByCategory(inventoryList,cmbCategorySelected);
    }

    private void filterTableByDescription(String cmbInventorySelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryController.getFilteredListByInventory(inventoryList,cmbInventorySelected);
    }

    private void filterTableByLocation(String cmbLocationSelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryController.getFilteredListByLocation(inventoryList,cmbLocationSelected);
    }

    private void enableCmbBox() {
        cmbCategory.setDisable(false);
        cmbDescription.setDisable(false);
        cmbLocation.setDisable(false);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        filteredList.clear();
        loadTable();
    }

    private void clearFilters() throws SQLException, ClassNotFoundException {
        cmbCategory.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        cmbLocation.getSelectionModel().clearSelection();
    }

    public void loadDetailsToEditOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        txtInvNo.setText(inventorySelected.getInventoryNo());
        txtDescription.setText(inventorySelected.getDescription());
        txtQtyOnHand.setText(String.valueOf(inventorySelected.getQtyOnHand()));
        cmbLocation2.setValue(inventorySelected.getLocation());
        cmbCategory2.setValue(inventorySelected.getCategory());
        spinnerQTY2.getEditor().setText(InventoryController.getNotifyLevel(inventorySelected));

        setDisableOn();
    }

    private void setDisableOff(){
        lblInvNo.setDisable(false);
        lblDescription.setDisable(false);
        lblQtyOnHand.setDisable(false);

        txtInvNo.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
    }

    private void setDisableOn(){

        lblInvNo.setDisable(true);
        lblDescription.setDisable(true);
        lblQtyOnHand.setDisable(true);

        txtInvNo.setDisable(true);
        txtDescription.setDisable(true);
        txtQtyOnHand.setDisable(true);

    }

    private void clearFields(){

        setDisableOff();
        txtInvNo.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        //spinnerQTY.decrement(spinnerQTY.getValue());
        spinnerQTY.getEditor().setText("0");
        cmbLocation2.getSelectionModel().clearSelection();
        cmbCategory2.getSelectionModel().clearSelection();
        spinnerQTY2.getEditor().setText("0");

        chkBoxAddQty.setSelected(false);
        chkBoxRemoveQty.setSelected(false);
    }

    public void clearFieldsOnAction(ActionEvent actionEvent) {
        clearFields();
    }
}
