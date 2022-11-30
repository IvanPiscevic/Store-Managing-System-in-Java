package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.sql.SQLException;
import java.util.Optional;

public class DisconnectFromDatabaseThread implements Runnable {

    public boolean successFlag = false;

    @Override
    public void run() {
        while(Database.activeConnectionWithDatabase == false) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        try {
            if (!ProductionApplication.connection.get().isClosed()) {
                ProductionApplication.database.disconnectFromDatabase(ProductionApplication.connection.get());
                Database.activeConnectionWithDatabase = false;
                successFlag = true;
                ProductionApplication.connection = Optional.empty();
                System.out.println("<SUCCESS> Closed connection with database.");
                ProductionApplication.logger.info("<SUCCESS> Closed connection with database.");
            }
        } catch (SQLException ex) {
            System.out.println("<ERROR> Failed to disconnect from database.");
            ProductionApplication.logger.error("<ERROR> Failed to disconnect from database.", ex);
            ex.printStackTrace();
        }
    }
}
