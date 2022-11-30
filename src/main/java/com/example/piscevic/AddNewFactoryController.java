package com.example.piscevic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import production.database.Database;
import production.enums.City;
import production.exception.InsertItemException;
import production.model.Address;
import production.model.Factory;
import production.model.Item;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;
import production.threads.InsertNewFactoryThread;
import production.threads.InsertNewItemIntoFactoryThread;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewFactoryController {

    @FXML
    private TextField nameTextField;

   @FXML
   private ChoiceBox<Address> addressChoiceBox;

    @FXML
    private ChoiceBox<Item> itemChoiceBox;

    @FXML
    private TextField pickedItemsTextField;

    private static final List<Item> allPickedItems = new ArrayList<>();

    @FXML
    public void initialize() {

        ObservableList<Address> addressObservableList = FXCollections.observableList(ProductionApplication.addresses);
        addressChoiceBox.setItems(addressObservableList);
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
    public void onAddFactoryButtonClick() {

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information Dialog");
        informationAlert.setHeaderText("Information dialog for a new Factory.");
        informationAlert.setContentText("Factory added successfully!");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Dialog");
        confirmationAlert.setHeaderText("Confirmation Dialog for adding new Factory.");
        confirmationAlert.setContentText("Are you sure you want to add this Factory?");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");
        errorAlert.setContentText("An exception has occurred during Factory add action.");

        String errorMessage = "";

        Optional<String> name = Optional.ofNullable(nameTextField.getText());
        Optional<Address> addressOptional = Optional.ofNullable(addressChoiceBox.getValue());
        Set<Item> itemSet = new HashSet<>();

        if (nameTextField.getText().isBlank()) {
            errorMessage += "Name Text Field cannot be blank!\n";
            name = Optional.empty();
        }

        if (addressOptional.isEmpty()) {
            errorMessage += "Address Choice Box cannot be empty!\n";
            addressOptional = Optional.empty();
        }

        if (!allPickedItems.isEmpty()) {
            itemSet = new HashSet<>(allPickedItems);
        }

        if (name.isPresent() && addressOptional.isPresent()) {
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent()) {
                executorService.execute(connectToDatabaseThread);
                long lastID = ProductionApplication.factories.get(ProductionApplication.factories.size() - 1).getId() + 1;
                Factory newFactory = new Factory(lastID, name.get(), addressOptional.get(), itemSet);
                ProductionApplication.factories.add(newFactory);
                //ProductionApplication.serializeFactories(ProductionApplication.factories);

                InsertNewFactoryThread insertNewFactoryThread = new InsertNewFactoryThread(newFactory);
                executorService.execute(insertNewFactoryThread);

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                for (Item item : itemSet) {
                    //ProductionApplication.database.insertNewItemIntoFactoryDatabase(newFactory, item);
                    InsertNewItemIntoFactoryThread insertNewItemIntoFactoryThread = new InsertNewItemIntoFactoryThread(newFactory, item);
                    executorService.execute(insertNewItemIntoFactoryThread);
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
