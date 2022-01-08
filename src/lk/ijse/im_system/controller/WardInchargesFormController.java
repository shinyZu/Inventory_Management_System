package lk.ijse.im_system.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.view.table_model.Category_InventoryTM;
import lk.ijse.im_system.view.table_model.InventoryInWardTM;
import lk.ijse.im_system.view.table_model.InventoryTM;
import lk.ijse.im_system.view.table_model.Ward_InchargeDetailTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class WardInchargesFormController extends FieldDataForm implements LoadTableEvent {

    public TableView<Ward_InchargeDetailTM> tblWardDetail;
    public TableColumn colWardNo;
    public TableColumn colWardTitle;
    public TableColumn colExtensionNo;
    public TableColumn colEmail;
    public TableColumn colInchargeName;
    public TableColumn colInchargeId;

    public TextField txtSearch;

    String loggedWardNo = MainFormController.loggedWardNo;

    String cmbTitleSelected;
    String cmbWardNoSelected;
    String cmbInchargeNameSelected;

    public void initialize() {

        try {
            initTable();
            loadTitle();
            loadWardNo();
            loadInchargeNames();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

        cmbWardTitle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbTitleSelected = newValue;
        });

        cmbWard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbWardNoSelected = newValue;
        });

        cmbInchargeName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbInchargeNameSelected = newValue;
        });
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        colWardNo.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        colWardTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colExtensionNo.setCellValueFactory(new PropertyValueFactory<>("extensionNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colInchargeName.setCellValueFactory(new PropertyValueFactory<>("inchargeName"));
        colInchargeId.setCellValueFactory(new PropertyValueFactory<>("inchargeId"));

        loadTable();
    }

    ObservableList<Ward_InchargeDetailTM> wardDetailList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        tblWardDetail.getItems().clear();
        wardDetailList.clear();
        wardDetailList = WardController.getAllWardDetails(wardDetailList);
        tblWardDetail.setItems(wardDetailList);

    }

    public void addNewWardOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewWardForm.fxml"));
        Parent parent = loader.load();

        NewWardFormController controller = loader.getController();
        controller.setEvent(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }

    public void addNewInchargeOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewInchargeForm.fxml"));
        Parent parent = loader.load();

        NewInchargeFormController controller = loader.getController();
        controller.setEvent(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }

    public void searchInventoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblWardDetail.getItems().clear();
        search(txtSearch.getText());
    }

    ObservableList<Ward_InchargeDetailTM> filteredList = FXCollections.observableArrayList();
    List<Ward_InchargeDetailTM> searchList = new ArrayList<>();

    private void search(String value) throws SQLException, ClassNotFoundException {

        if (filteredList.isEmpty()) {
            searchList = WardController.searchWard(value);
            tblWardDetail.setItems(FXCollections.observableArrayList(searchList));

        } else {
            filteredList.clear();
            filteredList = WardController.searchWardFromFilterList(txtSearch.getText(),filteredList);
            tblWardDetail.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (cmbTitleSelected == null && cmbWardNoSelected == null && cmbInchargeNameSelected == null) {
            // loadTable();
            return;
        }

        tblWardDetail.getItems().clear();
        filteredList.clear();

        filterTableByTitle(cmbTitleSelected);
        filterTableByWardNo(cmbWardNoSelected);
        filterTableByIncharge(cmbInchargeNameSelected);

        LinkedHashSet<Ward_InchargeDetailTM> hashSet = new LinkedHashSet<Ward_InchargeDetailTM>(filteredList);
        ArrayList<Ward_InchargeDetailTM> uniqueList = new ArrayList<Ward_InchargeDetailTM>(hashSet);

        tblWardDetail.setItems(FXCollections.observableArrayList(uniqueList));

    }

    private void filterTableByTitle(String cmbTitleSelected) throws SQLException, ClassNotFoundException {
        filteredList = WardController.getFilteredListByTitle(wardDetailList,cmbTitleSelected);
    }

    private void filterTableByWardNo(String cmbWardNoSelected) throws SQLException, ClassNotFoundException {
        filteredList = WardController.getFilteredListByWardNo(wardDetailList,cmbWardNoSelected);
    }

    private void filterTableByIncharge(String cmbInchargeNameSelected) throws SQLException, ClassNotFoundException {
        filteredList = WardController.getFilteredListByIncharge(wardDetailList,cmbInchargeNameSelected);
    }


    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        filteredList.clear();
        loadTable();
    }

    private void clearFilters() {
        cmbWardTitle.getSelectionModel().clearSelection();
        cmbWard.getSelectionModel().clearSelection();
        cmbInchargeName.getSelectionModel().clearSelection();
        txtSearch.clear();
    }
}
