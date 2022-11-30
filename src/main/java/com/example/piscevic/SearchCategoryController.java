package com.example.piscevic;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import production.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchCategoryController {

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, String> nameTableColumn;

    @FXML
    private TableColumn<Category, String> descriptionTableColumn;

    @FXML
    public void initialize() {

        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        ObservableList<Category> categoryObservableList = FXCollections.observableList(ProductionApplication.categories);
        ObservableList<String> categoryStringObservableList = FXCollections.observableList(ProductionApplication.categories.stream().map(c -> c.getName()).collect(Collectors.toList()));

        categoryChoiceBox.setItems(categoryStringObservableList);
        categoryTableView.setItems(categoryObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {

        String categoryName = categoryChoiceBox.getValue();
        String categoryDescription = descriptionTextField.getText();

        List<Category> filteredItemsList = new ArrayList<>(ProductionApplication.categories);

        if(!Optional.ofNullable(categoryName).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getName().compareToIgnoreCase(categoryName) == 0).collect(Collectors.toList());
        }

        if(!Optional.ofNullable(categoryDescription).isEmpty()) {
            filteredItemsList = filteredItemsList.stream().filter(i -> i.getDescription().toLowerCase().contains(categoryDescription)).collect(Collectors.toList());
        }

        categoryTableView.setItems(FXCollections.observableList(filteredItemsList));
    }
}
