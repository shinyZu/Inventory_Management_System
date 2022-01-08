package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.im_system.controller.controller_db.InchargeController;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.OrderController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.controller.util.*;
import lk.ijse.im_system.db.DbConnection;
import lk.ijse.im_system.model.Inventory;
import lk.ijse.im_system.model.Order;
import lk.ijse.im_system.model.OrderDetail;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;
import lk.ijse.im_system.view.table_model.WardWise_Order_Condemn_DetailsTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class OrderFormController extends FieldDataForm implements LoadTableEvent, FilterComboBoxEvent, SendMailEvent {

    public Label lblWard;
    public Label lblOrderID;

    public TextField txtQtyOnHand;
    public Spinner<Integer> spinnerOrderQTY;

    public TableView<Order_Condemn_InventoryTM> tblNewOrder;
    public TableColumn colCategory;
    public TableColumn colInventoryNo;
    public TableColumn colDescription;
    public TableColumn colOrderQty;
    
    public JFXDatePicker datePicker;

    public TableView<WardWise_Order_Condemn_DetailsTM> tblWardOrderDetails;
    public TableColumn colOrderId;
    public TableColumn colWardNo;
    public TableColumn colInvNo;
    public TableColumn colDescript;
    public TableColumn colQtyOrdered;
    public TableColumn colOrderDate;

    private InboxFormController event;
    private static String wardNo  = null;

    public static String senderMail;
    public static String senderPwd;
    private String recipient;
    
    String cmbWardSelected;
    String cmbCategorySelected;
    String cmbInventoryNoSelected;
    String cmbDescriptionSelected;
    Order_Condemn_InventoryTM invSelected;

    String wardToFilter = null;
    String invToFilter = null;
    String date = null;

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
            lblOrderID.setText(OrderController.getNextOrderID());
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
        this.spinnerOrderQTY.setValueFactory(qtyValueFactory);

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
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("modifiedQty"));

        tblNewOrder.setItems(orderList);

        // Table 2

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_condemn_Id"));
        colWardNo.setCellValueFactory(new PropertyValueFactory<>("wardNo"));
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("invNo"));
        colDescript.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOrdered.setCellValueFactory(new PropertyValueFactory<>("qtyOrdered_Condemned"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("date_Ordered_Condemned"));

        loadTable();
    }

    ObservableList<WardWise_Order_Condemn_DetailsTM> orderDetailList = FXCollections.observableArrayList(); // list for table 2

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException {
        orderDetailList = OrderController.getWardOrderDetails(orderDetailList);
        tblWardOrderDetails.setItems(orderDetailList);
    }

    @Override
    public void setFieldsOnInvNo() throws SQLException, ClassNotFoundException {
        Inventory inventory = InventoryController.getInventoryOnInvNo(cmbInventoryNoSelected);

        if (inventory == null) {
            new Alert(Alert.AlertType.WARNING,"Inventory Currently not Available at Store.");

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

    public static ObservableList<Order_Condemn_InventoryTM> orderList = FXCollections.observableArrayList();

    public void importListOnAction(ActionEvent actionEvent) {
        importList(tblNewOrder);
    }

    ObservableList<Order_Condemn_InventoryTM> listToAdd = FXCollections.observableArrayList();

    public void addToListOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String qtyOnStore = txtQtyOnHand.getText();
        int spinnerValue = Integer.parseInt(spinnerOrderQTY.getEditor().getText());

        orderList = addToList(cmbCategorySelected,cmbInventoryNoSelected,cmbDescriptionSelected,spinnerValue,qtyOnStore,orderList);

        if (spinnerValue != 0) {
            clearFields();
            cmbInventoryNo.setDisable(true);
            cmbDescription.setDisable(true);
        } else {
            //
        }
    }

    public void removeFromListOnAction(ActionEvent actionEvent) {
        orderList.remove(invSelected);
        tblNewOrder.setItems(orderList);
        tblNewOrder.refresh();
    }

    public void clearTableOnAction(ActionEvent actionEvent) {
        clearFields();
        lblWard.setText("WD-000");
        cmbWardNo.getSelectionModel().clearSelection();
        cmbCategory.setDisable(true);
        tblNewOrder.getItems().clear();
    }

    private void clearFields() {

        if (cmbCategorySelected != null) {
            cmbCategory.getSelectionModel().clearSelection();
        } else {
            //cmbCategory.setValue("");
        }
        cmbInventoryNo.getSelectionModel().clearSelection();
        cmbDescription.getSelectionModel().clearSelection();
        txtQtyOnHand.clear();
        spinnerOrderQTY.getEditor().setText("0");

    }

    public void confirmOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, MessagingException {

        if (lblWard.getText().equals("WD-000")) {
            new Alert(Alert.AlertType.WARNING,"No Ward selected.\nPlease select a Ward to proceed...",ButtonType.OK).show();
            return;

        }

        if (orderDetailList.size() == 0) {
            new Alert(Alert.AlertType.WARNING,"No Inventory selected.\nPlease select Inventory to proceed.",ButtonType.OK).show();
            return;
        }

        if (!lblWard.getText().equals("WD-000")) {
            recipient = WardController.getEmail(lblWard.getText());

        } else {
            new Alert(Alert.AlertType.WARNING,"No Ward specified.\nPlease select a Ward No to proceed...",ButtonType.OK).show();
            return;
        }

        ArrayList<OrderDetail> orderInvDetails = new ArrayList();

        Order newOrder = new Order(
                lblOrderID.getText(),
                DateTime.date,
                lblWard.getText()
        );

        for (Order_Condemn_InventoryTM octm : orderList) {

            orderInvDetails.add(new OrderDetail(
                   lblOrderID.getText(),
                   octm.getInvNo(),
                   octm.getModifiedQty()
            ));
        }

        if (OrderController.confirmOrder(newOrder,orderInvDetails)) {

            new Alert(Alert.AlertType.CONFIRMATION,"Order Placed Successful.",ButtonType.OK).show();
            lblOrderID.setText(OrderController.getNextOrderID());
            loadTable();
            sendMail();

        } else {
            new Alert(Alert.AlertType.WARNING,"Failed to Place the Order.\nTry again...",ButtonType.OK).show();
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

            message.setSubject("Order Placed Successfully.");
            message.setText("You can now collect your Order from Store");

            return message;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void applyFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        filterTableDetails(tblWardOrderDetails,orderDetailList,wardToFilter,invToFilter,date);
    }

    public void clearFiltersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearFilters();
        orderDetailList.clear();
        loadTable();
    }

    private void clearFilters() {
        cmbWard.getSelectionModel().clearSelection();
        cmbDescription2.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
    }

    public void setEvent(InboxFormController event, List<Order_Condemn_InventoryTM> orderList, String wardNo) {
        this.event = event;
        OrderFormController.orderList = FXCollections.observableArrayList(orderList);
        OrderFormController.wardNo = wardNo;
    }

    public void printRequestNoteOnAction(ActionEvent actionEvent) {

        if (lblWard.getText().equals("WD-000")) {
            new Alert(Alert.AlertType.WARNING,"No Ward No specified...\nSelect a Ward No",ButtonType.OK).show();
            return;
        }

        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("../view/reports/RequestNote.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            /**
             * Create a Bean Array Data Source and pass the values in table to it
             * */

            ObservableList<Order_Condemn_InventoryTM> orderList = tblNewOrder.getItems();

            /**
             * Getting/Calculating values for Parameters in Report
             * */
            String wardNo = lblWard.getText();
            String wardTitle = WardController.getWardTitle(wardNo);
            String[] inchargeIdName = InchargeController.getInchargeId(wardNo);
            String inchargeId = inchargeIdName[0];
            String inchargeName = inchargeIdName[1];
            String extensionNo = WardController.getExtensionNo(wardNo);
            String orderId = lblOrderID.getText();

            /**
             * Mapping Calculated Values with the Parameters in Report
             * */

            HashMap map = new HashMap();
            map.put("wardTitle",wardTitle);
            map.put("wardNo",wardNo);
            map.put("inchargeId",inchargeId);
            map.put("inchargeName",inchargeName);
            map.put("extensionNo",extensionNo);
            map.put("orderId",orderId);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JRBeanArrayDataSource(orderList.toArray()));
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
