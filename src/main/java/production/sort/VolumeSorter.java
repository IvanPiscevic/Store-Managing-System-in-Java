package production.sort;

import production.model.Item;

import java.math.BigDecimal;
import java.util.Comparator;

public class VolumeSorter implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        BigDecimal firstValue, secondValue;
        firstValue = i1.getWidth().multiply(i1.getLength().multiply(i1.getHeight()));
        secondValue = i2.getWidth().multiply(i2.getLength().multiply(i2.getHeight()));
        if (firstValue.compareTo(secondValue) > 0) {
            return 1;
        } else if (firstValue.compareTo(secondValue) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
