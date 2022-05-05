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

    public Item removeItem(int index) {
        return items.remove(index);
    }

    public double getItemGroupSubtotal() {
        double subtotal = 0;
        for (Item i : items)
            subtotal += i.getItemSubtotal();
        return subtotal;
    }

    /**
     * @return the sum over the item that have a positive subtotal
     * */
    public double getItemGroupCashValue() {
        double value = 0;
        for (Item i : items) {
            double sub = i.getItemSubtotal();
            if (sub > 0)
                value += sub;
        }
        return value;
    }

    /**
     * @return the sum over the item that have a negative subtotal in abs
     * */
    public double getItemGroupDiscountPrice() {
        double discount = 0;
        for (Item i : items) {
            double sub = i.getItemSubtotal();
            if (sub < 0)
                discount -= sub;
        }
        return discount;
    }

    public boolean isEmpty() {
        return items.isEmpty() && getDescription().isEmpty();
    }

    public String toString() {
        return getDescription();
    }
}
