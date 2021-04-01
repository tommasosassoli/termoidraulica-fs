package tfs.estimates.management.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.estimates.model.Client;
import tfs.estimates.model.CompanyData;
import tfs.estimates.model.Estimate;
import tfs.estimates.model.TaxRate;
import tfs.estimates.util.FileNameResolver;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

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