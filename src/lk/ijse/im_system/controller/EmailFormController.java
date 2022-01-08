package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.UserLoginController;
import lk.ijse.im_system.controller.util.*;
import org.controlsfx.control.Notifications;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class EmailFormController implements SendMailEvent, Validation {

    public TextField txtSender;
    public TextField txtPassword;
    public PasswordField fieldPassword;
    public MaterialDesignIconView iconHidePassword;
    public MaterialDesignIconView iconShowPassword;

    public TextField txtRecipient;
    public TextField txtSubject;
    public TextArea txtContent;
    public ListView<File> listViewAttachment;

    public JFXButton btnCancel;

    private Notifications sendNotifications;
    private Notifications receiveNotifications;
    private String senderMail;
    private String senderPwd;
    private String recipient;

    private File pickedFileFromList = null;

    private WardOrderFormController event1;
    private WardCondemnInventoryFormController event2;

    KeyEvent keyEvent;

    public void initialize() throws SQLException, ClassNotFoundException {

        if (MainFormController.loggedWardNo == null) { // if a Storekeeper has logged in
            senderMail = "invms1141@gmail.com";
            txtSender.setText(senderMail);
        } else  {
            senderMail = UserLoginController.getInchargeMail(MainFormController.loggedWardNo);
            txtSender.setText(senderMail);
            txtRecipient.setText("invms1141@gmail.com");
            txtSubject.setText(MainFormController.loggedWardNo);
        }

        if(InchargeDashboardFormController.pageTitle.equals("Orders")) {
            txtSubject.setText(MainFormController.loggedWardNo+" - New Order");

        } else if(InchargeDashboardFormController.pageTitle.equals("Inventory Condemnations")){
            txtSubject.setText(MainFormController.loggedWardNo+" - New Condemnation");
        }

        listViewAttachment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            pickedFileFromList = newValue;
        });

        fieldPassword.setOnKeyReleased(event1 -> {
            validateField();
        });

        txtPassword.setOnKeyReleased(event -> {
            validateField();
            fieldPassword.setText(txtPassword.getText());
        });

        mapValidations();

    }

    boolean isValidPwd = false;
    boolean isValidTxtPwd = false;

    private void validateField() {

        isValidPwd = Pattern.matches("^[a-z0-9@]{8,12}$", fieldPassword.getText());
        isValidTxtPwd = Pattern.matches("^[a-z0-9@]{8,12}$", txtPassword.getText());

        if (isValidPwd || isValidTxtPwd) {
            fieldPassword.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
            txtPassword.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            fieldPassword.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
            txtPassword.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }
    }

    LinkedHashMap<TextField, Pattern> mapEmailDetails = new LinkedHashMap<>();

    String wardEmailPwd = "^[a-z0-9@]{8,12}$";  // cannot use uppercase

    Pattern wardEmailPwdPtn = Pattern.compile(wardEmailPwd);

    @Override
    public void mapValidations(){
        mapEmailDetails.put(txtPassword,wardEmailPwdPtn);
        mapEmailDetails.put(fieldPassword,wardEmailPwdPtn);
    }

    public void validateFieldOnKeyRelease(KeyEvent keyEvent) {

        /*Object response = validateFormFields(mapEmailDetails);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField invalidField = (TextField) response;
                invalidField.requestFocus();

            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION,"success").showAndWait();
                setStyleToInitial();
            }
        }*/

    }

    private void setStyleToInitial() {
        for (TextField keyTextField : mapEmailDetails.keySet()) {
            keyTextField.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");
        }
    }

    private void validateHiddenTXTUserPwdField() {

        /*boolean pwdMatches = Pattern.matches(wardEmailPwd, txtPassword.getText());

        txtPassword.getStylesheets().clear();

        if (pwdMatches) {
            txtPassword.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            txtPassword.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
            fieldPassword.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }*/
    }

    private void loadListView() {
        for (File f : attachedFiles ) {
            listViewAttachment.getItems().add(f);
        }
    }

    private File selectedFile = null; // single file selected from Single-FileChooser
    private List<File> selectedFiles = null; // files selected from Multi-FileChooser
    private List<File> attachedFiles = new ArrayList<>(); // list of Files to display in listView

    public void openFileChooserOnClick(MouseEvent mouseEvent) { // Attachment glyph

        FileChooser fc = new FileChooser();
        fc.setTitle("Choose File");

        if(InchargeDashboardFormController.pageTitle.equals("Orders")) {
            fc.setInitialFileName("Order_00");

        } else if(InchargeDashboardFormController.pageTitle.equals("Inventory Condemnations")){
            fc.setInitialFileName("Condemn_00");
        }

        fc.setInitialDirectory(new File("/home"));
        //fc.setInitialDirectory(selectedFile.getParentFile());

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Documents","*.odt","*.doc"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        if(InchargeDashboardFormController.pageTitle.equals("Orders")) {

            selectedFile = fc.showOpenDialog(null);

            if (selectedFile != null) {
                attachedFiles.add(new File(selectedFile.getName()));
                listViewAttachment.setItems(FXCollections.observableArrayList(attachedFiles));

            } else {
                //System.out.println("File is not valid");
            }

        } else if(InchargeDashboardFormController.pageTitle.equals("Inventory Condemnations")){

            selectedFiles = fc.showOpenMultipleDialog(null);

            if (selectedFiles != null) {

                for (File f : selectedFiles) {
                    attachedFiles.add(new File(f.getName()));
                    listViewAttachment.setItems(FXCollections.observableArrayList(attachedFiles));
                }

            } else {
                //System.out.println("File is not valid");
            }
        }
    }

    public void openAttachmentOnAction(ActionEvent actionEvent) {
    }

    public void removeAttachmentOnAction(ActionEvent actionEvent) {
        attachedFiles.remove(pickedFileFromList);
        listViewAttachment.getItems().clear();
        loadListView();
    }

    public void sendEmailOnAction(ActionEvent actionEvent) throws IOException, MessagingException, SQLException, ClassNotFoundException {
        senderPwd = fieldPassword.getText();
        recipient = txtRecipient.getText();
        sendMail();
    }

    @Override
    public void sendMail() throws MessagingException, IOException, SQLException, ClassNotFoundException {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, senderPwd);
            }
        });

        Message message = prepareMessage(session,senderMail,recipient);

        String fileName = message.getFileName();

        /**
         * adding an attachment
         *part1- for text msg, part2 - for attachment, part3- for another attachment.....
         */

        Multipart emailContent = new MimeMultipart();

        /**Text Body Part*/

        MimeBodyPart textBodyPart = new MimeBodyPart();
        //textBodyPart.setText("New Order from "+txtSubject.getText());
        textBodyPart.setText(txtContent.getText());

        /**Attach Text to email content*/
        emailContent.addBodyPart(textBodyPart);

        /**Attachment Body Part*/

        if (selectedFiles != null) { // if a sending multiple files - Condemnation

            for (File f : selectedFiles) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(f);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(f.getAbsolutePath());

                /**Attach Files to email content*/
                emailContent.addBodyPart(attachmentBodyPart);
            }

        } else if (selectedFile != null) { // if sending only a single file - Order

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(selectedFile.getAbsolutePath());
            emailContent.addBodyPart(attachment);
        }

        /**Attach email content(text, files) to message*/
        message.setContent(emailContent);

        if (fieldPassword.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,"Password not accepted",ButtonType.OK).show();
            return;

        } else if (!fieldPassword.getText().isEmpty() || !txtPassword.getText().isEmpty()) {
            if (UserLoginController.verifyMailPassword(senderMail,fieldPassword.getText())) {

                Transport.send(message);
                Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Email sent successfully.");

            } else {
                new Alert(Alert.AlertType.WARNING,"Invalid Password. Try again...",ButtonType.OK).show();
            }
        }
    }

    @Override
    public Message prepareMessage(Session session, String senderMail, String recipient) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(senderMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //BCC,CC

            message.setSubject(txtSubject.getText());
            message.setText(txtContent.getText());

            return message;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();

        if(InchargeDashboardFormController.pageTitle.equals("Orders")) {
            event1.contextPane.setEffect(null);

        } else if(InchargeDashboardFormController.pageTitle.equals("Inventory Condemnations")){
            event2.contextPane.setEffect(null);
        }
    }

    public void setEvent(WardOrderFormController event) { // to catch WardOrderFormController
        this.event1 = event;
    }

    public void setEventFromWardCondemnInventory(WardCondemnInventoryFormController event) { // to catch WardCondemnInventoryFormController
        this.event2 = event;
    }

    public void showPasswordOnAction(MouseEvent mouseEvent) {
        txtPassword.setVisible(true);
        fieldPassword.setVisible(false);

        txtPassword.toFront();
        txtPassword.setText(fieldPassword.getText());
        iconHidePassword.toFront();
        iconHidePassword.setVisible(true);
        iconShowPassword.setVisible(false);

        validateHiddenTXTUserPwdField();
    }

    public void hidePasswordOnAction(MouseEvent mouseEvent) {
        txtPassword.setVisible(false);
        fieldPassword.setVisible(true);

        fieldPassword.toFront();
        fieldPassword.setText(txtPassword.getText());
        iconShowPassword.toFront();
        iconShowPassword.setVisible(true);
        iconHidePassword.setVisible(false);
    }
}
