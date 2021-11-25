package tfs.modules.estimates.management.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.storage.DataManagement;
import tfs.modules.estimates.model.Client;
import tfs.modules.common.model.CompanyData;
import tfs.modules.estimates.model.Estimate;
import tfs.modules.common.model.TaxRate;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataManagementTest {
    private static DataManagement instance;

    @BeforeAll
    static void instance() {
        instance = DataManagement.instance();
    }

    @Test
    void getClients() throws IOException {
        List<Client> cl = instance.getClients();
        assertNotNull(cl);
        assertFalse(cl.isEmpty());
    }

    @Test
    void getEstimates() throws IOException {
        List<Estimate> es = instance.getEstimates();
        assertNotNull(es);
        assertFalse(es.isEmpty());
    }

    @Test
    void getCompanyData() throws IOException {
        CompanyData cd = instance.getCompanyData();
        assertNotNull(cd);
    }

    @Test
    void getTaxRates() throws IOException {
        List<TaxRate> cl = instance.getTaxRates();
        assertNotNull(cl);
        assertFalse(cl.isEmpty());
    }

    @Test
    void getNextIDs() {
        if (instance.getNextClientID().isEmpty())
            fail();
        if (instance.getNextEstimateID().isEmpty())
            fail();
    }
}