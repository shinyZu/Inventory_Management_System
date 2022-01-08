package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.controller.util.*;
import lk.ijse.im_system.model.CondemnDetail;
import lk.ijse.im_system.model.Condemnation;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;
import lk.ijse.im_system.view.table_model.WardWise_Order_Condemn_DetailsTM;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CondemnInventoryFormController extends FieldDataForm implements FilterComboBoxEvent, LoadTableEvent, SendMailEvent {
    public Label lblWard;
    public Label lblCondemnId;

    public TextField txtQtyWithWard;
    public Spinner<Integer> spinnerCondemnQTY;
    
    public TableView<Order_Condemn_InventoryTM> tblNewCondemn;
    public TableColumn colCategory;
    public TableColumn colInventoryNo;
    public TableColumn colDescription;
    public TableColumn colCondemnQty;

    public JFXDatePicker datePicker;

    public TableView<WardWise_Order_Condemn_DetailsTM> tbWardCondemnDetails;
    public TableColumn colCondemnId;
    public TableColumn colWardNo;
    public TableColumn colInvNo;
    public TableColumn colDescript;
    public TableColumn colQtyCondemned;
    public TableColumn colCondemnDate;

    private InboxFormController event;
    private static String wardNo  = null;

    String cmbWardSelected;
    String cmbCategorySelected;
    String cmbInventoryNoSelected;
    String cmbDescriptionSelected;
    Order_Condemn_InventoryTM invSelected;

    public static String senderMail;
    public static String senderPwd;
    private String recipient;

    String wardToFilter;
    String invToFilter;
    String date;

    static {
        senderMail = "invms1141@gmail.com";
        senderPwd = "ims1141@ijse";
    }

    public void initialize() {

        if (wardNo == null ) {
            lblWard.setText("WD-000");

        } else {
            lblWard.setText(wardNo);
        }

        try {
            lblCondemnId.setText(CondemnController.getNextCondemnID());
            loadWards();
            loadCategory();
            loadWardNo();
            loadDescriptionForFilter();
            initTable();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCategory.setDisable(true);
        cmbInventoryNo.setDisable(true);
        cmbDescription.setDisable(true);

        SpinnerValueFactory<Integer> qtyValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        this.spinnerCondemnQTY.setValueFactory(qtyValueFactory);

        cmbWardNo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbWardSelected = newValue;

            if (newValue != null){
                lblWard.setText(newValue);
            } else {
                lblWard.setText("WD-000");
            }

            cmbCategory.setDisable(false);

        });

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

        //--------------------------------------------------Filter--------------------------------------------------

        cmbWard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            wardToFilter = newValue;

        });

        cmbDescription2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            invToFilter = newValue;
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            date = String.valueOf(newValue);
        });
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        //Table 1

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colInventoryNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCondemnQty.setCellValueFactory(new PropertyValueFactory<>("modifiedQty"));

        tblNewCondemn.setItems(condemnList);

        // Table 2

        colCondemnId.setCellValueFactory(new PropertyValueFactory<>("order_condemn_Id"));
        colWardNo.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescript.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyCondemned.setCellValueFactory(new PropertyValueFactory<>("qtyOrdered_Condemned"));
        colCondemnDate.setCellValueFactory(new PropertyValueFactory<>("date_Ordered_Condemned"));

        loadTable();
    }

    ObservableList<WardWise_Order_Condemn_DetailsTM> condemnDetailList = FXCollections.observableArrayList(); // list for table 2

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        condemnDetailList = CondemnController.getWardCondemnDetails(condemnDetailList);
        tbWardCondemnDetails.setItems(condemnDetailList);
    }

    @Override
    public void setFieldsOnInvNo() throws SQLException, ClassNotFoundException {

        String description = InventoryController.getOnlyDescription(cmbInventoryNoSelected);
        int qtyWithWard = InventoryWithWardController.getQtyWithWardOfInv(cmbWardSelected,cmbInventoryNoSelected);

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
        int qtyWithWard = InventoryWithWardController.getQtyWithWardOfInv(cmbWardSelected,invNo);

        if (qtyWithWard != 0 && invNo != null ) {
            cmbInventoryNo.setValue(invNo);
            txtQtyWithWard.setText(String.valueOf(qtyWithWard));

        } else {
            new Alert(Alert.AlertType.WARNING,"Inventory Currently not Available at Ward.");
        }
    }

    public static ObservableList<Order_Condemn_InventoryTM> condemnList = FXCollections.observableArrayList();

    public void setEvent(InboxFormController event, List<Order_Condemn_InventoryTM> condemnList, String wardNo) {
        this.event = event;
        CondemnInventoryFormController.condemnList = FXCollections.observableArrayList(condemnList);
        CondemnInventoryFormController.wardNo = wardNo;
    }

    public void addToListOnAction(ActionEvent actionEvent) {
        String qtyOnStore = txtQtyWithWard.getText();
        int spinnerValue = Integer.parseInt(spinnerCondemnQTY.getEditor().getText());

        condemnList = addToList(cmbCategorySelected,cmbInventoryNoSelected,cmbDescriptionSelected,spinnerValue,qtyOnStore,condemnList);

        if (spinnerValue != 0) {
            clearFields();
            cmbInventoryNo.setDisable(true);
            cmbDescription.setDisable(true);
        }
    }

    public void removeFromListOnAction(ActionEvent actionEvent) {
        condemnList.remove(invSelected);
        tblNewCondemn.setItems(condemnList);
        tblNewCondemn.refresh();
    }

    public void importListOnAction(ActionEvent actionEvent) {
        importList(tblNewCondemn);
    }

    public void confirmCondemnOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, MessagingException {

        if (lblWard.getText().equals("WD-000")) {
            new Alert(Alert.AlertType.WARNING,"No Ward selected.\nPlease select a Ward to proceed...",ButtonType.OK).show();
            return;

        }

        if (condemnDetailList.size() == 0) {
            new Alert(Alert.AlertType.WARNING,"No Inventory selected.\nPlease select Inventory to proceed.",ButtonType.OK).show();
            return;
        }

        if (!lblWard.getText().equals("WD-000")) {
            recipient = WardController.getEmail(lblWard.getText());

        } else {
            new Alert(Alert.AlertType.WARNING,"No Ward specified.\nPlease select a Ward No to proceed...",ButtonType.OK).show();
            return;
        }

        ArrayList<CondemnDetail> condemnInvDetails = new ArrayList();

        Condemnation newCondemn = new Condemnation(
                lblCondemnId.getText(),
                lblWard.getText(),
                DateTime.date
        );

        for (Order_Condemn_InventoryTM octm : condemnList) {

            condemnInvDetails.add(new CondemnDetail(
                    lblCondemnId.getText(),
                    octm.getInvNo(),
                    octm.getModifiedQty()
            ));
        }

        if (CondemnController.confirmCondemn(newCondemn,condemnInvDetails)) {

            new Alert(Alert.AlertType.CONFIRMATION,"Condemnation done successfully.",ButtonType.OK).show();
            lblCondemnId.setText(CondemnController.getNextCondemnID());
            loadTable();
            sendMail();

        } else {
            new Alert(Alert.AlertType.WARNING,"Failed to Condemn Inventory.\nTry again...",ButtonType.OK).show();
        }
    }
    @Override
    public void sendMail() throws SQLException, ClassNotFoundException, MessagingException {

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

        recipient = WardController.getEmail(lblWard.getText());

        Message message = prepareMessage(session,senderMail,recipient);

        Transport.send(message);
        Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Email sent successfully.");
    }

    @Override
    public Message prepareMessage(Session session, String senderMail, String recipient) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(senderMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //BCC,CC

            message.setSubject("Condemnation done successfully.");

            message.setText("Your condemn details have been recorded successfully.");

            return message;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearTableOnAction(ActionEvent actionEvent) {
        clearFields();
        lblWard.setText("WD-000");
        cmbWardNo.getSelectionModel().clearSelection();
        cmbCategory.setDisable(true);
        tblNewCondemn.getItems().clear();
    }

    private void clearFields() {
        if (cmbCategorySelected != null) {
            cmbCategory.getSelectionModel().clearSelection();
        } else {
            //cmbCategory.setValue("");
        }
        cmbInventoryNo.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        txtQtyWithWard.clear();
        spinnerCondemnQTY.getValueFactory().setValue(0);
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        filterTableDetails(tbWardCondemnDetails,condemnDetailList,wardToFilter,invToFilter,date);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        condemnDetailList.clear();
        loadTable();
    }

    private void clearFilters() {
        cmbWard.getSelectionModel().clearSelection();
        cmbDescription2.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
    }

}
