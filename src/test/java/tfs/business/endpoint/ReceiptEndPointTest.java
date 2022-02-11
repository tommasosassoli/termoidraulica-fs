package tfs.business.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.estimate.Estimate;
import tfs.business.model.receipt.Receipt;
import tfs.business.model.receipt.Riba;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptEndPointTest {
    private ReceiptEndPoint endPoint;

    @BeforeEach
    void setUp() {
        endPoint = new ReceiptEndPoint();
    }

    @Test
    void addGetUpdateAndDeleteReceipt() {
        Receipt receipt = createReceipt();

        // add
        boolean r = endPoint.addReceipt(receipt);
        assertTrue(r);

        // get list
        List<Receipt> l = endPoint.getReceiptList();
        assertFalse(l.isEmpty());

        // get (max id in the list)
        Receipt r1 = endPoint.getReceipt(selectMaxId(l));
        assertNotNull(r1);

        // update
        r1.addRiba(new Riba());
        r = endPoint.updateReceipt(r1);
        assertTrue(r);

        // delete
        r1 = endPoint.deleteReceipt(r1.getId());
        assertNotNull(r1);
    }

    private String selectMaxId(List<Receipt> l) {
        int max = -1;
        for (Receipt c : l) {
            int tmp = Integer.parseInt(c.getId());
            if (tmp > max)
                max = tmp;
        }
        return String.valueOf(max);
    }

    private Receipt createReceipt() {
        Receipt rr = new Receipt("ext2", "termomarket", LocalDateTime.now());
        Riba r1 = new Riba();
        r1.setAmount(100);
        Riba r2 = new Riba();
        r2.setAmount(60);

        rr.addRiba(r1);
        rr.addRiba(r2);
        return rr;
    }
}