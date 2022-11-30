package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Item;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewItemThread implements Runnable{

    private Item item;
    public volatile boolean successFlag = false;

    public InsertNewItemThread(Item newItem) {
        this.item = newItem;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewItemToDatabase(item);
            System.out.println("<SUCCESS> Successfully added new Item into database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Item into database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<ERROR> Failed adding new Item to database.");
            ProductionApplication.logger.info(("<ERROR> Failed adding new Item to database."));
            ex.printStackTrace();
        }
    }
}
