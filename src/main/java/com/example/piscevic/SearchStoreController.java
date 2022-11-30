package com.example.piscevic;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import production.model.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchStoreController {

    @FXML
    private TextField storeName;

    @FXML
    private TextField storeAddress;

    @FXML
    private TextField storeItems;

    @FXML
    private TableView<Store> storeTableView;

    @FXML
    private TableColumn<Store, String> nameTableColumn;

    @FXML
    private TableColumn<Store, String> webAddressTableColumn;

    @FXML
    private TableColumn<Store, String> itemsTableColumn;

    @FXML
    public void initialize() {

        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        webAddressTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWebAddress()));
        itemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItems().toString()));

        ObservableList<Store> factoryObservableList = FXCollections.observableList(ProductionApplication.stores);

        storeTableView.setItems(factoryObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {

        String nameStore = storeName.getText().toLowerCase();
        String addressStore = storeAddress.getText().toLowerCase();
        String itemString = storeItems.getText().toLowerCase();

        List<Store> filteredItemsList = new ArrayList<>(ProductionApplication.stores);

        if(!Optional.ofNullable(nameStore).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getName().toLowerCase().contains(nameStore)).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(addressStore).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getWebAddress().toLowerCase().contains(addressStore)).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(itemString).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getItems().toString().toLowerCase().contains(itemString)).collect(Collectors.toList());
        }

        storeTableView.setItems(FXCollections.observableList(filteredItemsList));
    }

}
