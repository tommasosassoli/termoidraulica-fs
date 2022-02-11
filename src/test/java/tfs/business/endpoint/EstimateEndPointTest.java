package tfs.business.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.business.model.estimate.Item;
import tfs.business.model.estimate.ItemGroup;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateEndPointTest {
    EstimateEndPoint endPoint;

    @BeforeEach
    void setUp() {
        endPoint = new EstimateEndPoint();
    }

    @Test
    void addGetUpdateAndDeleteEstimate() {
        Estimate estimate = createEstimateTest();

        // add
        boolean r = endPoint.addEstimate(estimate);
        assertTrue(r);

        // get list
        List<Estimate> l = endPoint.getEstimateList();
        assertFalse(l.isEmpty());

        // get (max id in the list)
        Estimate e1 = endPoint.getEstimate(selectMaxId(l));
        assertNotNull(e1);

        // update
        e1.setDeposit(500);
        r = endPoint.updateEstimate(e1);
        assertTrue(r);

        // delete
        e1 = endPoint.deleteEstimate(e1.getId());
        assertNotNull(e1);
    }

    private String selectMaxId(List<Estimate> l) {
        int max = -1;
        for (Estimate c : l) {
            int tmp = Integer.parseInt(c.getId());
            if (tmp > max)
                max = tmp;
        }
        return String.valueOf(max);
    }

    private Estimate createEstimateTest() {
        Estimate estimate = new Estimate();
        estimate.setCustomer("1");
        estimate.setExpirationDate(LocalDateTime.now());

        Item i1 = new Item();
        i1.setDescription("i1");
        Item i2 = new Item();
        i2.setDescription("i2");

        ItemGroup ig1 = new ItemGroup();
        ig1.addItem(i1);
        ig1.addItem(i2);

        estimate.addItemGroup(ig1);
        return estimate;
    }
}