package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.InchargeController;
import lk.ijse.im_system.controller.controller_db.UserLoginController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.Notification;
import lk.ijse.im_system.controller.util.User;
import lk.ijse.im_system.controller.util.Validation;
import lk.ijse.im_system.model.InchargeLogin;
import lk.ijse.im_system.model.StorekeeperLogin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class MainFormController implements Validation {

    public StackPane rootPane;
    public AnchorPane anchorPane;

    public static String wardNo;
    public Pane loginPane;

    public JFXButton btnIncharge;
    public JFXButton btnStorekeeper;

    public JFXTextField txtUsername;
    public JFXTextField txtPassword;
    public JFXPasswordField fieldPassword;
    public JFXTextField txtEmail;
    public JFXComboBox<String> cmbWard;
    public JFXComboBox<String> cmbInchargeId;

    public MaterialDesignIconView iconHidePassword;
    public MaterialDesignIconView iconShowPassword;
    public MaterialDesignIconView iconMail;

    public Label lblUsernameError;
    public Label lblPasswordError;
    public Label lblEmailError;
    public Label lblEmailPasswordError;

    public JFXTextField txtEmailPassword;
    public JFXPasswordField fieldEmailPassword;
    public MaterialDesignIconView iconHidePassword1;
    public MaterialDesignIconView iconShowPassword1;

    public JFXButton btnSignUp;
    public JFXButton btnLogin;

    public Label lblRegistered;
    public Label lblNewUser;
    public Label lblLogin;
    public Label lblSignUp;

    public String userRole = "Ward In-charge";
    public String wardSelected;
    public String inchargeIdSelected;

    public static String loggedWardNo = null;
    public static String storeMail = "invms1141@gmail.com";
    public static String storeMailPwd = "ims1141@ijse";

    KeyEvent event;

    public void initialize() {

        try {
            loadWardNo();
            loadInchargeId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        btnSignUp.setVisible(false);

        txtPassword.setVisible(false);
        iconHidePassword.setVisible(false);
        lblRegistered.setVisible(false);
        lblLogin.setVisible(false);

        txtEmail.setVisible(false);
        iconMail.setVisible(false);

        txtEmailPassword.setVisible(false);
        fieldEmailPassword.setVisible(false);
        iconShowPassword1.setVisible(false);
        iconHidePassword1.setVisible(false);

        cmbWard.setVisible(false);
        cmbInchargeId.setVisible(false);

        lblUsernameError.setVisible(false);
        lblPasswordError.setVisible(false);
        lblEmailError.setVisible(false);
        lblEmailPasswordError.setVisible(false);

        cmbWard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            wardSelected = newValue;
        });

        cmbInchargeId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            inchargeIdSelected = newValue;
        });

        txtUsername.requestFocus();

        txtPassword.setOnKeyReleased(event -> {
            this.event = event;
            fieldPassword.setText(txtPassword.getText());
            validateFieldOnKeyRelease(event);
            validateHiddenTXTUserPwdField();
        });

        txtEmailPassword.setOnKeyReleased(event -> {
            fieldEmailPassword.setText(txtEmailPassword.getText());
            validateFieldOnKeyRelease(event);
            validateHiddenEmailPwdField();
        });

        mapValidations();
    }

    private void validateHiddenEmailPwdField() {

        boolean pwdMatches = Pattern.matches(wardEmailPwd, txtEmailPassword.getText());

        txtEmailPassword.getStylesheets().clear();

        if (pwdMatches) {
            txtEmailPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/validInput.css");

        } else {
            txtEmailPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/invalidInput.css");
            fieldEmailPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/invalidInput.css");
        }
    }

    private void validateHiddenTXTUserPwdField() {
        boolean pwdMatches = Pattern.matches(userPwdRegEx, txtPassword.getText());

        txtPassword.getStylesheets().clear();

        if (pwdMatches) {
            txtPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/validInput.css");
            txtEmail.requestFocus();
        } else {
            txtPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/invalidInput.css");
            fieldPassword.getStylesheets().add("lk/ijse/im_system/view/assets/styles/invalidInput.css");
        }
    }

    List<String> cmbInchargeIdList = new ArrayList<>();
    private void loadInchargeId() throws SQLException, ClassNotFoundException {
        cmbInchargeIdList = InchargeController.getNewInchargeId(cmbInchargeIdList);

        if (WardController.currentInchargeId != null) {
            cmbInchargeIdList.remove(WardController.currentInchargeId);
        }

        cmbInchargeId.getItems().addAll(cmbInchargeIdList);
    }

    List<String> cmbWardNoList = new ArrayList<>();
    private void loadWardNo() throws SQLException, ClassNotFoundException {
        cmbWardNoList = WardController.getAllWardNo(cmbWardNoList);
        cmbWard.getItems().addAll(cmbWardNoList);
    }

    public void goToStorekeeperLoginFormOnAction(ActionEvent actionEvent) throws IOException {

        btnStorekeeper.setStyle("-fx-background-color: #22577A; -fx-background-radius: 0 10 10 0;");
        btnIncharge.setStyle("-fx-background-color: #999; -fx-background-radius: 10 0 0 10;");

        userRole = btnStorekeeper.getText();

        if (btnSignUp.isVisible()) {
            cmbWard.setVisible(false);
            cmbInchargeId.setVisible(false);
        }
        clearFields();
        setFieldsToInitialState();
    }

    public void goToInchargeLoginFormOnAction(ActionEvent actionEvent) {

        btnIncharge.setStyle("-fx-background-color: #22577A; -fx-background-radius: 10 0 0 10;");
        btnStorekeeper.setStyle("-fx-background-color: #999; -fx-background-radius: 0 10 10 0;");

        userRole = btnIncharge.getText();

        if (btnSignUp.isVisible()) {
            cmbWard.setVisible(true);
            cmbInchargeId.setVisible(true);
        }
        clearFields();
        setFieldsToInitialState();
    }

    public void showPasswordOnAction(MouseEvent mouseEvent) {

        iconShowPassword.setVisible(false);
        iconHidePassword.setVisible(true);

        txtPassword.setText(fieldPassword.getText());
        txtPassword.setVisible(true);
        txtPassword.setLabelFloat(true);

        fieldPassword.setLabelFloat(false);

        validateHiddenTXTUserPwdField();

        fieldPassword.setVisible(false);
        fieldPassword.toBack();
        fieldPassword.clear();
    }

    public void hidePasswordOnAction(MouseEvent mouseEvent) {

        iconHidePassword.setVisible(false);
        iconShowPassword.setVisible(true);

        fieldPassword.setVisible(true);
        fieldPassword.setText(txtPassword.getText());
        //validateFieldOnKeyRelease(event);
        fieldPassword.setLabelFloat(true);

        txtPassword.setVisible(false);
    }

    public void showEmailPasswordOnAction(MouseEvent mouseEvent) {

        iconShowPassword1.setVisible(false);
        iconHidePassword1.setVisible(true);

        txtEmailPassword.setText(fieldEmailPassword.getText());
        txtEmailPassword.setVisible(true);
        txtEmailPassword.setLabelFloat(true);

        fieldEmailPassword.setLabelFloat(false);
        fieldEmailPassword.setVisible(false);
        fieldEmailPassword.toBack();
        fieldEmailPassword.clear();
    }

    public void hideEmailPasswordOnAction(MouseEvent mouseEvent) {
        iconHidePassword1.setVisible(false);
        iconShowPassword1.setVisible(true);

        fieldEmailPassword.setVisible(true);
        fieldEmailPassword.setText(txtEmailPassword.getText());
        fieldEmailPassword.setLabelFloat(true);

        txtEmailPassword.setVisible(false);
    }

    public void displayLoginBtnOnAction(MouseEvent mouseEvent) {

        txtUsername.requestFocus();

        btnLogin.setVisible(true);
        btnSignUp.setVisible(false);

        lblNewUser.setVisible(true);
        lblSignUp.setVisible(true);
        lblLogin.setVisible(false);
        lblRegistered.setVisible(false);

        txtEmail.setVisible(false);
        iconMail.setVisible(false);

        cmbWard.setVisible(false);
        cmbInchargeId.setVisible(false);

        txtEmailPassword.setVisible(false);
        fieldEmailPassword.setVisible(false);
        iconShowPassword1.setVisible(false);
        iconHidePassword1.setVisible(false);

        clearFields();
        setFieldsToInitialState();
    }

    public void displaySignUpBtnOnAction(MouseEvent mouseEvent) {

        txtUsername.requestFocus();

        btnSignUp.setVisible(true);
        btnLogin.setVisible(false);

        lblRegistered.setVisible(true);
        lblLogin.setVisible(true);
        lblNewUser.setVisible(false);
        lblSignUp.setVisible(false);

        txtEmail.setVisible(true);
        iconMail.setVisible(true);

        //txtEmailPassword.setVisible(true);
        fieldEmailPassword.setVisible(true);
        iconShowPassword1.setVisible(true);
        iconHidePassword1.setVisible(true);

        if (userRole.equals("Ward In-charge")) {
            cmbWard.setVisible(true);
            cmbInchargeId.setVisible(true);
        }

        clearFields();
        setFieldsToInitialState();
    }

    private void setFieldsToInitialState() {
        txtUsername.getStylesheets().clear();
        txtPassword.getStylesheets().clear();
        fieldPassword.getStylesheets().clear();
        txtEmail.getStylesheets().clear();
        txtEmailPassword.getStylesheets().clear();
        fieldEmailPassword.getStylesheets().clear();

    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        fieldPassword.clear();

        txtEmail.clear();
        txtEmailPassword.clear();
        fieldEmailPassword.clear();

        cmbWard.getSelectionModel().clearSelection();
        cmbInchargeId.getSelectionModel().clearSelection();
    }

    LinkedHashMap<TextField,Pattern> mapSignUp = new LinkedHashMap<>();
    LinkedHashMap<TextField,Pattern> mapLogin = new LinkedHashMap<>();

    String userNameRegEx = "^[A-Z][a-z]*[0-9]{1,4}$"; // Incharge1, Storekeeper1
    String userPwdRegEx = "^[A-z]*[0-9]{2}$"; // thkWard01, thkSK01
    String wardEmail = "^[A-z|0-9]{2,}@(gmail)(.com|.lk)$";
    String wardEmailPwd = "^[a-z0-9@]{8,12}$";  // cannot use uppercase

    Pattern userNamePtn = Pattern.compile(userNameRegEx);
    Pattern userPwdPtn = Pattern.compile(userPwdRegEx);
    Pattern wardEmailPtn = Pattern.compile(wardEmail);
    Pattern wardEmailPwdPtn = Pattern.compile(wardEmailPwd);

    @Override
    public void mapValidations() {

        mapLogin.put(txtUsername,userNamePtn);
        mapLogin.put(fieldPassword,userPwdPtn);
        //mapLogin.put(txtPassword,userPwdPtn);

        mapSignUp.put(txtUsername,userNamePtn);
        mapSignUp.put(fieldPassword,userPwdPtn);
        //mapSignUp.put(txtPassword,userPwdPtn);
        mapSignUp.put(txtEmail,wardEmailPtn);
        mapSignUp.put(fieldEmailPassword,wardEmailPwdPtn);
        //mapSignUp.put(txtEmailPassword,wardEmailPwdPtn);
    }

    public void validateFieldOnKeyRelease(KeyEvent keyEvent) {

        Object response = null;

        if (btnLogin.isVisible()) {
            response = validateFields(mapLogin);

        } else {
            response = validateFields(mapSignUp);
        }

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField invalidField = (TextField) response;
                invalidField.requestFocus();

            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION,"success").showAndWait();
            }
        }
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Stage window = new Stage();

        String username = txtUsername.getText();
        String tPassword = txtPassword.getText();
        String fPassword = fieldPassword.getText();

        if (userRole.equals("Ward In-charge")) {

            if (UserLoginController.verifyIncharge(username,tPassword,fPassword)) {

                loggedWardNo = UserLoginController.getWardNo(username);
                System.out.println(loggedWardNo);
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/InchargeDashboardForm.fxml"))));

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();

            } else {
                Notification.showErrorDialogBox(rootPane,anchorPane,"lk/ijse/im_system/view/assets/images/alert_glass.png","Invalid Username or Password");
            }

        } else if (userRole.equals("Storekeeper")) {

            if (UserLoginController.verifyStorekeeper(username,tPassword,fPassword)) {
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/StorekeeperDashboardForm.fxml"))));

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();

            } else {
                Notification.showErrorDialogBox(rootPane,anchorPane,"lk/ijse/im_system/view/assets/images/alert_glass.png","Invalid Username or Password");
            }
        }
        window.show();
    }

    public void signUpOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        Stage window = new Stage();

        String username = txtUsername.getText();
        String tPassword = txtPassword.getText();
        String fPassword = fieldPassword.getText();
        String email = txtEmail.getText();
        String tEmailPwd = txtEmailPassword.getText();
        String fEmailPwd = fieldEmailPassword.getText();
        String userPassword = null;
        String emailPassword = null;

        if (username.equals("")) { // if username is empty
            Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Invalid Username.Try Again...");
            return;

        } else if (fPassword.equals("")) { // if fPassword is empty

            if (tPassword.equals("")) { // and if txtPassword is empty

                Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Invalid User Password.\nTry Again...");
                return;

            } else { // if fPassword is empty but txtPassword is not empty
                //
            }
        }

        if (fPassword.equals("") && !tPassword.equals("")) {
            userPassword = tPassword;

        } else if (!fPassword.equals("") && tPassword.equals("")) {
            userPassword = fPassword;

        } else if (!fPassword.equals("") && !tPassword.equals("")) {
            if (fieldPassword.isVisible()) {
                userPassword = fPassword;

            } else if (txtPassword.isVisible()) {
                userPassword = tPassword;
            }
        }

        if (fEmailPwd.isEmpty() && !tEmailPwd.isEmpty()) {
            emailPassword = tEmailPwd;

        } else if (!fEmailPwd.isEmpty() && tEmailPwd.isEmpty()) {
            emailPassword = fEmailPwd;

        } else if (!fEmailPwd.isEmpty() && !tEmailPwd.isEmpty()) {
            if (fieldEmailPassword.isVisible()) {
                emailPassword = fEmailPwd;

            } else if (txtEmailPassword.isVisible()) {
                emailPassword = tEmailPwd;
            }
        }

        if (userRole.equals("Ward In-charge")) {

            User incharge = User.INCHARGE;

            InchargeLogin newInchargeSignUp = new InchargeLogin(
                    username,
                    userPassword,
                    incharge,
                    wardSelected,
                    email,
                    emailPassword
            );

            if (UserLoginController.isInchargeUsernameAlreadyTaken(username)) {
                Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Username not available.\nTry again...");
                return;

            } else if (UserLoginController.isPwdAlreadyTaken(userPassword)) {
                Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "User Password not available.\nTry again...");
                return;

            } else {

                if (cmbInchargeId.getValue() == null) {
                    Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "No Incharge Id selected.Try again...");
                    return;
                }

                if (UserLoginController.signUpAsNewIncharge(newInchargeSignUp, cmbInchargeId.getValue(), rootPane, anchorPane)) {

                    loggedWardNo = newInchargeSignUp.getWardNo();
                    System.out.println(loggedWardNo);
                    window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/InchargeDashboardForm.fxml"))));
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();
                    window.show();

                } else {
                    //Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Failed to Sign Up.\nTry again...");
                }
            }

        } else if (userRole.equals("Storekeeper")) {

            User storekeeper = User.STOREKEEPER;

            StorekeeperLogin newStorekeeperSignUp = new StorekeeperLogin(
                    username,
                    userPassword,
                    storekeeper
            );

            if (UserLoginController.isStorekeeperUsernameAlreadyTaken(username)) {
                Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Username not available.Try again...");
                return;

            } else if (UserLoginController.isSKPwdAlreadyTaken(userPassword)) {
                Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "User Password not available.\nTry again...");
                //new Alert(Alert.AlertType.WARNING,"User Password already taken.Try again...",ButtonType.OK).show();
                return;

            } else {

                if (storeMail.equals(email)) {
                    if (storeMailPwd.equals(emailPassword)) {
                        if (UserLoginController.signUpAsNewStorekeeper(newStorekeeperSignUp)) {

                            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/StorekeeperDashboardForm.fxml"))));
                            Stage stage = (Stage) btnLogin.getScene().getWindow();
                            stage.close();
                            window.show();
                        }

                    } else {
                        Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Invalid Password for Email.\nTry Again...");
                        return;
                    }

                } else {
                    Notification.showErrorDialogBox(rootPane, anchorPane, "lk/ijse/im_system/view/assets/images/alert_glass.png", "Invalid Email Address.\nTry Again...");
                    return;
                }
            }
        }
    }
}


