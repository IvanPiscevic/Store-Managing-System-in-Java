package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Category;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewCategoryThread implements Runnable {

    private Category category;
    public volatile boolean successFlag = false;

    public InsertNewCategoryThread(Category newCategory) {
        this.category = newCategory;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewCategoryToDatabase(category);
            System.out.println("<SUCCESS> Successfully added new Category into database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Category into database.");
            successFlag = true;
        } catch (SQLException ex) {
            System.out.println("<ERROR> Failed adding new Category to database.");
            ProductionApplication.logger.info(("<ERROR> Failed adding new Category to database."));
            ex.printStackTrace();
        }
    }
}
