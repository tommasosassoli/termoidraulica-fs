package tfs.business.model.estimate;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
    private String description;
    private final List<Item> items = new ArrayList<>();

    public ItemGroup() {
    }

    public ItemGroup(ItemGroup g) {
        this.description = g.description;
        this.items.addAll(g.getItems());
    }

    public String getDescription() {
        return (description != null) ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        ArrayList<Item> l = new ArrayList<>();
        for (Item i : items)
            l.add(new Item(i));
        return l;
    }

    public void addItem(Item e) {
        items.add(new Item(e));
    }

    public void overrideItem(List<Item> list) {
        items.clear();
        for (Item i : list)
            items.add(new Item(i));
    }

    public boolean removeItem(Item e) {
        return items.remove(e);
    }

    public double getItemGroupSubtotal() {
        double subtotal = 0;
        for (Item i : items)
            subtotal += i.getItemSubtotal();
        return subtotal;
    }

    public boolean isEmpty() {
        return items.isEmpty() && getDescription().isEmpty();
    }

    public String toString() {
        return getDescription();
    }
}
