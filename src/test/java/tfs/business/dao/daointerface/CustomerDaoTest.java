package tfs.business.dao.daointerface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.dao.daofactory.CustomerDaoFactory;
import tfs.business.model.customer.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {
    CustomerDao dao;

    @BeforeEach
    void beforeAll() {
        dao = CustomerDaoFactory.getDao();
    }

    @Test
    void addCustomer() {
        // insert
        Customer c = new Customer(null, "Pie", "Sas", "via pinco pallino",
                "Firenze", "FI", null, null, null);

        boolean r = dao.addCustomer(c);
        assertTrue(r);

        // insert (must fail, look province field)
        c = new Customer(null, "Pie", "Sas", "via pinco pallino",
                "Firenze", "FIIIII", null, null, null);

        r = dao.addCustomer(c);
        assertFalse(r);
    }

    @Test
    void getCustomerList() {
        List<Customer> l = dao.getCustomerList();
        assertFalse(l.isEmpty());
    }

    @Test
    void getCustomerAndUpdate() {
        Customer c = dao.getCustomer("1");
        assertNotNull(c);

        boolean r = dao.updateCustomer(c);
        assertTrue(r);
    }
}