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
import production.model.Address;
import production.model.Item;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;
import production.threads.InsertNewAddressThread;
import production.threads.InsertNewCategoryThread;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNewAddressController {

    @FXML
    private ChoiceBox<City> cityChoiceBox;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    public void initialize() {
        ObservableList<City> cityObservableList = FXCollections.observableList(Arrays.stream(City.values()).toList());
        cityChoiceBox.setItems(cityObservableList);
    }

    private final int NUM_OF_THREADS = 3;

    @FXML
    public void onAddButtonClick() {

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information Dialog");
        informationAlert.setHeaderText("Information dialog for a new Address.");
        informationAlert.setContentText("Address added successfully!");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Dialog");
        confirmationAlert.setHeaderText("Confirmation Dialog for adding new Address.");
        confirmationAlert.setContentText("Are you sure you want to add this address?");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");
        errorAlert.setContentText("An exception has occurred during Address add action.");

        String errorMessage = "";

        Optional<City> cityOptional = Optional.ofNullable(cityChoiceBox.getValue());
        Optional<String> streetOptional = Optional.of(streetTextField.getText());
        Optional<String> houseNumberOptional = Optional.of(houseNumberTextField.getText());

        if (streetTextField.getText().isBlank()) {
            errorMessage += "Street Text Field cannot be blank!\n";
            streetOptional = Optional.empty();
        }

        if (houseNumberTextField.getText().isBlank()) {
            errorMessage += "House Number Text Field cannot be blank!\n";
            houseNumberOptional = Optional.empty();
        }

        if (cityOptional.isEmpty()) {
            errorMessage += "City Choice Box cannot be blank!\n";
            cityOptional = Optional.empty();
        }

        if (streetOptional.isPresent() && houseNumberOptional.isPresent() && cityOptional.isPresent()) {
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent()) {
                executorService.execute(connectToDatabaseThread);
                Address newAddress = new Address.Builder()
                        .withStreet(streetOptional.get())
                        .withHouseNumber(houseNumberOptional.get())
                        .withCity(cityOptional.get())
                        .build();

                InsertNewAddressThread insertNewAddressThread = new InsertNewAddressThread(newAddress);
                executorService.execute(insertNewAddressThread);

                ProductionApplication.addresses.add(newAddress);
                informationAlert.showAndWait();
            } else {
                errorAlert.setContentText(errorMessage);
                errorAlert.showAndWait();
            }
        }
        executorService.execute(disconnectFromDatabaseThread);
        executorService.shutdown();
    }
}
