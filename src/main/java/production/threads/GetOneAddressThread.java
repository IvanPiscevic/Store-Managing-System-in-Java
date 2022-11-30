package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;
import production.model.Address;

import java.sql.SQLException;
import java.util.Optional;

public class GetOneAddressThread implements Runnable {

    Long id;
    Optional<Address> newAddress;

    public GetOneAddressThread(Long id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (ProductionApplication.connection.isPresent() && Database.activeConnectionWithDatabase) {
            try {
                newAddress = ProductionApplication.database.getOneAddressFromDatabase(ProductionApplication.connection.get(), id);
                if (newAddress.isPresent()) {
                    System.out.println("<SUCCESS> Successfully received Address (id: " + id + ")");
                    ProductionApplication.logger.error("<SUCCESS> Successfully received Address (id: " + id + ")");
                } else {
                    System.out.println("<ERROR> Failed getting Address (id: " + id + "), Couldn't find Address.");
                    ProductionApplication.logger.error("<ERROR> Failed getting Address (id: " + id + "), Couldn't find Address.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("<ERROR> Failed getting Address (id: " + id + "), No Connection to the Database.");
            ProductionApplication.logger.error("<ERROR> Failed getting Address (id: " + id + "), No Connection to the Database.");
        }
    }

    public Long getId() {
        return id;
    }

    public Optional<Address> getNewAddress() {
        return newAddress;
    }
}
