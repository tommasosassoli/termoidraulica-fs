package tfs.modules.estimates.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.modules.common.management.TaxRateManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateTest {
    private Estimate estimate;

    @BeforeEach
    void createEstimate() {
        estimate = new Estimate("000", new Client(), LocalDateTime.now());

        Item i1 = new Item();//100
        i1.setQt(1);
        i1.setPrice(100);
        Item i2 = new Item();//50
        i2.setQt(2);
        i2.setPrice(25);
        Item i3 = new Item();//0
        i3.setQt(1);
        i3.setPrice(0);

        ItemGroup group1 = new ItemGroup();
        group1.addItem(i1);
        group1.addItem(i2);
        group1.addItem(i3);

        estimate.addItemGroup(group1);

        Item i4 = new Item();//20
        i4.setQt(10);
        i4.setPrice(2);

        ItemGroup group2 = new ItemGroup();
        group2.addItem(i4);

        estimate.addItemGroup(group2);
        //total 170
    }

    @Test
    void calculateEstimateSubtotal() {
        assertEquals(170, estimate.getEstimateSubtotal());
    }

    @Test
    void calculateTaxableTotal() {
        estimate.setDeposit(-50);
        assertEquals(0, estimate.getDeposit());
        estimate.setDeposit(50);

        assertEquals(120, estimate.getTaxableTotal());

        estimate.setDeposit(0);
        assertEquals(170, estimate.getTaxableTotal());
    }

    @Test
    void calculateTax() {
        assertEquals(37.4, estimate.getCalculatedTax());
    }

    @Test
    void calculateEstimateTotal() {
        estimate.setDeposit(50);
        assertEquals(157.4, estimate.getEstimateTotal());
    }

    @Test
    void getTaxableList() {
        //first item of first group set to 10% tax rate
        estimate.getItemGroups().get(0).getItems().get(0).setTaxRate(TaxRateManagement.instance().getFromValue(10));

        //first item of second group set to 10% tax rate
        estimate.getItemGroups().get(1).getItems().get(0).setTaxRate(TaxRateManagement.instance().getFromValue(10));

        //expecting result
        List<Taxable> expected = new ArrayList<>();
        expected.add(new Taxable(50, TaxRateManagement.instance().getFromValue(22)));
        expected.add(new Taxable(120, TaxRateManagement.instance().getFromValue(10)));
        expected.add(new Taxable(0, TaxRateManagement.instance().getFromValue(4)));
        expected.add(new Taxable(0, TaxRateManagement.instance().getFromValue(0)));

        List<Taxable> computed = estimate.getTaxableList();

        int ok = 0;
        for (Taxable c : computed) {
            for (Taxable e : expected) {
                if (c.getTaxable() == e.getTaxable() && c.getTaxRate().getTaxRateValue() == e.getTaxRate().getTaxRateValue())
                    ok++;
            }
        }
        assertEquals(4, ok);
    }

}