package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetAllAddressesThread implements Runnable {

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.addresses = ProductionApplication.database
                            .getAllAddressesFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all addresses from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully acquired all addresses from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Addresses from database.");
                ProductionApplication.logger.info("<ERROR> Failed getting Addresses from database.");
                ex.printStackTrace();
            }
        }
    }
}
