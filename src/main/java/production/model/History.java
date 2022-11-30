package production.model;

public class History {

    public Item[] insertedItems;

    public History(int numOfItems) {
        this.insertedItems = new Item[numOfItems];
    }

    public void insertItem(Item item, int itemIdx) {
        insertedItems[itemIdx] = item;
    }
}
