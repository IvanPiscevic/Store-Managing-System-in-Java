package production.genericsi;

import production.model.Item;
import production.model.Store;
import production.model.Technical;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TechnicalStore<T extends Technical> extends Store {

    List<T> technicalItemsList = new ArrayList<>();

    public TechnicalStore(Long id, String name, String webAddress, Set<Item> items, List<T> technicalItemsList) {
        super(id, name, webAddress, items);
        this.technicalItemsList = technicalItemsList;
    }

    public void addTechnical(T item) {
        technicalItemsList.add(item);
    }

    public List<T> getTechnicalItemsList() {
        return technicalItemsList;
    }
    public void setTechnicalItemsList(List<T> technicalItemsList) {
        this.technicalItemsList = technicalItemsList;
    }
}
