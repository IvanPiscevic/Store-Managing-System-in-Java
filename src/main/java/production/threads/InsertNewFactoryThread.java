package production.threads;

import com.example.piscevic.ProductionApplication;
import production.model.Factory;

import java.io.IOException;
import java.sql.SQLException;

public class InsertNewFactoryThread implements Runnable {

    private Factory factory;
    public volatile boolean successFlag = false;

    public InsertNewFactoryThread(Factory newFactory) {
        this.factory = newFactory;
    }

    @Override
    public void run() {
        try {
            ProductionApplication.database.insertNewFactoryToDatabase(factory);
            System.out.println("<SUCCESS> Successfully added new Factory into database.");
            ProductionApplication.logger.info("<SUCCESS> Successfully added new Factory into database.");
            successFlag = true;
        } catch (SQLException | IOException ex) {
            System.out.println("<ERROR> Failed adding new Factory to database.");
            ProductionApplication.logger.info(("<ERROR> Failed adding new Factory to database."));
            ex.printStackTrace();
        }
    }

}
