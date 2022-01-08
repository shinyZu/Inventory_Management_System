package lk.ijse.im_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.util.FieldDataForm;
import lk.ijse.im_system.controller.util.LoadListItemEvent;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.controller.util.Notification;
import lk.ijse.im_system.view.table_model.Category_InventoryTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryFormController extends FieldDataForm implements LoadListItemEvent, LoadTableEvent {

    public AnchorPane contextCategory;
    public ListView<String> listViewCategory;

    public TableView<Category_InventoryTM> tblInventoryOfCategory;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colLocation;

    public Label lblCategoryId;
    public Label lblCategoryName;

    String categorySelected;
    Category_InventoryTM inventorySelected;

    public void initialize(){
        try {
            loadCategories();
            initTable();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        listViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            categorySelected = newValue; // selected Category from list

            try {
                filterInventoryToTable(categorySelected);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblInventoryOfCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                //
            } else {
                inventorySelected = newValue;
            }
        });
    }

    private void initTable() throws SQLException, ClassNotFoundException {
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("inventoryNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadTable();
    }

    ObservableList<Category_InventoryTM> inventoryList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException { // loads the table with all Inventory in the Store
        inventoryList.clear();
        inventoryList = InventoryController.getAllInventory(inventoryList);
        tblInventoryOfCategory.setItems(inventoryList);
    }

    @Override
    public void loadCategoryList(String categoryTitle) { // reloads the listView once a new Category is added
        listViewCategory.getItems().add(categoryTitle);
    }

    private void filterInventoryToTable(String categorySelected) throws SQLException, ClassNotFoundException { // loads Inventory for only selected Category
        inventoryList.clear();
        setPageTitle();

        if (!categoryList.isEmpty()) {
            if (categorySelected.equals("All")) {
                loadTable();
                lblCategoryId.setText("CG-000");
                lblCategoryName.setText("Category");

            } else {
                inventoryList = InventoryController.getInventoryCategoryWise(categorySelected, inventoryList);
                tblInventoryOfCategory.setItems(inventoryList);
            }
        }
    }

    private void setPageTitle() throws SQLException, ClassNotFoundException {
        lblCategoryId.setText(CategoryController.getCategoryId(categorySelected));
        lblCategoryName.setText(categorySelected);

    }

    List<String> categoryList = new ArrayList<>();
    private void loadCategories() throws SQLException, ClassNotFoundException { // loads Category Names to the listView
        categoryList.clear();
        listViewCategory.getItems().clear();

        categoryList.add("All");
        categoryList = CategoryController.getAllCategories(categoryList);

        for (String categoryTitle : categoryList ) {
            listViewCategory.getItems().add(categoryTitle);
        }
    }

    public void addNewCategoryOnAction(ActionEvent actionEvent) throws IOException {
        /*URL resource = getClass().getResource("../view/NewCategoryForm.fxml");
        Parent load = FXMLLoader.load(resource); // cannot do with static class
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();*/

        /**
         * should create a object of FXMLLoader without using the static class
         * */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewCategoryForm.fxml"));
        Parent parent = loader.load();

        NewCategoryFormController controller = loader.getController();
        controller.setEvent(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();

    }

    public void removeCategoryFromStore(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ButtonType yes= new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no= new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Category from Store?", yes, no);
        alert.setTitle("Confirmation Alert");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {

            if (CategoryController.deleteCategory(CategoryController.getCategoryId(categorySelected))) {
                loadTable();
                loadCategories();
                //Notification.showSuccessMsg("lk/ijse/im_system/view/assets/images/ok.png","Category deleted successfully!");
                new Alert(Alert.AlertType.CONFIRMATION,"Category deleted successfully!",ButtonType.OK).show();

            } else {
                //Notification.showAlertMsg("lk/ijse/im_system/view/assets/images/alert_glass.png","Failed to delete Category. Try Again...");
                new Alert(Alert.AlertType.WARNING,"Failed to delete Category. Try Again...",ButtonType.OK).show();
            }
        }
    }

    public void addNewLocationOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewLocationForm.fxml"));
        Parent parent = loader.load();

        NewLocationFormController controller = loader.getController();
        controller.setEvent2(this);

        Stage window = new Stage();
        window.setScene(new Scene(parent));
        //window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }
}
