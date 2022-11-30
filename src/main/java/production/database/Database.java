package production.database;

import com.example.piscevic.ProductionApplication;
import production.enums.City;
import production.model.*;
import production.threads.ConnectToDatabaseThread;
import production.threads.DisconnectFromDatabaseThread;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Database {

    public volatile static boolean activeConnectionWithDatabase = false;
    public volatile static boolean databaseInUse = false;

    public synchronized Optional<Connection> connectToDatabase() throws SQLException, IOException {
        try {
            if (activeConnectionWithDatabase) {
                System.out.println("[DEBUG WAITING IN CONNECT TO DATABASE THREAD");
                wait();
            }

            Properties databaseConfiguration = new Properties();
            databaseConfiguration.load(new FileReader("dat/database.properties"));

            String databaseURL = databaseConfiguration.getProperty("databaseURL");
            String databaseUsername = databaseConfiguration.getProperty("databaseUsername");
            String databasePassword = databaseConfiguration.getProperty("databasePassword");

            activeConnectionWithDatabase = true;
            notifyAll();
            return Optional.of(DriverManager.getConnection(databaseURL, databaseUsername, databasePassword));

        } catch (InterruptedException ex) {
            ex.printStackTrace();
            notifyAll();
            return Optional.empty();
        }
    }
    public synchronized void disconnectFromDatabase(Connection connection) throws SQLException {
        try {
            if (activeConnectionWithDatabase == false) {
                wait();
            }
            connection.close();
            activeConnectionWithDatabase = false;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        notifyAll();
    }

    public synchronized List<Category> getAllCategoriesFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        databaseInUse = true;

        List<Category> categoryList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();
        ResultSet categoryResultSet = sqlStatement.executeQuery("SELECT * FROM CATEGORY");

        while(categoryResultSet.next()) {

            Long categoryId = categoryResultSet.getLong("ID");
            String categoryName = categoryResultSet.getString("NAME");
            String categoryDescription = categoryResultSet.getString("DESCRIPTION");

            Category newCategory = new Category(categoryId, categoryName, categoryDescription);
            categoryList.add(newCategory);
        }

        databaseInUse = false;
        notifyAll();
        return categoryList;
    }
    public synchronized List<Item> getAllItemsFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        databaseInUse = true;

        List<Item> itemList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();
        ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM ITEM");

        while(itemResultSet.next()) {
            Long itemId = itemResultSet.getLong("ID");
            String itemName = itemResultSet.getString("NAME");
            Long itemCategoryId = itemResultSet.getLong("CATEGORY_ID");
            BigDecimal itemWidth = itemResultSet.getBigDecimal("WIDTH");
            BigDecimal itemHeight = itemResultSet.getBigDecimal("HEIGHT");
            BigDecimal itemLength = itemResultSet.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

            Item newItem = new Item(itemId, itemName,
                    ProductionApplication.categories.get(itemCategoryId.intValue() - 1),
                    itemWidth, itemHeight, itemLength, itemProductionCost, itemSellingPrice,
                    new Discount(new BigDecimal(BigInteger.ZERO)));
            itemList.add(newItem);
        }

        databaseInUse = false;
        notifyAll();
        return itemList;
    }
    public synchronized List<Address> getAllAddressesFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        databaseInUse = true;

        List<Address> addressList = new ArrayList<>();
        List<City> cities = Arrays.stream(City.values()).toList();

        Statement sqlStatement = connection.createStatement();

        ResultSet addressResultSet = sqlStatement.executeQuery("SELECT * FROM ADDRESS");

        while(addressResultSet.next()) {
            Long addressId = addressResultSet.getLong("ID");
            String addressStreet = addressResultSet.getString("STREET");
            String addressHouseNumber = addressResultSet.getString("HOUSE_NUMBER");
            String addressCity = addressResultSet.getString("CITY");
            String addressPostalCode = addressResultSet.getString("POSTAL_CODE");
            Optional<City> cityOptional = cities
                    .stream()
                    .filter(value -> value.getCityName().equalsIgnoreCase(addressCity) &&
                            value.getCityPostalCode().equalsIgnoreCase(addressPostalCode))
                    .findFirst();
            if (cityOptional.isPresent()) {
                Address newAddress = new Address.Builder()
                        .withStreet(addressStreet)
                        .withHouseNumber(addressHouseNumber)
                        .withCity(cityOptional.get())
                        .build();
                addressList.add(newAddress);
            }
        }

        databaseInUse = false;
        notifyAll();
        return addressList;
    }
    public synchronized List<Factory> getAllFactoriesFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        databaseInUse = true;

        List<Factory> factoryList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet factoryResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY");

        while(factoryResultSet.next()) {
            Long factoryId = factoryResultSet.getLong("ID");
            String factoryName = factoryResultSet.getString("NAME");
            Long factoryAddressId = factoryResultSet.getLong("ADDRESS_ID");

            Factory newFactory = new Factory(factoryId, factoryName,
                    ProductionApplication.addresses.get(factoryAddressId.intValue() - 1), new HashSet<>());
            factoryList.add(newFactory);
        }

        databaseInUse = false;
        notifyAll();
        return factoryList;
    }
    public synchronized void getFactoryItemsFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        databaseInUse = true;

        Statement sqlStatement = connection.createStatement();

        ResultSet factoryItemResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY_ITEM");

        while(factoryItemResultSet.next()) {
            Long factoryId = factoryItemResultSet.getLong("FACTORY_ID");
            Long itemId = factoryItemResultSet.getLong("ITEM_ID");

            Item item = ProductionApplication.items.get(itemId.intValue() - 1);
            Set<Item> tmpSet = ProductionApplication.factories.get(factoryId.intValue() - 1).getItems();
            tmpSet.add(item);
        }

        databaseInUse = false;
        notifyAll();
    }
    public synchronized List<Store> getAllStoresFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        databaseInUse = true;

        List<Store> storeList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet storeResultSet = sqlStatement.executeQuery("SELECT * FROM STORE");

        while(storeResultSet.next()) {
            Long storeId = storeResultSet.getLong("ID");
            String storeName = storeResultSet.getString("NAME");
            String storeWebAddress = storeResultSet.getString("WEB_ADDRESS");

            Store newStore = new Store(storeId, storeName, storeWebAddress , new HashSet<>());
            storeList.add(newStore);
        }

        databaseInUse = false;
        notify();
        return storeList;
    }
    public synchronized void getStoreItemsFromDatabase(Connection connection) throws SQLException {

        try {
            while (databaseInUse)  {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        databaseInUse = true;

        Statement sqlStatement = connection.createStatement();

        ResultSet storeItemResultSet = sqlStatement.executeQuery("SELECT * FROM STORE_ITEM");

        while(storeItemResultSet.next()) {
            Long storeId = storeItemResultSet.getLong("STORE_ID");
            Long itemId = storeItemResultSet.getLong("ITEM_ID");

            Item item = ProductionApplication.items.get(itemId.intValue() - 1);
            Set<Item> tmpSet = ProductionApplication.stores.get(storeId.intValue() - 1).getItems();
            tmpSet.add(item);
        }

        databaseInUse = false;
        notifyAll();
    }

    public synchronized Optional<Item> getOneItemFromDatabase(Connection connection, Long id) throws SQLException {

        while(databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        databaseInUse = true;

        Optional<Item> itemOptional = Optional.empty();

        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM ITEM WHERE ID = ?;");
        sqlStatement.setLong(1, id);
        ResultSet newItemResultSet = sqlStatement.executeQuery();

        while (newItemResultSet.next()) {
            Long retrievedId = newItemResultSet.getLong("ID");
            itemOptional = ProductionApplication.items
                    .stream()
                    .filter(value -> value.getId().equals(retrievedId)).findAny();
        }

        sqlStatement.close();
        databaseInUse = false;
        notify();
        return itemOptional;
    }
    public synchronized Optional<Category> getOneCategoryFromDatabase(Connection connection, Long id) throws SQLException {

        while(databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        databaseInUse = true;

        Optional<Category> categoryOptional = Optional.empty();

        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM CATEGORY WHERE ID = ?;");
        sqlStatement.setLong(1, id);
        ResultSet newItemResultSet = sqlStatement.executeQuery();

        while (newItemResultSet.next()) {
            Long retrievedId = newItemResultSet.getLong("ID");
            categoryOptional = ProductionApplication.categories
                    .stream()
                    .filter(value -> value.getId().equals(retrievedId)).findAny();
        }

        sqlStatement.close();
        databaseInUse = false;
        notify();
        return categoryOptional;
    }
    public synchronized Optional<Address> getOneAddressFromDatabase(Connection connection, Long id) throws SQLException {

        while(databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        databaseInUse = true;

        Optional<Address> addressOptional = Optional.empty();

        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM ADDRESS WHERE ID = ?;");
        sqlStatement.setLong(1, id);
        ResultSet newAddressResultSet = sqlStatement.executeQuery();

        while (newAddressResultSet.next()) {
            long retrievedId = newAddressResultSet.getLong("ID");
            addressOptional = Optional.ofNullable(ProductionApplication.addresses.get((int) retrievedId - 1));
        }

        sqlStatement.close();
        databaseInUse = false;
        notify();
        return addressOptional;
    }
    public synchronized Optional<Factory> getOneFactoryFromDatabase(Connection connection, Long id) throws SQLException {

        while(databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        databaseInUse = true;

        Optional<Factory> factoryOptional = Optional.empty();

        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM FACTORY WHERE ID = ?;");
        sqlStatement.setLong(1, id);
        ResultSet newItemResultSet = sqlStatement.executeQuery();

        while (newItemResultSet.next()) {
            Long retrievedId = newItemResultSet.getLong("ID");
            factoryOptional = ProductionApplication.factories
                    .stream()
                    .filter(value -> value.getId().equals(retrievedId)).findAny();
        }

        sqlStatement.close();
        databaseInUse = false;
        notify();
        return factoryOptional;
    }
    public synchronized Optional<Store> getOneStoreFromDatabase(Connection connection, Long id) throws SQLException {

        while(databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        databaseInUse = true;

        Optional<Store> storeOptional = Optional.empty();

        PreparedStatement sqlStatement = connection.prepareStatement("SELECT * FROM STORE WHERE ID = ?;");
        sqlStatement.setLong(1, id);
        ResultSet newItemResultSet = sqlStatement.executeQuery();

        while (newItemResultSet.next()) {
            Long retrievedId = newItemResultSet.getLong("ID");
            storeOptional = ProductionApplication.stores
                    .stream()
                    .filter(value -> value.getId().equals(retrievedId)).findAny();
        }

        sqlStatement.close();
        databaseInUse = false;
        notify();
        return storeOptional;
    }

    public synchronized void insertNewCategoryToDatabase(Category newCategory) throws SQLException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        while (activeConnectionWithDatabase == false) {
            try {
                System.out.println("[DEBUG] WAITING IN INSERT CATEGORY TO DATABASE METHOD");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES(?, ?)");
            sqlStatement.setString(1, newCategory.getName());
            sqlStatement.setString(2, newCategory.getDescription());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }
    public synchronized void insertNewItemToDatabase(Item newItem) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        while (activeConnectionWithDatabase == false) {
            try {
                System.out.println("[DEBUG] WAITING IN INSERT ITEM TO DATABASE METHOD");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {

            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO ITEM (CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) " +
                            "VALUES(?, ?, ?, ? , ?, ?, ?)");

            sqlStatement.setLong(1, newItem.getCategory().getId());
            sqlStatement.setString(2, newItem.getName());
            sqlStatement.setBigDecimal(3, newItem.getWidth());
            sqlStatement.setBigDecimal(4, newItem.getHeight());
            sqlStatement.setBigDecimal(5, newItem.getLength());
            sqlStatement.setBigDecimal(6, newItem.getProductionCost());
            sqlStatement.setBigDecimal(7, newItem.getSellingPrice());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }
    public synchronized void insertNewAddressToDatabase(Address newAddress) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        while (activeConnectionWithDatabase == false) {
            try {
                System.out.println("[DEBUG] WAITING IN INSERT ADDRESS TO DATABASE METHOD");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO ADDRESS (STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) " +
                            "VALUES(?, ?, ?, ?)");

            sqlStatement.setString(1, newAddress.getStreet());
            sqlStatement.setString(2, newAddress.getHouseNumber());
            sqlStatement.setString(3, newAddress.getCity().getCityName());
            sqlStatement.setString(4, newAddress.getCity().getCityPostalCode());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }
    public synchronized void insertNewFactoryToDatabase(Factory newFactory) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        while (activeConnectionWithDatabase == false) {
            try {
                System.out.println("[DEBUG] WAITING IN INSERT FACTORY TO DATABASE METHOD");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO FACTORY (NAME, ADDRESS_ID) " +
                            "VALUES(?, ?)");

            long addressId = 1L;
            for (int i = 0; i < ProductionApplication.addresses.size(); i++) {
                if (newFactory.getAddress().getStreet().equalsIgnoreCase(ProductionApplication.addresses.get(i).getStreet()) &&
                        (newFactory.getAddress().getHouseNumber().equalsIgnoreCase(ProductionApplication.addresses.get(i).getHouseNumber()))) {
                    addressId = i + 1L;
                }
            }

            sqlStatement.setString(1, newFactory.getName());
            sqlStatement.setLong(2, addressId);
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }
    public synchronized void insertNewStoreToDatabase(Store newStore) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        while (activeConnectionWithDatabase == false) {
            try {
                System.out.println("[DEBUG] WAITING IN INSERT STORE TO DATABASE METHOD");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO STORE (NAME, WEB_ADDRESS) " +
                            "VALUES(?, ?)");

            sqlStatement.setString(1, newStore.getName());
            sqlStatement.setString(2, newStore.getWebAddress());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }

    public synchronized void insertNewItemIntoFactoryDatabase(Factory factory, Item item) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO FACTORY_ITEM (FACTORY_ID, ITEM_ID) " +
                            "VALUES(?, ?)");

            sqlStatement.setLong(1, factory.getId());
            sqlStatement.setLong(2, item.getId());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }

        databaseInUse = false;
        notify();
    }
    public synchronized void insertNewItemIntoStoreDatabase(Store store, Item item) throws SQLException, IOException {

        while (databaseInUse) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        databaseInUse = true;

        if (ProductionApplication.connection.isPresent()) {
            PreparedStatement sqlStatement = ProductionApplication.connection.get().prepareStatement(
                    "INSERT INTO STORE_ITEM (STORE_ID, ITEM_ID) " +
                            "VALUES(?, ?)");

            sqlStatement.setLong(1, store.getId());
            sqlStatement.setLong(2, item.getId());
            sqlStatement.executeUpdate();
            sqlStatement.close();
        }
        databaseInUse = false;
        notify();
    }
}

