package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetAllFactoriesThread implements Runnable {

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.factories = ProductionApplication.database
                            .getAllFactoriesFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all Factories from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully Factories all items from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Factories from database.");
                ProductionApplication.logger.info("<ERROR> Failed Factories Items from database.");
                ex.printStackTrace();
            }
        }
    }
}
