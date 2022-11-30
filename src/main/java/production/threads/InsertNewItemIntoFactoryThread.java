package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Factory;
import production.model.Item;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewItemIntoFactoryThread implements Runnable {

    private Item item;
    private Factory factory;
    public volatile boolean successFlag = false;

    public InsertNewItemIntoFactoryThread(Factory newFactory, Item newItem) {
        this.factory = newFactory;
        this.item = newItem;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewItemIntoFactoryDatabase(factory, item);
            System.out.println("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + factory.getId() + ". database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + factory.getId() + ". database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + factory.getId() + ". database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Item number: " + item.getId() + ". into Factory number: " + factory.getId() + ". database.");
            ex.printStackTrace();
        }
    }

}
