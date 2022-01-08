package lk.ijse.im_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;

import javax.mail.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class InboxFormController {

    public ListView<String> inboxListView;
    public ListView msgTypeListView;
    public Label lblStoreWardMail;

    private StorekeeperDashboardFormController event1;
    private InchargeDashboardFormController event2;

    String mailSelected;

    String host = "imap.gmail.com";

    String storeMail;
    String storeMailPwd;

    String wardMail;
    String wardMailPwd;

    int unreadMessageCount;

    public void initialize() {

        inboxListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mailSelected = newValue; // selected Mail from Inbox
        });

        loadMsgTypeListView();

        if (MainFormController.loggedWardNo == null) { // if a Storekeeper is logged in
            storeMail = "invms1141@gmail.com";
            storeMailPwd = "ims1141@ijse";

            lblStoreWardMail.setText(storeMail);

        } else { // if an Incharge is logged in

            try {
                wardMail = WardController.getEmail(MainFormController.loggedWardNo);
                wardMailPwd = WardController.getEmailPwd(MainFormController.loggedWardNo);

                lblStoreWardMail.setText(wardMail);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    ObservableList<String> msgTypes = FXCollections.observableArrayList();
    private void loadMsgTypeListView() {
        msgTypes.addAll("All Mail","Unread Mails","Sent","Drafts");
        msgTypeListView.setItems(msgTypes);
    }

    public void openMailOnAction(ActionEvent actionEvent) throws IOException, MessagingException {

        if (MainFormController.loggedWardNo == null) { // if a Storekeeper is logged in should get and display store inbox
            fetchStoreInbox();
            event1.updateMailBox();

        } else { // if an Incharge is logged in

            try {
                wardMail = WardController.getEmail(MainFormController.loggedWardNo);
                wardMailPwd = WardController.getEmailPwd(MainFormController.loggedWardNo);
                fetchWardInbox();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchWardInbox() throws MessagingException {

        /**Set Properties*/

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        /**Get the Session Object*/

        Session emailSession = Session.getDefaultInstance(properties);

        /**Create IMAP store object and connect to the imap store*/

        Store store = emailSession.getStore();
        store.connect(host, wardMail, wardMailPwd);

        Folder inboxFolder = store.getFolder("INBOX");
        //inboxFolder.open(Folder.READ_ONLY);
        inboxFolder.open(Folder.READ_WRITE);

        unreadMessageCount = inboxFolder.getUnreadMessageCount();

        /**Retrieve the messages from the folder object*/

        Message[] messages = inboxFolder.getMessages();

        /**Iterate the messages*/

        Message msg = null;
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].getSubject().equals(mailSelected)) {
                msg = messages[i];
            }
        }
    }

    private void fetchStoreInbox() throws MessagingException, IOException {

        /**Set Properties*/

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        /**Get the Session Object*/

        Session emailSession = Session.getDefaultInstance(properties);

        /**Create IMAP store object and connect to the imap store*/

        Store store = emailSession.getStore();
        store.connect(host, storeMail, storeMailPwd);

        /**Create the folder object and open it in the mailbox*/

        Folder inboxFolder = store.getFolder("INBOX");
        //inboxFolder.open(Folder.READ_ONLY);
        inboxFolder.open(Folder.READ_WRITE);

        unreadMessageCount = inboxFolder.getUnreadMessageCount();

        /**Retrieve the messages from the folder object*/

        Message[] messages = inboxFolder.getMessages();

        /**Iterate the messages*/

        Message msg = null;
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].getSubject().equals(mailSelected)) {
                msg = messages[i];
            }
        }

        String subject = msg.getSubject();

        /**Iterate Multiparts*/

        /*Multipart multipart = (Multipart) msg.getContent();
        System.out.println(multipart);

        BodyPart bodyPart = multipart.getBodyPart(1);
        System.out.println("bodyPart getContent - "+bodyPart.getContent());

        System.out.println("multipart getCount - "+multipart.getCount());

        BodyPart mailSubject = multipart.getBodyPart(0);
        System.out.println("mail subject - "+mailSubject.toString());*/

        String text = null;
        Multipart multipart = null;

        Object content = msg.getContent();

        if (content instanceof String) {
            text = (String)content;

        } else if (content instanceof Multipart) {
            multipart = (Multipart)content;

        }

        BodyPart mailSubject = null;
        BodyPart bodyPart = null;

        if (multipart != null) {

            //mailSubject = multipart.getBodyPart(0);
            mailSubject = multipart.getBodyPart(0); //text in txtContent/textBodyPart
            bodyPart = multipart.getBodyPart(1); // attachments

        } else {
            //
        }

        //------------------------------------------------------------------------------------------------------

        /**Ordered Inventory List In the Mail*/

        String orderContent = String.valueOf(bodyPart.getContent());
        System.out.println("content.toString - " +orderContent);

        /**
         * Specifies the Name and Path of the File to write
         * the details of the New Order from a Ward
         * */

        String orderFilePath = "/home/shiny/Documents/Final-Project/Orders/RecievedFromWards/NewOrder_01.txt";
        File file = new File(orderFilePath);

        /**
         * This logic will make sure that the file
         * gets created if it is not present at the
         * specified location
         * */

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(orderContent);

        if (bw != null) {
            bw.close();

        } else {
            //
        }

