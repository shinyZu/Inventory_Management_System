package lk.ijse.im_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.util.LoadListItemEvent;
import lk.ijse.im_system.controller.util.Validation;
import lk.ijse.im_system.model.Category;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class NewCategoryFormController {

    public AnchorPane contextNewCategory;
    public TextField txtCategoryId;
    public TextField txtCategoryTitle;

    public JFXButton btnCancel;

    private LoadListItemEvent event;

    public void initialize(){
        try {
            txtCategoryId.setText(CategoryController.getNextID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        txtCategoryTitle.setOnKeyReleased(event1 -> {
            validateField();
        });
    }

    boolean isValidTitle = false;

    private void validateField() {
        isValidTitle = Pattern.matches("^[A-Z][a-z/ ]+[A-Z][a-z]+|[A-Z][a-z]+$", txtCategoryTitle.getText());

        if (isValidTitle) {
            txtCategoryTitle.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");
        } else {
            txtCategoryTitle.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
        }
    }

    public void saveCategoryOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (txtCategoryTitle.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,"No Category Title is specified",ButtonType.OK).show();
            return;
        }

        if (isValidTitle) {

            Category category = new Category(txtCategoryId.getText(), txtCategoryTitle.getText());

            if (!CategoryController.isCategoryExists(category)) {

                if (CategoryController.addCategory(category)) {
                    event.loadCategoryList(category.getCategoryTitle());

                    new Alert(Alert.AlertType.CONFIRMATION, "New Category Added Successfully.", ButtonType.OK).show();
                    clearFields();
                    txtCategoryId.setText(CategoryController.getNextID());
                    txtCategoryTitle.setStyle("-fx-background-color: transparent; -fx-border-color: #636e72; -fx-border-radius: 3;");

                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Save New Category.\nTry Again", ButtonType.OK).show();
                }

            } else {
                new Alert(Alert.AlertType.WARNING, "A Category already exist with same name.\nTry another name", ButtonType.OK).show();
            }

        } else {
            new Alert(Alert.AlertType.WARNING,"Category Title not accepted.\nTry format(Abcd Efgh / Abcd)",ButtonType.OK).show();
        }

    }

    private void clearFields() {
        txtCategoryId.clear();
        txtCategoryTitle.clear();
    }

    public void setEvent(CategoryFormController event) { // to catch
        this.event = event;
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
