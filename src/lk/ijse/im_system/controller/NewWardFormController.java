package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.controller_db.InchargeController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.*;
import lk.ijse.im_system.model.Incharge;
import lk.ijse.im_system.model.InchargeHistory;
import lk.ijse.im_system.model.Ward;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class NewWardFormController extends FieldDataForm implements Validation {

    public TextField txtWardNo;
    public TextField txtWardTitle;
    public TextField txtExtensionNo;
    public TextField txtInchargeId;
    public TextField txtInchargeName;

    public JFXButton btnCancel;

    private WardInchargesFormController event;

    public void initialize(){

        try {
            setNextInchargeId();
            setNextWardNo();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mapValidations();
    }

    LinkedHashMap<TextField, Pattern> mapNewWardDetails = new LinkedHashMap<>();

    String wardTitleRegEx = "^[A-Z][a-z/ ]+[A-Z][a-z/ ]+|[A-Z][a-z]*$";
    String extensionRegEx = "^[0-9]{4,}$";
    String inchargeNameRegEx = "^[A-Z][a-z/ ]{4,}[A-Z][a-z]{4,}|[A-Z][a-z]{4,}$";

    Pattern titlePtn = Pattern.compile(wardTitleRegEx);
    Pattern extensionPtn = Pattern.compile(extensionRegEx);
    Pattern namePtn = Pattern.compile(inchargeNameRegEx);

    @Override
    public void mapValidations(){
        mapNewWardDetails.put(txtWardTitle,titlePtn);
        mapNewWardDetails.put(txtExtensionNo,extensionPtn);
        mapNewWardDetails.put(txtInchargeName,namePtn);
    }

    public void validateFieldOnKeyRelease(KeyEvent keyEvent) {

        Object response = validateFormFields(mapNewWardDetails);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField invalidField = (TextField) response;
                invalidField.requestFocus();

            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION,"success").showAndWait();
                setStyleToInitial();
            }
        }
    }

    /*private Object validateFormFields(LinkedHashMap<TextField, Pattern> mapNewWardDetails) {

        for (TextField keyTextField : mapNewWardDetails.keySet()) {
            Pattern valuePatten = mapNewWardDetails.get(keyTextField);

            if (!valuePatten.matcher(keyTextField.getText()).matches()) { // if the inserted text doesn't match with pattern
                if (!keyTextField.getText().isEmpty()) {
                    keyTextField.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
                }
                return keyTextField;

            }
            keyTextField.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");

        }
        return true;

    }*/

    private void setStyleToInitial() {
        for (TextField keyTextField : mapNewWardDetails.keySet()) {
            keyTextField.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");
        }
    }

    public void saveWardOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Ward newWard = new Ward(
                txtWardNo.getText(),
                txtWardTitle.getText(),
                txtExtensionNo.getText(),
                txtInchargeId.getText()
        );

        Incharge newIncharge = new Incharge(
                txtInchargeId.getText(),
                txtInchargeName.getText()
        );

        InchargeHistory history = new InchargeHistory(
                txtWardNo.getText(),
                txtInchargeId.getText(),
                DateTime.date,
                null
        );

        if (InchargeController.addNewWardWithIncharge(newWard,newIncharge,history)) {
            new Alert(Alert.AlertType.CONFIRMATION,"Ward added successfully.", ButtonType.OK).show();
            event.loadTable();
            clearFields();
            setNextInchargeId();
            setStyleToInitial();

        } else {
            new Alert(Alert.AlertType.WARNING,"Failed to Save New Ward.\nTry Again", ButtonType.OK).show();
        }

    }

    private void setNextInchargeId() throws SQLException, ClassNotFoundException {
        txtInchargeId.setText(InchargeController.getNextID());
    }

    private void setNextWardNo() throws SQLException, ClassNotFoundException {
        txtWardNo.setText(WardController.getNextWardNo());
    }


    private void clearFields() {
        txtWardNo.clear();
        txtWardTitle.clear();
        txtExtensionNo.clear();
        txtInchargeName.clear();
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setEvent(WardInchargesFormController event) {
        this.event = event;
    }

}
