package com.example.piscevic;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import production.model.*;
import production.threads.SortingItemsThread;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchItemsController {

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableColumn<Item, String> nameTableColumn;

    @FXML
    private TableColumn<Item, String> categoryTableColumn;

    @FXML
    private TableColumn<Item, String> widthTableColumn;

    @FXML
    private TableColumn<Item, String> heightTableColumn;

    @FXML
    private TableColumn<Item, String> lengthTableColumn;

    @FXML
    private TableColumn<Item, String> productionCostTableColumn;

    @FXML
    private TableColumn<Item, String> sellingPriceTableColumn;

    public static Timeline mostExpensiveItemDisplay;

    public static final int NUM_OF_THREADS = 1;

    @FXML
    public void initialize() {

        // Sorts All Items (High -> Low)
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        SortingItemsThread sortingItemsThread = new SortingItemsThread();
        executorService.execute(sortingItemsThread);
        executorService.shutdown();

        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        categoryTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        widthTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWidth().toString()));
        heightTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeight().toString()));
        lengthTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLength().toString()));
        productionCostTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductionCost().toString()));
        sellingPriceTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSellingPrice().toString()));

        ObservableList<Item> itemObservableList = FXCollections.observableList(ProductionApplication.items);
        ObservableList<String> categoryObservableList = FXCollections.observableList(ProductionApplication.categories.stream().map(c -> c.getName()).collect(Collectors.toList()));

        categoryChoiceBox.setItems(categoryObservableList);
        itemTableView.setItems(itemObservableList);

        mostExpensiveItemDisplay = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProductionApplication.setStageTitle("Item with the highest price: " + ProductionApplication.items.get(0).getName());
            }
        }), new KeyFrame(Duration.seconds(1)));
        mostExpensiveItemDisplay.setCycleCount(Animation.INDEFINITE);
        mostExpensiveItemDisplay.play();
    }

    @FXML
    protected void onSearchButtonClick() {

        String itemName = nameTextField.getText().toLowerCase();
        String categoryName = categoryChoiceBox.getValue();

        List<Item> filteredItemsList = new ArrayList<>(ProductionApplication.items);

        if(!Optional.ofNullable(itemName).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getName().toLowerCase().contains(itemName)).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(categoryName).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getCategory().getName().compareToIgnoreCase(categoryName) == 0).collect(Collectors.toList());
        }

        itemTableView.setItems(FXCollections.observableList(filteredItemsList));
    }
}
