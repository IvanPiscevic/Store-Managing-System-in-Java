package com.example.piscevic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.database.Database;
import production.exception.InsertCategoryException;
import production.exception.InsertItemException;
import production.model.*;
import production.threads.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProductionApplication extends Application {

    private static Stage mainStage;
    public static Optional<Connection> connection;
    public static Database database = new Database();

    public static final Logger logger = LoggerFactory.getLogger(ProductionApplication.class);

    public static List<Category> categories = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Address> addresses = new ArrayList<>();
    public static List<Factory> factories = new ArrayList<>();
    public static List<Store> stores = new ArrayList<>();

    public static final int NUM_OF_THREADS = 15;

    @Override
    public void start(Stage stage) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

        ConnectToDatabaseThread connectToDatabaseThread = new ConnectToDatabaseThread();
        DisconnectFromDatabaseThread disconnectFromDatabaseThread = new DisconnectFromDatabaseThread();

        GetAllCategoriesThread getAllCategoriesThread = new GetAllCategoriesThread();
        GetAllItemsThread getAllItemsThread = new GetAllItemsThread();
        GetAllAddressesThread getAllAddressesThread = new GetAllAddressesThread();
        GetAllFactoriesThread getAllFactoriesThread = new GetAllFactoriesThread();
        GetFactoryItemsThread getFactoryItemsThread = new GetFactoryItemsThread();
        GetAllStoresThread getAllStoresThread = new GetAllStoresThread();
        GetStoreItemsThread getStoreItemsThread = new GetStoreItemsThread();

        // Establishes connection with the Database
        executorService.execute(connectToDatabaseThread);

        try {
            while(!ConnectToDatabaseThread.successFlag) {
                TimeUnit.MILLISECONDS.sleep(500);
                if (!ConnectToDatabaseThread.successFlag) {
                    //System.out.println("<ERROR> Failed connection to the Database. Retrying in 5 seconds.");
                    executorService.execute(connectToDatabaseThread);
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        try {
            if (connection.isPresent()) {
                // Acquires Categories from Database
                executorService.execute(getAllCategoriesThread);
                TimeUnit.MILLISECONDS.sleep(500);

                // Acquires Items from Database
                executorService.execute(getAllItemsThread);

                // Acquires Addresses from Database
                executorService.execute(getAllAddressesThread);
                TimeUnit.MILLISECONDS.sleep(500);

                // Acquires Factories and Stores from Database
                executorService.execute(getAllFactoriesThread);
                executorService.execute(getAllStoresThread);

                TimeUnit.MILLISECONDS.sleep(500);

                // Acquires Items for Factories and Stores from Database
                executorService.execute(getFactoryItemsThread);
                TimeUnit.MILLISECONDS.sleep(100);
                executorService.execute(getStoreItemsThread);

                // Acquire a single Item from Database using Item id (Works for every Object defined in the database).
                GetOneItemThread getOneItemThread = new GetOneItemThread(1L);
                executorService.execute(getOneItemThread);
                TimeUnit.MILLISECONDS.sleep(100);
                Optional<Item> foundItem = getOneItemThread.getNewItem();
                System.out.println("<DEBUG> Was Item the retrieval successful? - " + foundItem.isPresent());

                // Acquire a single Category from Database using Category id (Works for every Object defined in the database).
                GetOneCategoryThread getOneCategoryThread = new GetOneCategoryThread(1L);
                executorService.execute(getOneCategoryThread);
                TimeUnit.MILLISECONDS.sleep(100);
                Optional<Category> foundCategory = getOneCategoryThread.getNewCategory();
                System.out.println("<DEBUG> Was Category the retrieval successful? - " + foundCategory.isPresent());

                // Acquire a single Category from Database using Category id (Works for every Object defined in the database).
                GetOneAddressThread getOneAddressThread = new GetOneAddressThread(1L);
                executorService.execute(getOneAddressThread);
                TimeUnit.MILLISECONDS.sleep(100);
                Optional<Address> foundAddress = getOneAddressThread.getNewAddress();
                System.out.println("<DEBUG> Was Address the retrieval successful? - " + foundAddress.isPresent());

                // Acquire a single Category from Database using Category id (Works for every Object defined in the database).
                GetOneFactoryThread getOneFactoryThread = new GetOneFactoryThread(1L);
                executorService.execute(getOneFactoryThread);
                TimeUnit.MILLISECONDS.sleep(100);
                Optional<Factory> foundFactory = getOneFactoryThread.getNewFactory();
                System.out.println("<DEBUG> Was Factory the retrieval successful? - " + foundFactory.isPresent());

                // Acquire a single Category from Database using Category id (Works for every Object defined in the database).
                GetOneStoreThread getOneStoreThread = new GetOneStoreThread(1L);
                executorService.execute(getOneStoreThread);
                TimeUnit.MILLISECONDS.sleep(100);
                Optional<Store> foundStore = getOneStoreThread.getNewStore();
                System.out.println("<DEBUG> Was Store the retrieval successful? - " + foundStore.isPresent());

            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            logger.error("<ERROR> Error occurred during initialization of items.", ex);
        }


        // Disconnects from Database
        if (ConnectToDatabaseThread.successFlag) {
            executorService.execute(disconnectFromDatabaseThread);
        }

        /*
        stage.setOnCloseRequest(e->{
            try {
                Database.disconnectFromDatabase(connection);
                System.out.println("<SUCCESS> Closed connection with database.");
                logger.info("<SUCCESS> Closed connection with database.");
            } catch (Exception exc) {
                System.err.println("<ERROR> Couldn't close connection with database.");
                logger.info("<ERROR> Couldn't close connection with database.");
            }
        });
         */

        // Sorts All Items (High -> Low)
        SortingItemsThread sortingItemsThread = new SortingItemsThread();
        executorService.execute(sortingItemsThread);
        executorService.shutdown();

        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("firstScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Production application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return mainStage;
    }

    public static void setStageTitle(String newTitle) {
        mainStage.setTitle(newTitle);
    }

    public static boolean containsItem(List<Item> itemList, Item item) throws InsertItemException {

        Optional<Item> itemOptional = itemList.stream().filter(value -> value.getName().equalsIgnoreCase(item.getName())).findAny();

        if (itemOptional.isPresent()) {
            throw new InsertItemException("Picked Item already exists in a list. Please pick a different Item.");
        } else {
            return false;
        }
    }
    public static boolean containsCategory(String name) throws InsertCategoryException {

        Optional<Category> categoryOptional = ProductionApplication.categories.stream().filter(value -> value.getName().equalsIgnoreCase(name)).findAny();

        if (categoryOptional.isPresent()) {
            throw new InsertCategoryException("Inserted Category already exists. Please enter a different category.");
        } else {
            return false;
        }
    }
}