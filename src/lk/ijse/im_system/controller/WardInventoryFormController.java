package lk.ijse.im_system.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.controller.util.DateTime;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.controller.util.Notification;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.view.table_model.InventoryInWardTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class WardInventoryFormController extends FieldDataForm implements LoadTableEvent {

    public Label lblInvNo;
    public Label lblDescription;
    public Label lblQtyOnHand;

    public TextField txtInvNo;
    public TextField txtDescription;
    public TextField txtQtyOnHand;

    public TableView<InventoryInWardTM> tblInventoryInWard;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colBeforeQty;
    public TableColumn colLastModified;
    public TableColumn colAfterQty;
    public TableColumn colLocation;
    public TableColumn colCategory;

    public TextField txtSearch;
    public Spinner<Integer> spinnerQTY;

    InventoryInWardTM inventorySelected;
    String cmbLocationSelected;
    String cmbCategorySelected;
    String cmbInventorySelected;
    String loggedWardNo = MainFormController.loggedWardNo;

    public void initialize() {

        try {
            initTable();
            loadCategory();
            loadDescription();
            loadLocationsOfWard(loggedWardNo);
            //loadLocationForEditCmb();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SpinnerValueFactory<Integer> qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerQTY.setValueFactory(qtyValueFactory);

        tblInventoryInWard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                //
            } else {
                inventorySelected = newValue;
            }
        });

        cmbLocation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbLocationSelected = newValue;
        });

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbCategorySelected = newValue;
        });

        cmbDescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbInventorySelected = newValue;
        });

        cmbLocation2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbLocationSelected = newValue;
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
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colBeforeQty.setCellValueFactory(new PropertyValueFactory<>("beforeQTY"));
        colLastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        colAfterQty.setCellValueFactory(new PropertyValueFactory<>("afterQTY"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadTable();
    }

    ObservableList<InventoryInWardTM> wardInventoryList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        tblInventoryInWard.getItems().clear();
        wardInventoryList.clear();
        wardInventoryList = InventoryWithWardController.getAllInventoryForWard(wardInventoryList,MainFormController.loggedWardNo);
        tblInventoryInWard.setItems(wardInventoryList);
    }

    public void addNewLocationOnAction(ActionEvent actionEvent) throws IOException {

        /**
         * should create a object of FXMLLoader without using the static class
         * */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewLocationForm.fxml"));
        Parent parent = loader.load();

        NewLocationFormController controller = loader.getController();
        controller.setEvent(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        //window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }

    public void loadDetailsToEditOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        txtInvNo.setText(inventorySelected.getInvNo());
        txtDescription.setText(inventorySelected.getDescription());
        txtQtyOnHand.setText(String.valueOf(inventorySelected.getAfterQTY()));
        cmbLocation.setValue(inventorySelected.getLocation());
        spinnerQTY.getEditor().setText(InventoryWithWardController.getNotifyLevel(inventorySelected));

        setDisableOn();
    }

    private void setDisableOn() {

        lblInvNo.setDisable(true);
        lblDescription.setDisable(true);
        lblQtyOnHand.setDisable(true);

        txtInvNo.setDisable(true);
        txtDescription.setDisable(true);
        txtQtyOnHand.setDisable(true);
    }

    private void setDisableOff() {

        lblInvNo.setDisable(false);
        lblDescription.setDisable(false);
        lblQtyOnHand.setDisable(false);

        txtInvNo.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (inventorySelected == null) {
            new Alert(Alert.AlertType.INFORMATION,"No Inventory Selected.").show();
            return;
        }

        String definedLevel = InventoryWithWardController.getNotifyLevel(inventorySelected);
        int spinnerValue = Integer.parseInt(spinnerQTY.getEditor().getText());

        if (txtInvNo.getText().equals("")) {
            new Alert(Alert.AlertType.INFORMATION,"No Inventory Selected.").show();
            return;

        } else if (/*cmbLocation.getValue().equals(null) ||*/ cmbLocation.getValue().equals(inventorySelected.getLocation()) && (spinnerValue == 0 || spinnerValue == Integer.parseInt(definedLevel))) {
            new Alert(Alert.AlertType.INFORMATION,"No changes have been done to update...").show();
            return;
        }

        ButtonType yes= new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no= new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (cmbLocation.getValue().equals(cmbLocationSelected) || spinnerValue != 0 || spinnerValue != Integer.parseInt(definedLevel)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save changes?", yes, no);
            alert.setTitle("Confirmation Alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {
                if (InventoryWithWardController.updateLocationInWard(txtInvNo.getText(),cmbLocationSelected,spinnerValue)) {
                    Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Inventory Details Updated Successfully!");
                    wardInventoryList.clear();
                    loadTable();
                    clearFields();
                    setDisableOff();

                } else {
                    new Alert(Alert.AlertType.INFORMATION,"Update Unsuccessful.Try Again...").show();
                }

            } else {
                //
            }
        }

    }

    private void clearFields() {
        txtInvNo.setText("");
        txtDescription.setText("");
        txtQtyOnHand.setText("");
        cmbLocation.setValue("");
        spinnerQTY.getEditor().setText("0");
    }

    ObservableList<InventoryInWardTM> filteredList = FXCollections.observableArrayList();

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbCategorySelected == null && cmbInventorySelected == null && cmbLocationSelected == null) {
            //loadTable();
            return;
        }

        tblInventoryInWard.getItems().clear();
        filteredList.clear();

        filterTableByCategory(cmbCategorySelected);
        filterTableByDescription(cmbInventorySelected);
        filterTableByLocation(cmbLocationSelected);

        LinkedHashSet<InventoryInWardTM> hashSet = new LinkedHashSet<InventoryInWardTM>(filteredList);
        ArrayList<InventoryInWardTM> uniqueList = new ArrayList<InventoryInWardTM>(hashSet);

        tblInventoryInWard.setItems(FXCollections.observableArrayList(uniqueList));
    }

    private void filterTableByCategory(String cmbCategorySelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryWithWardController.getFilteredListByCategoryForWard(loggedWardNo,wardInventoryList,cmbCategorySelected);
    }

    private void filterTableByDescription(String cmbInventorySelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryWithWardController.getFilteredListByInventoryForWard(loggedWardNo,wardInventoryList,cmbInventorySelected);
    }

    private void filterTableByLocation(String cmbLocationSelected) throws SQLException, ClassNotFoundException {
        filteredList = InventoryWithWardController.getFilteredListByLocationForWard(loggedWardNo,wardInventoryList,cmbLocationSelected);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        filteredList.clear();
        loadTable();
    }

    private void clearFilters() {
        cmbCategory.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        cmbLocation2.getSelectionModel().clearSelection();
        txtSearch.clear();
    }

    public void searchInventoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblInventoryInWard.getItems().clear();
        search(txtSearch.getText());
    }

    List<InventoryInWardTM> searchList = new ArrayList<>();

    private void search(String value) throws SQLException, ClassNotFoundException {

        if (filteredList.isEmpty()) {
            searchList = InventoryWithWardController.searchInventory(loggedWardNo, txtSearch.getText());
            tblInventoryInWard.setItems(FXCollections.observableArrayList(searchList));

        } else {
            filteredList.clear();
            filteredList = InventoryWithWardController.searchInventoryFromFilterList(loggedWardNo,txtSearch.getText(),filteredList);
            tblInventoryInWard.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    public void printSubmissionReportOnAction(ActionEvent actionEvent) {

        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/reports/InventorySubmissionReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            /**
             * Getting/Calculating values for Parameters in Report
             * */

            String wardNo = MainFormController.loggedWardNo;
            String wardTitle = WardController.getWardTitle(wardNo);
            String[] inchargeIdName = InchargeController.getInchargeId(wardNo);
            String inchargeId = inchargeIdName[0];
            String inchargeName = inchargeIdName[1];
            String dateFrom = InchargeController.getServiceStartDate(inchargeId);
            String dateTo = DateTime.date;
            String totalOrders = OrderController.getTotalOrders(wardNo,dateFrom,dateTo);
            String totalCondemns = CondemnController.getTotalCondemns(wardNo,dateFrom,dateTo);

            /**
             * Mapping Calculated Values with the Parameters in Report
             * */

            HashMap map = new HashMap();
            map.put("wardTitle",wardTitle);
            map.put("wardNo",wardNo);
            map.put("inchargeId",inchargeId);
            map.put("inchargeName",inchargeName);
            map.put("dateFrom",dateFrom);
            map.put("dateTo",dateTo);
            map.put("totalOrders",totalOrders);
            map.put("totalCondemns",totalCondemns);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
