package tfs.business.dao.daointerface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.dao.daofactory.EstimateDaoFactory;
import tfs.business.dao.daointerface.EstimateDao;
import tfs.business.data.RDBConnection;
import tfs.business.model.estimate.Estimate;
import tfs.business.model.estimate.Item;
import tfs.business.model.estimate.ItemGroup;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateDaoTest {
    EstimateDao dao;

    @BeforeEach
    void setUp() {
        dao = EstimateDaoFactory.getDao();
    }

    @Test
    void addEstimate() {
        Estimate e = createEstimateTest();
        boolean r = dao.addEstimate(e);
        assertTrue(r);
    }

    @Test
    void getEstimateList() {
        List<Estimate> l = dao.getEstimateList();
        assertFalse(l.isEmpty());
    }

    @Test
    void getEstimate() {
        Estimate e = dao.getEstimate("1");
        assertNotNull(e);
    }

    Estimate createEstimateTest() {
        Estimate estimate = new Estimate();
        estimate.setCustomer("1");
        estimate.setExpirationDate(LocalDateTime.now());

        Item i1 = new Item();
        i1.setDescription("i1");
        Item i2 = new Item();
        i2.setDescription("i2");
        Item i3 = new Item();
        i3.setDescription("i3");
        Item i4 = new Item();
        i4.setDescription("i4");

        ItemGroup ig1 = new ItemGroup();
        ig1.addItem(i1);
        ig1.addItem(i2);

        ItemGroup ig2 = new ItemGroup();
        ig2.addItem(i3);
        ig2.addItem(i4);

        estimate.addItemGroup(ig1);
        estimate.addItemGroup(ig2);
        return estimate;
    }
}