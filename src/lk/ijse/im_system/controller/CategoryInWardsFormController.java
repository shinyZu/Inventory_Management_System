package lk.ijse.im_system.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.im_system.controller.controller_db.CategoryController;
import lk.ijse.im_system.controller.controller_db.InventoryController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.controller.controller_db.LocationController;
import lk.ijse.im_system.controller.util.LoadTableEvent;
import lk.ijse.im_system.model.Category;
import lk.ijse.im_system.view.table_model.Category_InventoryTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryInWardsFormController implements LoadTableEvent {
    //public TreeView<String> treeView;

    public ListView<String> listViewCategory;
    public Label lblCategoryId;
    public Label lblCategoryName;

    public TableView<Category_InventoryTM> tblCategoryInWard;
    public TableColumn colInvNo;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colLocation;

    String loggedWardNo = MainFormController.loggedWardNo;
    String categorySelected;

    public void initialize() {
        try {
            initTable();
            loadCategories();

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

    }

    private void initTable() throws SQLException, ClassNotFoundException {
        colInvNo.setCellValueFactory(new PropertyValueFactory<>("inventoryNo"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadTable();
    }

    ObservableList<Category_InventoryTM> wardInventoryList = FXCollections.observableArrayList();

    @Override
    public void loadTable() throws SQLException, ClassNotFoundException { // loads the table with all Inventory in the Ward
        wardInventoryList = InventoryWithWardController.getAllInventoryOfWard(wardInventoryList,loggedWardNo);
        tblCategoryInWard.setItems(wardInventoryList);
    }

    private void filterInventoryToTable(String categorySelected) throws SQLException, ClassNotFoundException { // loads Inventory for only selected Category
        setPageTitle();
        wardInventoryList.clear();

        if (categorySelected.equals("All")) {
            loadTable();
            lblCategoryId.setText("CG-000");
            lblCategoryName.setText("Category");

        } else {
            wardInventoryList = InventoryWithWardController.getInventoryCategoryWise(loggedWardNo,categorySelected,wardInventoryList);
            tblCategoryInWard.setItems(wardInventoryList);
        }

    }

    private void setPageTitle() throws SQLException, ClassNotFoundException {
        lblCategoryId.setText(CategoryController.getCategoryId(categorySelected));
        lblCategoryName.setText(categorySelected);
    }

    List<String> categoryList = new ArrayList<>();

    private void loadCategories() throws SQLException, ClassNotFoundException { // loads Category Names to the listView
        categoryList.add("All");
        categoryList = CategoryController.getAllCategories(categoryList);

        for (String categoryTitle : categoryList ) {
            listViewCategory.getItems().add(categoryTitle);
        }
    }

    public void refreshTableOnClick(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        wardInventoryList.clear();
        loadTable();
        lblCategoryId.setText("CG-000");
        lblCategoryName.setText("Category");
    }
}
