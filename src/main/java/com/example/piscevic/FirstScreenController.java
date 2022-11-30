package com.example.piscevic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;

public class FirstScreenController {

    @FXML
    public void showItemSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("itemSearch.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void showCategorySearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("categorySearch.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void showFactorySearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("factorySearch.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void showStoreSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("storeSearch.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void addNewItemScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("addNewItemScreen.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void addNewCategoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("addNewCategoryScreen.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void addNewFactoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("addNewFactoryScreen.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void addNewStoreScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("addNewStoreScreen.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }

    @FXML
    public void addNewAddressScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductionApplication.class.getResource("addNewAddressScreen.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProductionApplication.getStage().setScene(scene);
        ProductionApplication.getStage().show();
    }
}