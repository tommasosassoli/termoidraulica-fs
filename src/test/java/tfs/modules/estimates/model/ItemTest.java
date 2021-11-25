package tfs.modules.estimates.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.modules.common.management.TaxRateManagement;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item item;

    @BeforeEach
    void createItem() {
        item = new Item();
        assertNotNull(item);

        item.setTaxRate(TaxRateManagement.instance().getFromValue(0));

        item.setPrice(-50);
        assertEquals(0, item.getPrice());
        item.setPrice(50);

        item.setQt(-2);
        assertEquals(0, item.getQt());
        item.setQt(2);
    }

    @Test
    void getItemSubtotal() {
        assertEquals(100, item.getItemSubtotal());
    }

    @Test
    void getItemSubtotalDiscounted() {
        item.setDiscount(-10);
        assertEquals(item.getDiscount(), 0);
        item.setDiscount(10);

        assertEquals(90, item.getItemSubtotal());
    }
}