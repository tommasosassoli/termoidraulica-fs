package tfs.modules.estimates.management;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.modules.common.management.CompanyDataManagement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompanyDataManagementTest {
    private static CompanyDataManagement instance = null;

    @BeforeAll
    static void init() {
        instance = CompanyDataManagement.instance();
        assertNotNull(instance);
    }

    @Test
    void getTaxRates() {
        assertNotNull(instance.getCompanyData());
    }
}