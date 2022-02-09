package tfs.business.dao.daointerface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.dao.daofactory.ReceiptDaoFactory;
import tfs.business.model.receipt.Receipt;
import tfs.business.model.receipt.Riba;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptDaoTest {
    private ReceiptDao dao;

    @BeforeEach
    void setUp() {
        dao = ReceiptDaoFactory.getDao();
    }

    @Test
    void addReceipt() {
        Receipt r = createReceipt();
        boolean res = dao.addReceipt(r);
        assertTrue(res);
    }

    @Test
    void getReceiptList() {
        List<Receipt> l = dao.getReceiptList();
        assertNotNull(l);
    }

    @Test
    void getReceipt() {
        Receipt r = dao.getReceipt("1");
        assertNotNull(r);
    }

    Receipt createReceipt() {
        Receipt rr = new Receipt("ext1", "palagini", LocalDateTime.now());
        Riba r1 = new Riba();
        r1.setAmount(50);
        Riba r2 = new Riba();
        r2.setAmount(25);

        rr.addRiba(r1);
        rr.addRiba(r2);
        return rr;
    }
}