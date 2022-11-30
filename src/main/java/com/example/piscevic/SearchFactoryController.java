package com.example.piscevic;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.model.Factory;

import java.util.*;
import java.util.stream.Collectors;

public class SearchFactoryController {

    @FXML
    private TextField factoryName;

    @FXML
    private TextField factoryAddress;

    @FXML
    private TextField factoryItems;

    @FXML
    private TableView<Factory> factoryTableView;

    @FXML
    private TableColumn<Factory, String> nameTableColumn;

    @FXML
    private TableColumn<Factory, String> addressTableColumn;

    @FXML
    private TableColumn<Factory, String> itemsTableColumn;

    @FXML
    public void initialize() {

        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        addressTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().toString()));
        itemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItems().toString()));

        ObservableList<Factory> factoryObservableList = FXCollections.observableList(ProductionApplication.factories);

        factoryTableView.setItems(factoryObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {

        String nameFactory = factoryName.getText().toLowerCase();
        String addressFactory = factoryAddress.getText().toLowerCase();
        String itemString = factoryItems.getText().toLowerCase();

        List<Factory> filteredItemsList = new ArrayList<>(ProductionApplication.factories);

        if(!Optional.ofNullable(nameFactory).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getName().toLowerCase().contains(nameFactory)).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(addressFactory).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getAddress().toString().toLowerCase().contains(addressFactory)).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(itemString).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getItems().toString().toLowerCase().contains(itemString)).collect(Collectors.toList());
        }

        factoryTableView.setItems(FXCollections.observableList(filteredItemsList));
    }
}
