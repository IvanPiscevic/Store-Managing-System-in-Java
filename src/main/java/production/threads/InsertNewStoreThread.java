package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Store;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewStoreThread implements Runnable{

    private Store store;
    public volatile boolean successFlag = false;

    public InsertNewStoreThread(Store newStore) {
        this.store = newStore;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewStoreToDatabase(store);
            System.out.println("<SUCCESS> Successfully added new Store into database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Store into database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<ERROR> Failed adding new Store to database.");
            ProductionApplication.logger.info(("<ERROR> Failed adding new Store to database."));
            ex.printStackTrace();
        }
    }

}
