package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.FilterComboBoxEvent;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.model.Inventory;
import lk.ijse.im_system.model.InventoryWithWard;
import lk.ijse.im_system.view.table_model.Inventory_QtyTM;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class WardCondemnInventoryFormController extends FieldDataForm implements LoadTableEvent, FilterComboBoxEvent {

    //public AnchorPane contextPane;
    public StackPane contextPane;

    public TextField txtQtyWithWard;
    public Spinner<Integer> spinnerCondemnQTY;

    public TableView<Order_Condemn_InventoryTM> tblWardCondemnList;
    public TableColumn colCategory;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colQtyCondemn;

    public JFXDatePicker datePicker;

    public TableView<Inventory_QtyTM> tblBeforeAfterQty;
    public TableColumn colInventoryNo;
    public TableColumn colDescrip;
    public TableColumn colBeforeQty;
    public TableColumn colModifiedQty;
    public TableColumn colAfterQty;
    public TableColumn colLastModified;
    public TableColumn colCategoryTitle;


    String cmbCategorySelected;
    String cmbInventoryNoSelected;
    String cmbDescriptionSelected;
    Order_Condemn_InventoryTM invSelected;

    String categoryToFilter;
    String invToFilter;
    String date;

    String loggedWardNo = MainFormController.loggedWardNo;

    public void initialize() {

        try {
            initTable();
            loadCategory(); // cmbCategory
            loadCategoryForEditCmb(); // cmbCategory2
            loadDescriptionForFilter();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SpinnerValueFactory<Integer> qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerCondemnQTY.setValueFactory(qtyValueFactory);

        cmbInventoryNo.setDisable(true);
        cmbDescription.setDisable(true);

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbCategorySelected = newValue;

            try {
                filterComboBoxes(cmbCategorySelected);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        cmbInventoryNo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbInventoryNoSelected = newValue;

            try {
                setFieldsOnInvNo();
                //setFieldsOnInvNo(newValue, txtQtyWithWard);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        cmbDescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbDescriptionSelected = newValue;

            try {
                setFieldsOnDescrip();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblWardCondemnList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                invSelected = newValue;
            } else {
                //
            }
        });

        //---------------------------------Filter Combo Boxes--------------------------------------------------------------

        cmbCategory2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            categoryToFilter = newValue;

            try {
                loadCmbDescripByCategoryToFilter(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        cmbDescription2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            invToFilter = newValue;
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            date = String.valueOf(newValue);
        });

        //------------------------------------------------------------------------------------------------------------------
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        // Table 1

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyCondemn.setCellValueFactory(new PropertyValueFactory<>("modifiedQty"));

        // Table 2

        colInventoryNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescrip.setCellValueFactory(new PropertyValueFactory<>("description"));
        colBeforeQty.setCellValueFactory(new PropertyValueFactory<>("beforeQty"));
        colModifiedQty.setCellValueFactory(new PropertyValueFactory<>("modifiedQty"));
        colAfterQty.setCellValueFactory(new PropertyValueFactory<>("afterQty"));
        colLastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        colCategoryTitle.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadTable();
    }

    ObservableList<Inventory_QtyTM> qtyListWithCondemnQty = FXCollections.observableArrayList(); // list for table 2

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        qtyListWithCondemnQty = InventoryWithWardController.getCondemnInventoryDetailsWithQty(qtyListWithCondemnQty);
        tblBeforeAfterQty.setItems(qtyListWithCondemnQty);
    }

    @Override
    public void setFieldsOnInvNo() throws SQLException, ClassNotFoundException {

        int qtyWithWard = InventoryWithWardController.getQtyWithWardOfInv(loggedWardNo,cmbInventoryNoSelected);
        String description = InventoryController.getOnlyDescription(cmbInventoryNoSelected);

        if (qtyWithWard != 0 && description != null ) {
            cmbDescription.setValue(description);
            txtQtyWithWard.setText(String.valueOf(qtyWithWard));

        } else {
            new Alert(Alert.AlertType.WARNING,"Inventory Currently not Available at Ward.");
        }
    }

    @Override
    public void setFieldsOnDescrip() throws SQLException, ClassNotFoundException {

        String invNo = InventoryController.getOnlyInvNo(cmbDescriptionSelected);
        int qtyWithWard = InventoryWithWardController.getQtyWithWardOfInv(loggedWardNo,invNo);

        if (qtyWithWard != 0 && invNo != null ) {
            cmbInventoryNo.setValue(invNo);
            txtQtyWithWard.setText(String.valueOf(qtyWithWard));

        } else {
            new Alert(Alert.AlertType.WARNING,"Inventory Currently not Available at Ward.");
        }
    }

    ObservableList<Order_Condemn_InventoryTM> condemnList = FXCollections.observableArrayList();

    public void addToListOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String qtyOnStore = txtQtyWithWard.getText();
        int spinnerValue = Integer.parseInt(spinnerCondemnQTY.getEditor().getText());

        condemnList = addToList(cmbCategorySelected,cmbInventoryNoSelected,cmbDescriptionSelected,spinnerValue,qtyOnStore,condemnList);

        tblWardCondemnList.setItems(condemnList);
        clearFields();

        cmbInventoryNo.setDisable(true);
        cmbDescription.setDisable(true);
    }

    private void clearFields() throws SQLException, ClassNotFoundException {

        if (!cmbCategorySelected.equals("")) {
            cmbCategory.getSelectionModel().clearSelection();
        } else {
            cmbCategory.setValue("");
        }
        cmbInventoryNo.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        txtQtyWithWard.clear();
        spinnerCondemnQTY.getValueFactory().setValue(0);

    }

    public void exportListOnAction(ActionEvent actionEvent) {

        FileChooser fc = new FileChooser();

        fc.setTitle("Save Order List As");
        fc.setInitialFileName("Condemn_00");
        fc.setInitialDirectory(new File("/home"));
        //fc.setInitialDirectory(selectedFile.getParentFile());

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Documents","*.odt","*.doc"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        //File selectedFile = fc.showOpenDialog(null);
        File selectedFile = fc.showSaveDialog(null);

        if (selectedFile != null) {

            String filePath = selectedFile.getAbsolutePath();
            File file = new File(filePath);

            BufferedWriter bw = null;

            try {
                FileWriter fw = new FileWriter(file);
                bw = new BufferedWriter(fw);

                /**writes the data to the selected file*/

                for (Order_Condemn_InventoryTM otm : condemnList) {
                    String record = otm.getCategory()+","+otm.getInvNo()+","+otm.getDescription()+","+otm.getModifiedQty();
                    bw.newLine();
                    bw.write(record);
                }

                bw.flush();
                bw.close();
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //System.out.println("File is not valid");
        }
    }

    public void placeCondemnOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EmailForm.fxml"));
        Parent parent = loader.load();

        EmailFormController controller = loader.getController();
        controller.setEventFromWardCondemnInventory(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.initStyle(StageStyle.UNDECORATED);
        window.centerOnScreen();
        blurBackground(controller,window);
        window.show();
    }

    private void blurBackground(EmailFormController controller, Stage window) {
        BoxBlur blur = new BoxBlur(3,3,3);
        contextPane.setEffect(blur);
    }

    public void removeFromListOnAction(ActionEvent actionEvent) {
        condemnList.remove(invSelected);
        tblWardCondemnList.setItems(condemnList);
        tblWardCondemnList.refresh();
    }

    public void clearTableOnAction(ActionEvent actionEvent) {
        tblWardCondemnList.getItems().clear();
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        filterTable(tblBeforeAfterQty,qtyListWithCondemnQty,categoryToFilter,invToFilter,date);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        qtyListWithCondemnQty.clear();
        loadTable();
    }

    private void clearFilters() throws SQLException, ClassNotFoundException {
        cmbCategory2.getSelectionModel().clearSelection();
        cmbDescription2.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
        loadDescriptionForFilter();
    }
}
