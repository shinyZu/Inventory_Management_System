package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.InchargeController;
import lk.ijse.im_system.model.Incharge;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class NewInchargeFormController {

    public TextField txtInchargeId;
    public TextField txtInchargeName;
    public JFXButton btnCancel;

    private WardInchargesFormController event;

    public void initialize() {

        try {
            setNextInchargeId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        txtInchargeName.setOnKeyReleased(event1 -> {
            validateField();
        });
    }

    boolean isValidName = false;

    private void validateField() {
        isValidName = Pattern.matches("^[A-Z][a-z/ ]{1,}[A-Z][a-z]{1,}|[A-Z][a-z]{1,}$", txtInchargeName.getText());

        if (isValidName) {
            txtInchargeName.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            txtInchargeName.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }
    }

    private void setNextInchargeId() throws SQLException, ClassNotFoundException {
        txtInchargeId.setText(InchargeController.getNextID());
    }

    public void setEvent(WardInchargesFormController event) {
        this.event = event;
    }

    public void saveInchargeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Incharge newIncharge = new Incharge(
                txtInchargeId.getText(),
                txtInchargeName.getText()
        );

        if (InchargeController.addNewInchargeWithoutWard(newIncharge)) {
            new Alert(Alert.AlertType.CONFIRMATION,"New In-charge added successfully.", ButtonType.OK).show();
            clearFields();
            setNextInchargeId();
            txtInchargeName.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");

        } else {
            new Alert(Alert.AlertType.WARNING,"Failed to add new In-charge.\nTry again...", ButtonType.OK).show();
        }
    }

    private void clearFields() {
        txtInchargeName.clear();
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
