package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Factory;

import java.sql.SQLException;
import java.util.Optional;

public class GetOneFactoryThread implements Runnable {

    Long id;
    Optional<Factory> newFactory;

    public GetOneFactoryThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (ProductionApplication.connection.isPresent() && Database.activeConnectionWithDatabase) {
            try {
                newFactory = ProductionApplication.database.getOneFactoryFromDatabase(ProductionApplication.connection.get(), id);
                if (newFactory.isPresent()) {
                    System.out.println("<SUCCESS> Successfully received Factory (id: " + id + ")");
                    ProductionApplication.logger.error("<SUCCESS> Successfully received Factory (id: " + id + ")");
                } else {
                    System.out.println("<ERROR> Failed getting Factory (id: " + id + "), Couldn't find Factory.");
                    ProductionApplication.logger.error("<ERROR> Failed getting Factory (id: " + id + "), Couldn't find Factory.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("<ERROR> Failed getting Factory (id: " + id + "), No Connection to the Database.");
            ProductionApplication.logger.error("<ERROR> Failed getting Factory (id: " + id + "), No Connection to the Database.");
        }
    }

    public Long getId() {
        return id;
    }

    public Optional<Factory> getNewFactory() {
        return newFactory;
    }
}
