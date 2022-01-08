package lk.ijse.im_system.controller.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.Notifications;

import javax.swing.*;

import static javafx.scene.control.ButtonBar.*;

public class Notification {

    public static void showAlertMsg(String imgURL, String msgContent) {
        Image image = new Image(imgURL);

        Notifications notifications = Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(msgContent);
        notifications.title("");
        notifications.darkStyle();
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();

    }

    public static void showSuccessMsg(String imgURL, String msgContent) {
        Image image = new Image(imgURL);

        Notifications notifications = Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(msgContent);
        notifications.title("");
        //notifications.darkStyle();
        notifications.position(Pos.TOP_RIGHT);
        notifications.show();
    }

    public static void showInfoMsg(String imgURL, String msgContent) {
        Image image = new Image(imgURL);

        Notifications notifications = Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(msgContent);
        notifications.title("");
        notifications.darkStyle();
        notifications.position(Pos.TOP_CENTER);
        notifications.show();
    }

    public static void showConfirmMsg(String imgURL, String msgContent) {
        Image image = new Image(imgURL);

        ButtonType yes= new ButtonType("Yes", ButtonData.OK_DONE);
        ButtonType no= new ButtonType("No", ButtonData.CANCEL_CLOSE);

        Notifications notifications = Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(msgContent);
        notifications.title("");
        //notifications.graphic(new Button("Yes")).position();
        //notifications.graphic(new ButtonBar(no));
        notifications.darkStyle();
        notifications.position(Pos.TOP_CENTER);
        notifications.show();
        //notifications.title("Test Title").text("Test Text Test Text").action("Yes", "Yes", "No", "No", "Cancel", "Cancel").DarkStyle.Show
    }

    public static void showErrorDialogBox(StackPane rootPane, AnchorPane anchorPane, String imgURL, String msgContent) {

        BoxBlur blur = new BoxBlur(3,3,3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        //dialogLayout.getStyleClass().add("dialog-layout");

        JFXButton button = new JFXButton("OK");
        button.getStyleClass().add("dialog-button");

        JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            dialog.close();
        });

        dialogLayout.setHeading(new ImageView(new Image(imgURL)));
        dialogLayout.setBody(new Text(msgContent));
        dialogLayout.setActions(button);
        dialog.show();

        dialog.setOnDialogClosed(event -> {
            anchorPane.setEffect(null);
        });
        anchorPane.setEffect(blur);

    }

    public static void confirmDeleteDialogBox(StackPane rootPane, AnchorPane anchorPane, String imgURL, String msgContent) {

        BoxBlur blur = new BoxBlur(3,3,3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        //dialogLayout.getStyleClass().add("dialog-layout");

        JFXButton yes = new JFXButton("Yes");
        JFXButton no = new JFXButton("No");
        yes.getStyleClass().add("dialog-button");
        no.getStyleClass().add("dialog-button");

        JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);

        no.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            dialog.close();

        });

        yes.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            dialog.close();
        });

        dialogLayout.setHeading(new ImageView(new Image(imgURL)));
        dialogLayout.setBody(new Text(msgContent));
        dialogLayout.setActions(no,yes);
        dialog.show();

        if (yes.isPressed()) {
            System.out.println("Yes button clicked");
        } else if (yes.isFocused()){
            System.out.println("Yes button focused");
        }

        dialog.setOnDialogClosed(event -> {
            anchorPane.setEffect(null);
        });
        anchorPane.setEffect(blur);

    }
}
