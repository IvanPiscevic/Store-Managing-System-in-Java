package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Address;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewAddressThread implements Runnable {

    private Address address;
    public volatile boolean successFlag = false;

    public InsertNewAddressThread(Address newAddress) {
        this.address = newAddress;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewAddressToDatabase(address);
            System.out.println("<SUCCESS> Successfully added new Address into database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Address into database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<ERROR> Failed adding new Address to database.");
            ProductionApplication.logger.info(("<ERROR> Failed adding new Address to database."));
            ex.printStackTrace();
        }
    }
}
