package lk.ijse.im_system.controller;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.controller.util.DateTime;

import java.sql.SQLException;

public class DashBoardInchargeFormController {

    public Label lblSurgicalItems;
    public Label lblCategory;
    public Label lblOrders;
    public Label lblRestock;
    public Label lblWoodenFurniture;
    public Label lblInventory;
    public Label lblCondemnations;
    public Label lblMetalFurniture;
    public Label lblLocation;
    public Label lblLinen;

    public BarChart<String,Integer> barChart;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;

    String loggedWardNo = MainFormController.loggedWardNo;

    public void initialize(){

        try {
            loadDashBoardLabels();
            loadBarChart();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadBarChart() throws SQLException, ClassNotFoundException {
        XYChart.Series<String,Integer> chart = new XYChart.Series<>();
        barChart.getData().addAll(InventoryWithWardController.setValuesToBarChart(chart,loggedWardNo));
    }


    private void loadDashBoardLabels() throws SQLException, ClassNotFoundException {

        if (InventoryWithWardController.isInventoryExistsWithWard(loggedWardNo)) {

            lblSurgicalItems.setText(InventoryWithWardController.getSurgicalItemCount(loggedWardNo));
            lblLinen.setText(InventoryWithWardController.getLinenCount(loggedWardNo));
            lblWoodenFurniture.setText(InventoryWithWardController.getWoodenFurnCount(loggedWardNo));
            lblMetalFurniture.setText(InventoryWithWardController.getMetalFurnCount(loggedWardNo));
            lblCategory.setText(CategoryController.getCategoryCountInWard(loggedWardNo));
            lblInventory.setText(InventoryWithWardController.getInventoryCount(loggedWardNo));
            lblLocation.setText(LocationController.getLocationCountOfWard(loggedWardNo));

            String date = DateTime.date;
            String month = date.split("-")[1];

            lblOrders.setText(OrderController.getMonthlyOrderCountOfWard(loggedWardNo, month));
            lblCondemnations.setText(CondemnController.getMonthlyCondemnCount(loggedWardNo, month));

            lblRestock.setText(InventoryWithWardController.getInvBelowLevelCount(loggedWardNo));
        }
    }
}
