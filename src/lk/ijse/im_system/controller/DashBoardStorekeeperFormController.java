package lk.ijse.im_system.controller;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.ijse.im_system.controller.controller_db.*;
import lk.ijse.im_system.controller.util.DateTime;

import java.sql.SQLException;

public class DashBoardStorekeeperFormController {

    public Label lblSurgicalItems;
    public Label lblCategory;
    public Label lblOrders;
    public Label lblEmployees;
    public Label lblWoodenFurniture;
    public Label lblInventory;
    public Label lblCondemnations;
    public Label lblMetalFurniture;
    public Label lblWards;
    public Label lblLinen;

    public BarChart<String,Integer> barChart;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;

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

        /*Node n = barChart.lookup(".data0.chart-bar");
        n.setStyle("-fx-bar-fill: red");
        n = barChart.lookup(".data1.chart-bar");
        n.setStyle("-fx-bar-fill: blue");
        n = barChart.lookup(".data2.chart-bar");
        n.setStyle("-fx-bar-fill: green");
        n = barChart.lookup(".data3.chart-bar");
        n.setStyle("-fx-bar-fill: orange");*/

        barChart.getData().addAll(InventoryController.setValuesToBarChart(chart));
    }

    private void loadDashBoardLabels() throws SQLException, ClassNotFoundException {

        lblSurgicalItems.setText(InventoryController.getSurgicalItemCount());
        lblLinen.setText(InventoryController.getLinenCount());
        lblWoodenFurniture.setText(InventoryController.getWoodenFurnCount());
        lblMetalFurniture.setText(InventoryController.getMetalFurnCount());
        lblCategory.setText(CategoryController.getCategoryCount());
        lblInventory.setText(InventoryController.getInventoryCount());
        lblWards.setText(WardController.getWardCount());
        lblOrders.setText(OrderController.getDailyOrderCount(DateTime.date));
        lblCondemnations.setText(CondemnController.getDailyCondemnCount(DateTime.date));
        lblEmployees.setText(StorekeeperController.getStorekeeperCount());
    }
}
