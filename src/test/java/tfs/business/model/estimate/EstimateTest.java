package tfs.business.model.estimate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.model.customer.Customer;
import tfs.business.model.tax.TaxRate;
import tfs.business.model.tax.Taxable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateTest {
    private Estimate estimate;

    @BeforeEach
    void createEstimate() {
        estimate = new Estimate("000", new Customer(), LocalDateTime.now());

        Item i1 = new Item();//100 (tax=10)
        i1.setQt(1);
        i1.setPrice(100);
        i1.setTaxRate(new TaxRate(10, ""));
        Item i2 = new Item();//50 (tax=11)
        i2.setQt(2);
        i2.setPrice(25);
        i2.setTaxRate(new TaxRate(22, ""));
        Item i3 = new Item();//0
        i3.setQt(1);
        i3.setPrice(0);

        ItemGroup group1 = new ItemGroup();
        group1.addItem(i1);
        group1.addItem(i2);
        group1.addItem(i3);

        estimate.addItemGroup(group1);

        Item i4 = new Item();//20 (tax=2)
        i4.setQt(10);
        i4.setPrice(2);
        i4.setTaxRate(new TaxRate(10, ""));

        Item i5 = new Item();//20 (tax=2)
        i5.setQt(1);
        i5.setPrice(-10);

        ItemGroup group2 = new ItemGroup();
        group2.addItem(i4);
        group2.addItem(i5);

        estimate.addItemGroup(group2);
        //total 160
    }

    @Test
    void calculateEstimateSubtotal() {
        assertEquals(160, estimate.getEstimateSubtotal());
    }

    @Test
    void calculateTaxableTotal() {
        estimate.setDeposit(-50);
        assertEquals(0, estimate.getDeposit());
        estimate.setDeposit(50);

        assertEquals(110, estimate.getTaxableTotal());

        estimate.setDeposit(0);
        assertEquals(160, estimate.getTaxableTotal());
    }

    @Test
    void calculateTax() {
        assertEquals(23, estimate.getCalculatedTax());
    }

    @Test
    void calculateEstimateTotal() {
        estimate.setDeposit(50);
        assertEquals(133, estimate.getEstimateTotal());
    }

    @Test
    void getTaxableList() {
        //expecting result
        List<Taxable> expected = new ArrayList<>();
        expected.add(new Taxable(50, TaxRateDaoFactory.getDao().getTaxRate(22)));
        expected.add(new Taxable(120, TaxRateDaoFactory.getDao().getTaxRate(10)));
        expected.add(new Taxable(0, TaxRateDaoFactory.getDao().getTaxRate(4)));
        expected.add(new Taxable(0, TaxRateDaoFactory.getDao().getTaxRate(0)));

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

    @Test
    void cashValueAndDiscountPriceTest() {
        assertEquals(170, estimate.getCashValue());
        assertEquals(10, estimate.getDiscountPrice());
    }

}