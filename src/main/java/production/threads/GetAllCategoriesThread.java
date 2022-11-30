package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;

public class GetAllCategoriesThread implements Runnable {

    @Override
    public void run() {
        if (Database.activeConnectionWithDatabase) {
            try {
                if (ProductionApplication.connection.isPresent()) {
                    ProductionApplication.categories = ProductionApplication.database
                            .getAllCategoriesFromDatabase(ProductionApplication.connection.get());
                    System.out.println("<SUCCESS> Successfully acquired all categories from database.");
                    ProductionApplication.logger.info("<SUCCESS> Successfully acquired all categories from database.");
                }
            } catch (SQLException ex) {
                System.out.println("<ERROR> Failed getting Categories from database.");
                ProductionApplication.logger.info("<ERROR> Failed getting Categories from database.");
                ex.printStackTrace();
            }
        }
    }
}
