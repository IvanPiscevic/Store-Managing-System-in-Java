package com.example.piscevic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import production.database.Database;
import production.model.Category;
import production.model.Discount;
import production.model.Item;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;
import production.threads.InsertNewItemThread;
import production.threads.SortingItemsThread;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddNewItemController {

    public static final int NUM_OF_FIELDS = 6;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField productionCostTextField;

    @FXML
    private TextField sellingPriceTextField;

    @FXML
    private TextField discountTextField;

    @FXML
    public void initialize() {
        ObservableList<Category> categoryObservableList = FXCollections.observableList(ProductionApplication.categories);
        categoryChoiceBox.setItems(categoryObservableList);
    }

    private final int NUM_OF_THREADS = 3;

    @FXML
    public void onSaveItemButtonClick() {

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information Dialog");
        informationAlert.setHeaderText("Information dialog for a new Item");
        informationAlert.setContentText("Item added successfully!");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");
        errorAlert.setContentText("An exception has occurred during item add action.");

        String errorMessage = "";

        int correctInputCounter = 0;

        Optional<String> name = Optional.ofNullable(nameTextField.getText());
        Optional<Category> category = Optional.ofNullable(categoryChoiceBox.getValue());
        Optional<BigDecimal> width = Optional.of(BigDecimal.ZERO);
        Optional<BigDecimal> height = Optional.of(BigDecimal.ZERO);
        Optional<BigDecimal> length = Optional.of(BigDecimal.ZERO);
        Optional<BigDecimal> productionCost = Optional.of(BigDecimal.ZERO);
        Optional<BigDecimal> sellingPrice = Optional.of(BigDecimal.ZERO);
        Optional<BigDecimal> discountAmount = Optional.of(BigDecimal.ZERO);

        if (nameTextField.getText().isEmpty() || category.isEmpty()) {
            errorMessage += "Name TextField or Category Choice box is empty!\n";
        }

        try {
            width = Optional.of(new BigDecimal(widthTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (widthTextField.getText().isEmpty()) {
                errorMessage += "Width TextField is Empty!\n";
            } else {
                widthTextField.setText("");
                errorMessage += "Invalid width value input!\n";
            }
            widthTextField.setText("");
        }

        try {
            height = Optional.of(new BigDecimal(heightTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (heightTextField.getText().isEmpty()) {
                errorMessage += "Height TextField is Empty!\n";
            } else {
                heightTextField.setText("");
                errorMessage += "Invalid height value input!\n";
            }
            heightTextField.setText("");
        }

        try {
            length = Optional.of(new BigDecimal(lengthTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (lengthTextField.getText().isEmpty()) {
                errorMessage += "Length TextField is Empty!\n";
            } else {
                lengthTextField.setText("");
                errorMessage += "Invalid length value input!\n";
            }
            lengthTextField.setText("");
        }

        try {
            productionCost = Optional.of(new BigDecimal(productionCostTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (productionCostTextField.getText().isEmpty()) {
                errorMessage += "Production Cost TextField is Empty!\n";
            } else {
                productionCostTextField.setText("");
                errorMessage += "Invalid Production Cost value input!\n";
            }
            productionCostTextField.setText("");
        }

        try {
            sellingPrice = Optional.of(new BigDecimal(sellingPriceTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (sellingPriceTextField.getText().isEmpty()) {
                errorMessage += "Selling Price TextField is Empty!\n";
            } else {
                sellingPriceTextField.setText("");
                errorMessage += "Invalid Selling Price value input!\n";
            }
            sellingPriceTextField.setText("");
        }

        try {
            discountAmount = Optional.of(new BigDecimal(discountTextField.getText()));
            correctInputCounter++;
        } catch (NumberFormatException ex) {
            if (discountTextField.getText().isEmpty()) {
                errorMessage += "Discount TextField is Empty!\n";
            } else {
                errorMessage += "Invalid Discount value input!\n";
            }
            discountTextField.setText("");
        }

        if(correctInputCounter == NUM_OF_FIELDS && name.isPresent() && category.isPresent()) {
            executorService.execute(connectToDatabaseThread);
            Item newItem = new Item(ProductionApplication.items.get(ProductionApplication.items.size() - 1).getId() + 1,
                    name.get(), category.get(), width.get(), height.get(), length.get(), productionCost.get(), sellingPrice.get(), new Discount(discountAmount.get()));

            InsertNewItemThread insertNewItemThread = new InsertNewItemThread(newItem);
            executorService.execute(insertNewItemThread);
            ProductionApplication.items.add(newItem);
            //ProductionApplication.serializeItems(ProductionApplication.items);
            informationAlert.showAndWait();
        } else {
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        }
        executorService.execute(disconnectFromDatabaseThread);
        executorService.shutdown();
    }
}
