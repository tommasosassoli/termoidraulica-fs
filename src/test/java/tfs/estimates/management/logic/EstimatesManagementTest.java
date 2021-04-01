package tfs.estimates.management.logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.estimates.model.Client;
import tfs.estimates.model.Estimate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EstimatesManagementTest {
    private static EstimatesManagement instance = null;
    private static String id;

    @BeforeAll
    static void init() {
        instance = EstimatesManagement.instance();
        assertNotNull(instance);

        //add estimate
        id = instance.addEstimate(new Client(), LocalDateTime.now());
        assertFalse(id.isEmpty());
    }

    @Test
    void getEstimates() {
        assertFalse(instance.getEstimates().isEmpty());
    }

    @Test
    void getEstimate() {
        Estimate e = instance.getEstimate(id);
        assertNotNull(e);
    }

    @Test
    void updateEstimate() {
        Estimate newEstimate = new Estimate();
        newEstimate.setDeposit(100);

        instance.updateEstimate(id, newEstimate);

        Estimate updateEstimate = instance.getEstimate(id);
        assertEquals(100, updateEstimate.getDeposit());
    }

    @Test
    void deleteEstimate() {
        Estimate d = instance.deleteEstimate(id);
        assertNotNull(d);
    }
}