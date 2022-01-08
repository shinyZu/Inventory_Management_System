package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.controller.util.DateTime;
import lk.ijse.im_system.controller.util.LoadInboxEvent;
import lk.ijse.im_system.view.table_model.Inventory_QtyTM;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class StorekeeperDashboardFormController extends DateTime implements LoadInboxEvent {
    public StackPane contextDashboard;

    public MaterialDesignIconView headerIcon;
    public Label lblHeader;

    public JFXButton btnDashboard;
    public JFXButton btnCategories;

    public AnchorPane contextCommonPane;

    //public MaterialDesignIconView iconCategory;
    public Label lblNotification;
    public Label lblMailBox;

    String storeMail = "invms1141@gmail.com";
    String storeMailPwd = "ims1141@ijse";
    String host = "imap.gmail.com";

    public void initialize(){

        loadDateAndTime();

        try {
            updateMailBox();
            updateNotifications();
            loadDashboard();

        } catch (MessagingException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateNotifications() throws SQLException, ClassNotFoundException {
        lblNotification.setText(InventoryController.getInvBelowLevelCount());
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
        store.connect(host, storeMail, storeMailPwd);

        return store.getFolder("INBOX");
    }

    public void logoutOnAction(MouseEvent mouseEvent) throws IOException {
        Stage window = (Stage) contextDashboard.getScene().getWindow();
        window.close();

        window = new Stage();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"))));
        window.show();
    }

    public void showDashboardOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        lblHeader.setText("Dashboard");
        loadDashboard();
    }

    private void loadDashboard() throws IOException, SQLException, ClassNotFoundException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/DashBoardStorekeeperForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showCategoryOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Category");

        Parent load = FXMLLoader.load(getClass().getResource("../view/CategoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);

    }

    public void showInventoryWithStoreOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Inventory With Store");

        Parent load = FXMLLoader.load(getClass().getResource("../view/StoreInventoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showInventoryWithWardsOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Inventory With Wards");

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardWiseInventoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showCondemnsOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Inventory Condemnations");

        Parent load = FXMLLoader.load(getClass().getResource("../view/CondemnInventoryForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showInchargeDetailOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Wards/In-charges");

        Parent load = FXMLLoader.load(getClass().getResource("../view/WardInchargesForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showOrdersOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Orders");

        Parent load = FXMLLoader.load(getClass().getResource("../view/OrderForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
    }

    public void showReportsOnAction(ActionEvent actionEvent) throws IOException {
        lblHeader.setText("Reports");

        Parent load = FXMLLoader.load(getClass().getResource("../view/ReportForm.fxml"));
        contextCommonPane.getChildren().clear();
        contextCommonPane.getChildren().add(load);
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

    public static ArrayList<String> msgList = new ArrayList<>();

    public void showInboxMsgsOnClick(MouseEvent mouseEvent) throws MessagingException, IOException { // Mail Icon

        Message[] msgs = fetchMessages(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/InboxForm.fxml"));
        Parent parent = loader.load();

        InboxFormController controller = loader.getController();
        controller.setEvent(this);

        msgList = sortMessages(msgList,msgs);
        controller.inboxListView.setItems(FXCollections.observableArrayList(msgList));

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.show();
    }
}