//-------------
        /**
         * Reads the written file
         * */

        BufferedReader br = null;

        try {
            String strCurrentLine;

            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            String s = br.readLine();

            List<String[]> row = new ArrayList<>();
            while ((strCurrentLine = br.readLine()) != null) {  // to get multiple lines
                row.add(strCurrentLine.split(","));
            }

            List<Order_Condemn_InventoryTM> orderList = new ArrayList<>();
            for (String[] column : row) {
                orderList.add(new Order_Condemn_InventoryTM(
                        column[0],
                        column[1],
                        column[2],
                        Integer.parseInt(column[3])
                ));

            }

            boolean new_order = mailSelected.contains("New Order");
            boolean new_condemn = mailSelected.contains("New Condemnation");

            String wardNo = mailSelected.split(" - ")[0];

            if (new_order) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderForm.fxml"));
                Parent parent = loader.load();

                OrderFormController controller = loader.getController();
                controller.setEvent(this, orderList, wardNo);

                ActionEvent ev = null;
                event1.showOrdersOnAction(ev);

            } else if (new_condemn) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CondemnInventoryForm.fxml"));
                Parent parent = loader.load();

                CondemnInventoryFormController controller = loader.getController();
                controller.setEvent(this, orderList, wardNo);

                ActionEvent ev = null;
                event1.showCondemnsOnAction(ev);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null ) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //------------------------------------------------------------------------------------------------------

        /**Close the folder and store objects*/

        inboxFolder.close();
        store.close();
    }

    public void setEvent(StorekeeperDashboardFormController event) {
        this.event1 = event;
    }

    public void setEventAsInchargeDashBoard(InchargeDashboardFormController event) {
        this.event2 = event;
    }

    public void refreshInboxOnClick(MouseEvent mouseEvent) throws MessagingException, IOException {
        //event.updateMailBox();
        //event.showInboxMsgsOnClick(mouseEvent);
        //event.loadMsgList(this,);
    }


    public void markAsReadOnAction(ActionEvent actionEvent) throws MessagingException {
        if (MainFormController.loggedWardNo == null ) { // if a Storekeeper is logged in

            ArrayList<String> msgList = StorekeeperDashboardFormController.msgList;
            Message[] msgs = event1.fetchMessages(false);
            msgList.clear();

            markAsRead(msgList,msgs);
            event1.updateMailBox();

        } else {  // if an Incharge is logged in

            ArrayList<String> msgList = StorekeeperDashboardFormController.msgList;
            Message[] msgs = event2.fetchMessages(false);
            msgList.clear();

            markAsRead(msgList,msgs);
            event2.updateMailBox();
        }
    }

    private void markAsRead(ArrayList<String> msgList, Message[] msgs) throws MessagingException {
        Arrays.sort( msgs, (m1, m2 ) -> {
            try {
                return m2.getSentDate().compareTo( m1.getSentDate() );
            } catch ( MessagingException e ) {
                throw new RuntimeException( e );
            }
        } );

        for ( Message unreadMsg : msgs ) {
            if (unreadMsg.getSubject().equals(mailSelected)) {
                unreadMsg.setFlag(Flags.Flag.SEEN, true);

            } else {
                msgList.add(unreadMsg.getSubject());
            }
        }
        inboxListView.getItems().clear();
        inboxListView.setItems(FXCollections.observableArrayList(msgList));
    }
}
