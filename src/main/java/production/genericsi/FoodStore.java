package production.genericsi;

import production.model.Edible;
import production.model.Item;
import production.model.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FoodStore<T extends Edible> extends Store {

    List<T> edibleItemsList = new ArrayList<>();

    public FoodStore(Long id, String name, String webAddress, Set<Item> items, List<T> edibleItemsList) {
        super(id, name, webAddress, items);
        this.edibleItemsList = edibleItemsList;
    }

    public void addEdible(T item) {
        edibleItemsList.add(item);
    }

    public List<T> getEdibleItemsList() {
        return edibleItemsList;
    }
    public void setEdibleItemsList(List<T> edibleItemsList) {
        this.edibleItemsList = edibleItemsList;
    }
}
