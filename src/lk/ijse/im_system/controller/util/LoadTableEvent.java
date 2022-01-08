package lk.ijse.im_system.controller.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import lk.ijse.im_system.controller.InchargeDashboardFormController;
import lk.ijse.im_system.controller.controller_db.InventoryWithWardController;
import lk.ijse.im_system.view.table_model.Inventory_QtyTM;
import lk.ijse.im_system.view.table_model.Order_Condemn_InventoryTM;
import lk.ijse.im_system.view.table_model.WardWise_Order_Condemn_DetailsTM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public interface LoadTableEvent{

    void loadTable() throws SQLException, ClassNotFoundException;

    // For tbl 2 in WardOrderFormController and WardCondemnInventoryFormController
    default void filterTable(TableView<Inventory_QtyTM> tblBeforeAfterQty, ObservableList<Inventory_QtyTM> qtyListWithQty, String categoryToFilter, String invToFilter, String date) throws SQLException, ClassNotFoundException {

        FilteredList<Inventory_QtyTM> filteredData = null;
        SortedList<Inventory_QtyTM> sortedData = null;
        ArrayList<Inventory_QtyTM> uniqueList = null;

        if (categoryToFilter == null && invToFilter == null && date == null) {
            // loadTable();
            return;
        }

        if(InchargeDashboardFormController.pageTitle.equals("Orders")) {
            qtyListWithQty = InventoryWithWardController.getOrderInventoryDetailsWithQty(qtyListWithQty);

        } else if(InchargeDashboardFormController.pageTitle.equals("Inventory Condemnations")){
            qtyListWithQty = InventoryWithWardController.getCondemnInventoryDetailsWithQty(qtyListWithQty);
        }

        //qtyListWithQty = InventoryWithWardController.getOrderInventoryDetailsWithQty(qtyListWithQty);

        if (categoryToFilter != null && invToFilter == null && date == null) { // if only Category is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> a.getCategory().equals(categoryToFilter)); // source , predicate

        } else if (invToFilter != null && categoryToFilter == null && date == null) { // if only an Inventory is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> a.getDescription().equals(invToFilter));

        } else if (date != null && categoryToFilter == null && invToFilter == null) { // if only Date is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> a.getLastModified().equals(date));

        }  else if (categoryToFilter != null && date != null) { // if both Category & Date is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> (a.getCategory().equals(categoryToFilter) && a.getLastModified().equals(date)));

        }else if (invToFilter != null && date != null) { // if both Inventory & Date is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> (a.getDescription().equals(invToFilter) && a.getLastModified().equals(date)));

        } else if (categoryToFilter != null && invToFilter != null) { // if both Category & Inventory is selected
            filteredData = new FilteredList<>(
                    qtyListWithQty, a -> (a.getCategory().equals(categoryToFilter) && a.getDescription().equals(invToFilter)));
        }


        sortedData = new SortedList<>(filteredData);

        /**
         * Bind sorted result with tbl
         * */

        sortedData.comparatorProperty().bind(tblBeforeAfterQty.comparatorProperty());

        /**
         * Remove duplicates from sortedData
         * */

        LinkedHashSet<Inventory_QtyTM> hashSet = new LinkedHashSet<>(sortedData);
        uniqueList = new ArrayList<>(hashSet);

        /**
         * Apply filtered and sorted data to the tbl
         * */

        tblBeforeAfterQty.setItems(FXCollections.observableArrayList(uniqueList));

    }

    // For every AddToList Button
    default ObservableList<Order_Condemn_InventoryTM> addToList(String category, String invNo, String descrip, int spinnerValue, String qtyOnHand, ObservableList<Order_Condemn_InventoryTM> listToAdd) {

        if (qtyOnHand.equals("")) {
            new Alert(Alert.AlertType.INFORMATION,"No Inventory Selected.", ButtonType.OK).show();
            return listToAdd;

        } else if (!qtyOnHand.equals("") && spinnerValue == 0) {
            new Alert(Alert.AlertType.INFORMATION,"Please Enter Order QTY",ButtonType.OK).show();
            return listToAdd;
        }

        int qtyOnHandInStore = Integer.parseInt(qtyOnHand);
        int orderQTY = spinnerValue;

        if (orderQTY > qtyOnHandInStore ) {
            new Alert(Alert.AlertType.WARNING,"Invalid Order QTY...").show();
            return listToAdd;
        }

        Order_Condemn_InventoryTM invTo_Order_Condemn = new Order_Condemn_InventoryTM(
                category,
                invNo,
                descrip,
                spinnerValue
        );

        int rowNumber = isExists(invTo_Order_Condemn,listToAdd);

        if (rowNumber == -1) { // adds the Inventory as a new record
            listToAdd.add(invTo_Order_Condemn);

        } else { // updates the existing record in the table
            Order_Condemn_InventoryTM tmInvForUpdate = listToAdd.get(rowNumber); // gets the OrderedInventoryTM object in this particular row number
            Order_Condemn_InventoryTM tmUpdatedInv = new Order_Condemn_InventoryTM(
                    category,
                    invNo,
                    descrip,
                    spinnerValue + tmInvForUpdate.getModifiedQty()
            );
            listToAdd.remove(rowNumber);
            listToAdd.add(tmUpdatedInv);
        }
        return listToAdd;
    }

    default int isExists(Order_Condemn_InventoryTM invTo_Order_Condemn, ObservableList<Order_Condemn_InventoryTM> listToAdd) { // checks whether already there has been placed an order from the particular invNo

        for (int i = 0; i < listToAdd.size(); i++) {
            if (invTo_Order_Condemn.getInvNo().equals(listToAdd.get(i).getInvNo())) {
                return i; // if already ordered
            }
        }
        return -1; // if not ordered already
    }

    // For OrderForm tbl 2 and CondemnInventoryForm tbl 2
    default void filterTableDetails(TableView<WardWise_Order_Condemn_DetailsTM> tbl_Order_Condemn, ObservableList<WardWise_Order_Condemn_DetailsTM> order_condemn_detailList, String wardToFilter, String invToFilter, String date) throws SQLException, ClassNotFoundException {

        FilteredList<WardWise_Order_Condemn_DetailsTM> filteredData = null;
        SortedList<WardWise_Order_Condemn_DetailsTM> sortedData = null;
        ArrayList<WardWise_Order_Condemn_DetailsTM> uniqueList = null;

        if (wardToFilter == null && invToFilter == null && date == null) {
            // loadTable();
            return;
        }

        if (wardToFilter != null && invToFilter == null && date == null) { // if only Ward is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> a.getWardNo().equals(wardToFilter)); // source , predicate

        } else if (invToFilter != null && wardToFilter == null && date == null) { // if only an Inventory is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> a.getDescription().equals(invToFilter));

        } else if (date != null && wardToFilter == null && invToFilter == null) { // if only Date is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> a.getDate_Ordered_Condemned().equals(date));

        }  else if (wardToFilter != null && date != null) { // if both Ward & Date is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> (a.getWardNo().equals(wardToFilter) && a.getDate_Ordered_Condemned().equals(date)));

        }else if (invToFilter != null && date != null) { // if both Inventory & Date is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> (a.getDescription().equals(invToFilter) && a.getDate_Ordered_Condemned().equals(date)));

        } else if (wardToFilter != null && invToFilter != null) { // if both Ward & Inventory is selected
            filteredData = new FilteredList<>(
                    order_condemn_detailList, a -> (a.getWardNo().equals(wardToFilter) && a.getDescription().equals(invToFilter)));
        }


        sortedData = new SortedList<>(filteredData);

        /**
         * Bind sorted result with tbl
         * */

        sortedData.comparatorProperty().bind(tbl_Order_Condemn.comparatorProperty());

        /**
         * Remove duplicates from sortedData
         * */

        LinkedHashSet<WardWise_Order_Condemn_DetailsTM> hashSet = new LinkedHashSet<>(sortedData);
        uniqueList = new ArrayList<>(hashSet);

        /**
         * Apply filtered and sorted data to the tbl
         * */

        tbl_Order_Condemn.setItems(FXCollections.observableArrayList(uniqueList));
    }

    // For OrderForm & CondemnInventoryForm Import List Buttons
    default void importList(TableView<Order_Condemn_InventoryTM> tblNew_Order_Condemn) {

        FileChooser fc = new FileChooser();

        fc.setTitle("Select File");
        fc.setInitialDirectory(new File("/home"));

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Documents","*.odt","*.doc"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fc.showOpenDialog(null);
        //File selectedFile = fc.showSaveDialog(null);

        if (selectedFile != null) {

            BufferedReader br = null;

            try {

                String strCurrentLine;

                FileReader fr = new FileReader(selectedFile);
                br = new BufferedReader(fr);

                String s = br.readLine();

                List<String[]> columnData = new ArrayList<>();
                while ((strCurrentLine = br.readLine()) != null) {
                    columnData.add(strCurrentLine.split(","));
                }

                List<Order_Condemn_InventoryTM> order_condemn_List = new ArrayList<>();
                for (String[] row :columnData) {
                    order_condemn_List.add(new Order_Condemn_InventoryTM(
                            row[0],
                            row[1],
                            row[2],
                            Integer.parseInt(row[3])
                    ));
                }

                tblNew_Order_Condemn.setItems(FXCollections.observableArrayList(order_condemn_List));

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

        } else {
            //System.out.println("File is not valid");
        }
    }
}
