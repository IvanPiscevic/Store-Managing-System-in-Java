package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetAllStoresThread implements Runnable {

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.stores = ProductionApplication.database
                            .getAllStoresFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all Stores from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully Stores all items from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Stores from database.");
                ProductionApplication.logger.info("<ERROR> Failed Stores Items from database.");
                ex.printStackTrace();
            }
        }
    }

}
