package lk.ijse.im_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.WardController;
import lk.ijse.im_system.model.Inventory;
import lk.ijse.im_system.model.InventoryWithWard;
import lk.ijse.im_system.view.table_model.InventoryInWardTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationFormController {

    public ListView notificationListView;
    public Label lblStoreWardMail;

    String storeMail;
    String storeMailPwd;

    String wardMail;
    String wardMailPwd;

    String loggedWardNo = MainFormController.loggedWardNo;

    Object event;

    public void initialize() {

        if (loggedWardNo == null) { // if a Storekeeper is logged in
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

    List<InventoryWithWard> remainingQtyListOfWard = new ArrayList();
    List<Inventory> remainingQtyListOfStore = new ArrayList();

    ObservableList<String> notifications = FXCollections.observableArrayList();

    public void loadNotifications() throws SQLException, ClassNotFoundException {

        if (loggedWardNo != null) { // if a Storekeeper is logged in
            remainingQtyListOfWard = InventoryWithWardController.getInventoryDetailsForRestock(loggedWardNo,remainingQtyListOfWard);

            for (InventoryWithWard inv : remainingQtyListOfWard) {
                notifications.add("You have only "+inv.getAfterQty()+" remaining from Inventory "+inv.getInvNo());
            }

        } else {
            remainingQtyListOfStore = InventoryController.getInventoryDetailsForRestock(remainingQtyListOfStore);

            for (Inventory inv : remainingQtyListOfStore) {
                notifications.add("You have only "+inv.getQtyOnHand()+" remaining from Inventory "+inv.getInventoryNo());
            }
        }
        notificationListView.setItems(notifications);
    }

    public void setEvent(Object event) {
        this.event = event;
    }
}
