package tfs.business.model.estimate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemGroupTest {
    private ItemGroup group = new ItemGroup();

    @Test
    void addItem() {
        Item i = new Item();
        group.addItem(i);
        assertFalse(group.getItems().isEmpty());
    }

    @Test
    void getItemGroupSubtotal() {
        Item i1 = new Item();
        i1.setQt(1);
        i1.setPrice(100);
        Item i2 = new Item();
        i2.setQt(2);
        i2.setPrice(25);
        Item i3 = new Item();
        i3.setQt(1);
        i3.setPrice(0);

        group.addItem(i1);
        group.addItem(i2);
        group.addItem(i3);
        // total 150

        assertEquals(150, group.getItemGroupSubtotal());
    }
}