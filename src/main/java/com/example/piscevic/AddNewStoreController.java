package com.example.piscevic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import production.database.Database;
import production.exception.InsertItemException;
import production.model.Item;
import production.model.Store;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;
import production.threads.InsertNewItemIntoStoreThread;
import production.threads.InsertNewStoreThread;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewStoreController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField webAddressTextField;

    @FXML
    private ChoiceBox<Item> itemChoiceBox;

    @FXML
    private TextField pickedItemsTextField;

    private static final List<Item> allPickedItems = new ArrayList<>();

    @FXML
    public void initialize() {

        ObservableList<Item> itemObservableList = FXCollections.observableList(ProductionApplication.items);
        itemChoiceBox.setItems(itemObservableList);

        pickedItemsTextField.setEditable(false);
    }

    @FXML
    public void onAddItemButtonClick() {

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");

        Item pickedItem = itemChoiceBox.getValue();

        try {
            if (!ProductionApplication.containsItem(allPickedItems, pickedItem)) {
                allPickedItems.add(pickedItem);
                pickedItemsTextField.setText(allPickedItems.toString());
            }
        } catch (InsertItemException ex) {
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void onRemoveItemButtonClick() {

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");

        Item pickedItem = itemChoiceBox.getValue();
        Optional<Item> foundItemOptional = allPickedItems.stream().filter(value -> value.getName().equalsIgnoreCase(pickedItem.getName())).findAny();

        if (foundItemOptional.isEmpty()) {
            errorAlert.setContentText("You cant remove the item that is not inside the picked Item list.\n");
            errorAlert.showAndWait();
        } else {
            allPickedItems.remove(pickedItem);
            pickedItemsTextField.setText(allPickedItems.toString());
        }
    }

    private final int NUM_OF_THREADS = 3;

    @FXML
    public void onAddStoreButtonClick() {

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information Dialog");
        informationAlert.setHeaderText("Information dialog for a new Store.");
        informationAlert.setContentText("Store added successfully!");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Dialog");
        confirmationAlert.setHeaderText("Confirmation Dialog for adding new Store.");
        confirmationAlert.setContentText("Are you sure you want to add this Store?");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");
        errorAlert.setContentText("An exception has occurred during Store add action.");

        String errorMessage = "";

        Optional<String> name = Optional.ofNullable(nameTextField.getText());
        Optional<String> webAddress = Optional.ofNullable(webAddressTextField.getText());
        Set<Item> itemSet = new HashSet<>();

        if (nameTextField.getText().isBlank()) {
            errorMessage += "Name Text Field cannot be blank!\n";
            name = Optional.empty();
        }

        if (webAddressTextField.getText().isBlank()) {
            errorMessage += "Web Address Text Field cannot be blank!\n";
            webAddress = Optional.empty();
        }

        if (!allPickedItems.isEmpty()) {
            itemSet = new HashSet<>(allPickedItems);
        }

        if (name.isPresent() && webAddress.isPresent()) {
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent()) {
                executorService.execute(connectToDatabaseThread);
                long lastID = ProductionApplication.stores.get(ProductionApplication.stores.size() - 1).getId() + 1;
                Store newStore = new Store(lastID, name.get(), webAddress.get(), itemSet);
                ProductionApplication.stores.add(newStore);
                //ProductionApplication.serializeStores(ProductionApplication.stores);

                InsertNewStoreThread insertNewStoreThread = new InsertNewStoreThread(newStore);
                executorService.execute(insertNewStoreThread);

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                for (Item item : itemSet) {
                    //ProductionApplication.database.insertNewItemIntoStoreDatabase(newStore, item);
                    InsertNewItemIntoStoreThread insertNewItemIntoStoreThread = new InsertNewItemIntoStoreThread(newStore, item);
                    executorService.execute(insertNewItemIntoStoreThread);
                }
                informationAlert.showAndWait();
            }
        } else {
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        }
        executorService.execute(disconnectFromDatabaseThread);
        executorService.shutdown();
    }
}
