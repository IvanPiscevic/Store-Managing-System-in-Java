package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetStoreItemsThread implements Runnable {

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.database.getStoreItemsFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all Store Items from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully acquired all Store Items from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Store Items from database.");
                ProductionApplication.logger.info("<ERROR> Failed getting Store Items from database.");
                ex.printStackTrace();
            }
        }
    }

}
