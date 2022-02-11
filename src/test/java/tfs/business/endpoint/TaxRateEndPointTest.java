package tfs.business.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.tax.TaxRate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxRateEndPointTest {
    private TaxRateEndPoint endPoint;

    @BeforeEach
    void setUp() {
        endPoint = new TaxRateEndPoint();
    }

    @Test
    void getTaxRates() {
        List<TaxRate> l = endPoint.getTaxRatesList();
        assertFalse(l.isEmpty());

        for (TaxRate t : l) {
            TaxRate tax = endPoint.getTaxRate((int) t.getTaxRateValue());
            assertNotNull(tax);
        }
    }
}