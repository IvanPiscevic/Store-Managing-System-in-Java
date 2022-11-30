package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Factory;
import production.model.Item;
import production.model.Store;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewItemIntoStoreThread implements Runnable {

    private Item item;
    private Store store;
    public volatile boolean successFlag = false;

    public InsertNewItemIntoStoreThread(Store newStore, Item newItem) {
        this.store = newStore;
        this.item = newItem;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewItemIntoStoreDatabase(store, item);
            System.out.println("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + store.getId() + ". database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + store.getId() + ". database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + store.getId() + ". database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + store.getId() + ". database.");
            ex.printStackTrace();
        }
    }

}
