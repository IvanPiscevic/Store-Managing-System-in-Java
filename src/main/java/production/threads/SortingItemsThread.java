package production.threads;

import com.example.piscevic.ProductionApplication;
import production.sort.ProductionSorter;

public class SortingItemsThread implements Runnable {

    @Override
    public void run() {
        ProductionApplication.items.sort(new ProductionSorter());
    }
}
