package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.im_system.view.table_model.Inventory_QtyTM;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class WardOrderFormController extends FieldDataForm implements LoadTableEvent, FilterComboBoxEvent {

    public StackPane contextPane;
    public TextField txtQtyOnHand;
    public Spinner<Integer> spinnerOrderQTY;
    public JFXButton btnAddToList;
    public JFXButton btnPlaceOrder;

    public TableView<Order_Condemn_InventoryTM> tblWardOrderList; // tbl 1
    public TableColumn colCategory;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colQtyOrdered;

    public JFXDatePicker datePicker;
    
    public TableView<Inventory_QtyTM> tblBeforeAfterQty; //tbl2
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

    public void initialize() {
        try {
            loadCategory(); // cmbCategory
            loadCategoryForEditCmb(); // cmbCategory2
            loadDescriptionForFilter(); // cmbDescription2 in Filter pane
            initTable();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SpinnerValueFactory<Integer> qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerOrderQTY.setValueFactory(qtyValueFactory);

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
                //setFieldsOnInvNo(newValue,txtQtyOnHand);

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

        tblWardOrderList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                //
            } else {
                invSelected = newValue;
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
        colQtyOrdered.setCellValueFactory(new PropertyValueFactory<>("modifiedQty"));

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

    ObservableList<Inventory_QtyTM> qtyListWithOrderQty = FXCollections.observableArrayList(); // list for table 2

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException { // load tbl 2
        qtyListWithOrderQty = InventoryWithWardController.getOrderInventoryDetailsWithQty(qtyListWithOrderQty);
        tblBeforeAfterQty.setItems(qtyListWithOrderQty);
    }

    @Override
    public void setFieldsOnInvNo() throws SQLException, ClassNotFoundException {
        Inventory inventory = InventoryController.getInventoryOnInvNo(cmbInventoryNoSelected);

        if (inventory == null) {
            new Alert(Alert.AlertType.WARNING,"Inventory currently not available in Store.");

        } else {
            cmbDescription.setValue(inventory.getDescription());
            txtQtyOnHand.setText(String.valueOf(inventory.getQtyOnHand()));
        }
    }

    @Override
    public void setFieldsOnDescrip() throws SQLException, ClassNotFoundException {
        Inventory inventory = InventoryController.getInventoryOnDescrip(cmbDescriptionSelected);

        if (inventory == null) {
            new Alert(Alert.AlertType.WARNING,"Inventory Currently not Available at Store.");

        } else {
            cmbInventoryNo.setValue(inventory.getInventoryNo());
            txtQtyOnHand.setText(String.valueOf(inventory.getQtyOnHand()));
        }
    }

    ObservableList<Order_Condemn_InventoryTM> orderList = FXCollections.observableArrayList();

    public void addToListOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String qtyOnStore = txtQtyOnHand.getText();
        int spinnerValue = Integer.parseInt(spinnerOrderQTY.getEditor().getText());

        orderList = addToList(cmbCategorySelected,cmbInventoryNoSelected,cmbDescriptionSelected,spinnerValue,qtyOnStore,orderList);

        tblWardOrderList.setItems(orderList);
        clearFields();

        cmbInventoryNo.setDisable(true);
        cmbDescription.setDisable(true);
    }

    private void clearFields() {

        if (!cmbCategorySelected.equals("")) {
            cmbCategory.getSelectionModel().clearSelection();
        } else {
            cmbCategory.setValue("");
        }
        cmbInventoryNo.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        txtQtyOnHand.clear();
        //spinnerOrderQTY.getValueFactory().setValue(0);
        spinnerOrderQTY.getEditor().setText("0");

    }

    public void removeFromListOnAction(ActionEvent actionEvent) {
        orderList.remove(invSelected);
        tblWardOrderList.setItems(orderList);
        tblWardOrderList.refresh();
    }

    public void exportListOnAction(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();

        fc.setTitle("Save Order List As");
        fc.setInitialFileName("Order_00");
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

                for (Order_Condemn_InventoryTM otm : orderList) {
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

    public void placeOrderOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EmailForm.fxml"));
        Parent parent = loader.load();

        EmailFormController controller = loader.getController();
        controller.setEvent(this);

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

    public void clearTableOnAction(ActionEvent actionEvent) {
        tblWardOrderList.getItems().clear();
        //tblWardOrderList.setStyle("-fx-background-color:  #2c3e50; -fx-background-radius: 10;");
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        filterTable(tblBeforeAfterQty,qtyListWithOrderQty,categoryToFilter,invToFilter,date);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        qtyListWithOrderQty.clear();
        loadTable();

    }

    private void clearFilters() throws SQLException, ClassNotFoundException {
        cmbCategory2.getSelectionModel().clearSelection();
        cmbDescription2.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
        loadDescriptionForFilter();
    }
}
