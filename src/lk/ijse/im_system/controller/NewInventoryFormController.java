package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.LocationController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.model.Inventory;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class NewInventoryFormController extends FieldDataForm {

    public TextField txtInvNo;
    public TextField txtDescription;
    public Spinner<Integer> spinnerQTY;
    public Spinner<Integer> spinnerQTY2;
    public ComboBox<String> cmbLocation;
    public ComboBox<String> cmbCategory;

    public JFXButton btnCancel;

    private LoadTableEvent event;

    String loggedWardNo = MainFormController.loggedWardNo;

    public void initialize() {
        SpinnerValueFactory<Integer> qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerQTY.setValueFactory(qtyValueFactory);

        SpinnerValueFactory<Integer> qtyLevelValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerQTY2.setValueFactory(qtyLevelValueFactory);

        try {
            txtInvNo.setText(InventoryController.getNextID());

            if (loggedWardNo == null) {
               // loadLocation();
                loadLocationsOfStore();
            } else {
                loadLocationsOfWard(loggedWardNo);
            }

            loadCategory();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        txtDescription.setOnKeyReleased(event1 -> {
            validateField();
        });

    }

    boolean isValidDescription = false;

    private void validateField() {
        isValidDescription = Pattern.matches("^[A-Z][a-z/ ]+[A-Z][a-z]+|[A-Z][a-z]+$", this.txtDescription.getText());

        if (isValidDescription) {
            txtDescription.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            txtDescription.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }
    }

    public void saveInventoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        int spinnerValue = Integer.parseInt(spinnerQTY.getEditor().getText());
        int spinner2Value = Integer.parseInt(spinnerQTY2.getEditor().getText());

        String locationId = LocationController.getLocationIdByName(cmbLocation.getValue());
        String categoryId = CategoryController.getCategoryId(cmbCategory.getValue());

        Inventory inventory = new Inventory(txtInvNo.getText(),txtDescription.getText(), spinnerValue, categoryId, locationId, spinner2Value);

        if (isValidDescription) {

            if (!InventoryController.isInventoryExists(inventory)) {
                if (InventoryController.addInventory(inventory)) {
                    event.loadTable();

                    new Alert(Alert.AlertType.CONFIRMATION, "New Inventory Added Successfully.", ButtonType.OK).show();
                    clearFields();
                    txtInvNo.setText(InventoryController.getNextID());
                    txtDescription.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");

                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Save New Inventory.\nTry Again", ButtonType.OK).show();
                }

            } else {
                new Alert(Alert.AlertType.WARNING, "Description already exist with same name.\nTry another name", ButtonType.OK).show();
            }

        } else {
            new Alert(Alert.AlertType.WARNING,"Description not accepted.\n Try format(Abcd / Abcd Efgh)",ButtonType.OK).show();
        }
    }

    private void clearFields() {
        txtInvNo.clear();
        txtDescription.clear();
        spinnerQTY.getEditor().setText("0");
        cmbLocation.getSelectionModel().clearSelection();
        cmbCategory.getSelectionModel().clearSelection();
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setEvent(StoreInventoryFormController event) { // to catch  StoreInventoryController
        this.event = event;
    }
}
