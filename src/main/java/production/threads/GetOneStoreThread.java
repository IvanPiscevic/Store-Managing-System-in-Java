package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Store;

import java.sql.SQLException;
import java.util.Optional;

public class GetOneStoreThread implements Runnable {

    Long id;
    Optional<Store> newStore;

    public GetOneStoreThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (ProductionApplication.connection.isPresent() && Database.activeConnectionWithDatabase) {
            try {
                newStore = ProductionApplication.database.getOneStoreFromDatabase(ProductionApplication.connection.get(), id);
                if (newStore.isPresent()) {
                    System.out.println("<SUCCESS> Successfully received Store (id: " + id + ")");
                    ProductionApplication.logger.error("<SUCCESS> Successfully received Store (id: " + id + ")");
                } else {
                    System.out.println("<ERROR> Failed getting Store (id: " + id + "), Couldn't find Store.");
                    ProductionApplication.logger.error("<ERROR> Failed getting Store (id: " + id + "), Couldn't find Store.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("<ERROR> Failed getting Store (id: " + id + "), No Connection to the Database.");
            ProductionApplication.logger.error("<ERROR> Failed getting Store (id: " + id + "), No Connection to the Database.");
        }
    }

    public Long getId() {
        return id;
    }

    public Optional<Store> getNewStore() {
        return newStore;
    }
}
