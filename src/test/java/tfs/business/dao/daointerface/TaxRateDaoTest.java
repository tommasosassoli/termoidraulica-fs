package tfs.business.dao.daointerface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.dao.daointerface.TaxRateDao;
import tfs.business.model.tax.TaxRate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxRateDaoTest {
    TaxRateDao dao;

    @BeforeEach
    void setUp() {
        dao = TaxRateDaoFactory.getDao();
    }

    @Test
    void getTaxRatesList() {
        List<TaxRate> l = dao.getTaxRatesList();
        assertFalse(l.isEmpty());
    }

    @Test
    void getTaxRate() {
        TaxRate t = dao.getTaxRate(22);
        assertNotNull(t);
    }
}