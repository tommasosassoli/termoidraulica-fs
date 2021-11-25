package tfs.modules.estimates.management;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.modules.common.management.TaxRateManagement;

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