package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetAllItemsThread implements Runnable{

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.items = ProductionApplication.database
                            .getAllItemsFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all items from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully acquired all items from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Items from database.");
                ProductionApplication.logger.info("<ERROR> Failed getting Items from database.");
                ex.printStackTrace();
            }
        }
    }
}
