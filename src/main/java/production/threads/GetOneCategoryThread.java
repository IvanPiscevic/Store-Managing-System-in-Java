package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Category;

import java.sql.SQLException;
import java.util.Optional;

public class GetOneCategoryThread implements Runnable {

    Long id;
    Optional<Category> newCategory;

    public GetOneCategoryThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (ProductionApplication.connection.isPresent() && Database.activeConnectionWithDatabase) {
            try {
                newCategory = ProductionApplication.database.getOneCategoryFromDatabase(ProductionApplication.connection.get(), id);
                if (newCategory.isPresent()) {
                    System.out.println("<SUCCESS> Successfully received Category (id: " + id + ")");
                    ProductionApplication.logger.error("<SUCCESS> Successfully received Category (id: " + id + ")");
                } else {
                    System.out.println("<ERROR> Failed getting Category (id: " + id + "), Couldn't find Category.");
                    ProductionApplication.logger.error("<ERROR> Failed getting Category (id: " + id + "), Couldn't find Category.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("<ERROR> Failed getting Category (id: " + id + "), No Connection to the Database.");
            ProductionApplication.logger.error("<ERROR> Failed getting Category (id: " + id + "), No Connection to the Database.");
        }
    }

    public Long getId() {
        return id;
    }

    public Optional<Category> getNewCategory() {
        return newCategory;
    }
}
