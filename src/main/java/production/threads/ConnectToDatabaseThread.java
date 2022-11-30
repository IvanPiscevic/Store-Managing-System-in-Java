package production.threads;

import com.example.piscevic.ProductionApplication;
import production.database.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ConnectToDatabaseThread implements Runnable{

    public static boolean successFlag = false;
    int failCounter = 0;

    @Override
    public void run() {
        try {
            while(Database.activeConnectionWithDatabase) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    //System.out.println("[DEBUG SLEEPING IN CONNECT TO DATABASE THREAD");
                    failCounter++;
                    System.out.println("Failed connection! Attempt: " + failCounter);
                    if (failCounter > 10) {
                        throw new InterruptedException();
                    }
                } catch (InterruptedException ex) {
                    System.out.println("<ERROR> Thread was interrupted while trying to connect to the database.");
                    ProductionApplication.logger.error("<ERROR> Thread was interrupted while trying to connect to the database.", ex);
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
            ProductionApplication.connection = ProductionApplication.database.connectToDatabase();
            successFlag = true;
            System.out.println("<SUCCESS> Connected to database.");
            ProductionApplication.logger.info("<SUCCESS> Connected to database.");
        } catch (IOException | SQLException ex) {
            System.out.println("<ERROR> Failed connection with database. EXCEPTION");
            ProductionApplication.logger.error("<ERROR> Failed connection with database.");
            ex.printStackTrace();
        }
    }

}
