package production.sort;

import production.model.Item;
import java.util.Comparator;

public class ProductionSorter implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        if (i1.getSellingPrice().compareTo(i2.getSellingPrice()) < 0) {
            return 1;
        } else if (i1.getSellingPrice().compareTo(i2.getSellingPrice()) > 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
