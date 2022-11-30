package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Item;

import java.sql.SQLException;
import java.util.Optional;

public class GetOneItemThread implements Runnable {

    private Long id;
    private Optional<Item> newItem;

    public GetOneItemThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (ProductionApplication.connection.isPresent() && Database.activeConnectionWithDatabase) {
            try {
                newItem = ProductionApplication.database.getOneItemFromDatabase(ProductionApplication.connection.get(), id);
                if (newItem.isPresent()) {
                    System.out.println("<SUCCESS> Successfully received Item (id: " + id + ")");
                    ProductionApplication.logger.error("<SUCCESS> Successfully received Item (id: " + id + ")");
                } else {
                    System.out.println("<ERROR> Failed getting Item (id: " + id + "), Couldn't find Item.");
                    ProductionApplication.logger.error("<ERROR> Failed getting Item (id: " + id + "), Couldn't find Item.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("<ERROR> Failed getting Item (id: " + id + "), No Connection to the Database.");
            ProductionApplication.logger.error("<ERROR> Failed getting Item (id: " + id + "), No Connection to the Database.");
        }
    }

    public Long getId() {
        return id;
    }
    public Optional<Item> getNewItem() {
        return newItem;
    }
}
