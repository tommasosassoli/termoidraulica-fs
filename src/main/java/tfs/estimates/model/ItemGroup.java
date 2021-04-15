package tfs.estimates.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
    @JsonProperty("subtotal_description")
    private String description;

    @JacksonXmlElementWrapper(localName = "items_list")
    @JsonProperty("item")
    private List<Item> items = new ArrayList<>();

    public ItemGroup() {

    }

    @JsonIgnore
    public String getDescription() {
        return (description != null) ? description : "";
    }

    @JsonProperty("subtotal_description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<Item> getItems() {
        return items;
    }

    @JsonIgnore
    public void addItem(Item e) {
        items.add(e);
    }

    @JsonProperty("item")
    public void addItem(List<Item> l) {
        items.addAll(l);
    }

    @JsonIgnore
    public boolean removeItem(Item e) {
        return items.remove(e);
    }

    @JsonIgnore
    public double getItemGroupSubtotal() {
        double subtotal = 0;
        for (Item i : items)
            subtotal += i.getItemSubtotal();
        return subtotal;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return items.isEmpty() && getDescription().isEmpty();
    }
}
