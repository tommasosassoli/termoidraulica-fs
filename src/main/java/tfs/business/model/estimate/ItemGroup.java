package tfs.business.model.estimate;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
    private String description;
    private List<Item> items = new ArrayList<>();

    public ItemGroup() {

    }

    public String getDescription() {
        return (description != null) ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item e) {
        items.add(e);
    }

    public void addItem(List<Item> l) {
        items.addAll(l);
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
