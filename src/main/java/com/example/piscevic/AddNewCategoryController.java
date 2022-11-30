package com.example.piscevic;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import production.database.Database;
import production.exception.InsertCategoryException;
import production.model.Category;
import production.model.Factory;
import production.model.Item;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;
import production.threads.InsertNewCategoryThread;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewCategoryController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private final int NUM_OF_THREADS = 3;

    @FXML
    public void onSaveCategoryButtonClick() {

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information Dialog");
        informationAlert.setHeaderText("Information dialog for a new Category.");
        informationAlert.setContentText("Category added successfully!");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation Dialog");
        confirmationAlert.setHeaderText("Confirmation Dialog for adding new Category.");
        confirmationAlert.setContentText("Are you sure you want to add this Category?");

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Invalid Input Exception Dialog");
        errorAlert.setHeaderText("Exception Dialog");
        errorAlert.setContentText("An exception has occurred during category add action.");

        String errorMessage = "";

        Optional<String> name = Optional.ofNullable(nameTextField.getText());
        Optional<String> description = Optional.ofNullable(descriptionTextArea.getText());

        if(nameTextField.getText().isBlank()) {
            errorMessage += "Name text field cannot be empty!\n";
            name = Optional.empty();
        }
        if (descriptionTextArea.getText().isBlank()) {
            errorMessage += "Description text area cannot be empty!\n";
            description = Optional.empty();
        }

        try {
            if (name.isPresent() && description.isPresent()) {
                if (!ProductionApplication.containsCategory(name.get())) {
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent()) {
                        if (result.get() == ButtonType.OK) {
                            executorService.execute(connectToDatabaseThread);

                            try {
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                            long lastID = ProductionApplication.categories.get(ProductionApplication.categories.size()-1).getId() + 1;
                            Category newCategory = new Category(lastID, name.get(), description.get());
                            //ProductionApplication.serializeCategories(ProductionApplication.categories);

                            InsertNewCategoryThread insertNewCategoryThread = new InsertNewCategoryThread(newCategory);
                            executorService.execute(insertNewCategoryThread);

                            ProductionApplication.categories.add(newCategory);
                            informationAlert.showAndWait();
                        }
                    }
                }
            } else {
                errorAlert.setContentText(errorMessage);
                errorAlert.showAndWait();
            }
        } catch (InsertCategoryException ex) {
            errorAlert.setContentText(ex.getMessage());
            errorAlert.showAndWait();
        }
        executorService.execute(disconnectFromDatabaseThread);
        executorService.shutdown();
    }
}
