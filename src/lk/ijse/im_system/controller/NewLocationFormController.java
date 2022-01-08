package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.LocationController;
import lk.ijse.im_system.controller.util.Validation;
import lk.ijse.im_system.model.Location;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class NewLocationFormController {

    public TextField txtLocationId;
    public TextField txtLocationName;

    public JFXButton btnCancel;

    private WardInventoryFormController event;
    private CategoryFormController event2;

    String loggedWardNo = MainFormController.loggedWardNo;

    public void initialize() {

        try {
            txtLocationId.setText(LocationController.getNextID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        txtLocationName.setOnKeyReleased(event1 -> {
            validateField();
        });
    }

    boolean isValidName = false;

    private void validateField() {

        isValidName = Pattern.matches("^[A-Z][a-z]{3,}[\\-]?[0-9]*$", txtLocationName.getText());

        if (isValidName) {
            txtLocationName.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            txtLocationName.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }
    }

    public void saveLocationOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (txtLocationName.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING,"No Location Name specified.",ButtonType.OK).show();
            return;
        }

        if (isValidName) {

            Location newLocation = new Location(txtLocationId.getText(),txtLocationName.getText(),loggedWardNo);

            if (!LocationController.isLocationExists(newLocation)) {

                if (LocationController.addLocation(newLocation)) {
                    //event.loadTable();
                    if (loggedWardNo == null) {
                        //event2.loadLocationsOfStore();
                    } else {
                        event.loadLocationsOfWard(loggedWardNo);
                        //event.loadLocationForEditCmb();
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "New Location Added Successfully.", ButtonType.OK).show();
                    clearFields();
                    txtLocationId.setText(LocationController.getNextID());
                    txtLocationId.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");

                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Save New Location.\nTry Again", ButtonType.OK).show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Location already exist with same name.\nTry another name", ButtonType.OK).show();
            }

        } else {
            new Alert(Alert.AlertType.WARNING,"Location Name not accepted.\n Try format(Abcd / Abcd-00)",ButtonType.OK).show();
        }
    }

    private void clearFields() {
        txtLocationId.clear();
        txtLocationName.setText("");
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setEvent(WardInventoryFormController event) { // to catch WardInventoryFormController to update its combo boxes
        this.event = event;
    }

    public void setEvent2(CategoryFormController event2) {
        this.event2 = event2;
    }
}
