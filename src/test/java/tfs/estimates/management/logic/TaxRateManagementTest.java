package tfs.estimates.management.logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaxRateManagementTest {
    private static TaxRateManagement instance = null;

    @BeforeAll
    static void init() {
        instance = TaxRateManagement.instance();
        assertNotNull(instance);
    }

    @Test
    void getTaxRates() {
        assertFalse(instance.getTaxRates().isEmpty());
    }
}