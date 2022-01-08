package lk.ijse.im_system.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.InchargeController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.DateTime;
import lk.ijse.im_system.controller.util.LoadInboxEvent;

import javax.mail.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class InchargeDashboardFormController extends DateTime implements LoadInboxEvent {

    public StackPane contextDashboard;
    public AnchorPane contextCommonPane;
    public BorderPane dashBorderPane;

    public Label lblMailBox;
    public Label lblNotification;

    public Label lblHeader;
    public static String pageTitle;

    String wardMail;
    String wardMailPwd;
    String host  = "imap.gmail.com";

    public void initialize(){
        loadDateAndTime();

        try {
            wardMail = WardController.getEmail(MainFormController.loggedWardNo);
            wardMailPwd = WardController.getEmailPwd(MainFormController.loggedWardNo);

            updateMailBox();
            updateNotifications();
            loadDashboard();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateNotifications() throws SQLException, ClassNotFoundException {
        lblNotification.setText(InventoryWithWardController.getInvBelowLevelCount(MainFormController.loggedWardNo));
    }

    @Override
    public void updateMailBox() throws MessagingException {
        Folder inbox = fetchInbox();
        lblMailBox.setText("");
        lblMailBox.setText(String.valueOf(inbox.getUnreadMessageCount()));

    }

    @Override
    public Folder fetchInbox() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore();

        if (wardMail != null) {
            store.connect(host, wardMail, wardMailPwd);
            return store.getFolder("INBOX");

        } else {
            //new Alert(Alert.AlertType.WARNING,"New Incharge Logged in...", ButtonType.OK).show();
        }

        return null;
    }

    public void logoutOnAction(MouseEvent mouseEvent) throws IOException {
        Stage window = (Stage) contextDashboard.getScene().getWindow();
        window.close();

        window = new Stage();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"))));
        window.show();

        MainFormController.loggedWardNo = null;
    }

    public void showDashboardOnAction(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        lblHeader.setText("Dashboard");
        pageTitle = lblHeader.getText();
        loadDashboard();

    }

    private void loadDashboard() throws IOException, SQLException, ClassNotFoundException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/DashBoardInchargeForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showCategoryOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Category");
        pageTitle = lblHeader.getText();

        Parent load = FXMLLoader.load(getClass().getResource("../view/CategoryInWardsForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showInventoryOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Inventory");
        pageTitle = lblHeader.getText();

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardInventoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showCondemnsOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Inventory Condemnations");
        pageTitle = lblHeader.getText();

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardCondemnInventoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showOrdersOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Orders");
        pageTitle = lblHeader.getText();

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardOrderForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showReportsOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Reports");
        pageTitle = lblHeader.getText();

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardReportForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public static ArrayList<String> msgList = new ArrayList<>();

    public void showInboxMsgsOnClick(MouseEvent mouseEvent) throws MessagingException, IOException {  // Mail Icon

        Message[] msgs = fetchMessages(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/InboxForm.fxml"));
        Parent parent = loader.load();

        InboxFormController controller = loader.getController();
        controller.setEventAsInchargeDashBoard(this);

        msgList = sortMessages(msgList,msgs);
        controller.inboxListView.setItems(FXCollections.observableArrayList(msgList));

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.show();
    }

    public void showNotificationsOnClick(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException { // Bell Icon

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NotificationForm.fxml"));
        Parent parent = loader.load();

        NotificationFormController controller = loader.getController();
        controller.setEvent(this);
        controller.loadNotifications();

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.show();
    }
}
