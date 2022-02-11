package tfs.business.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.model.customer.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerEndPointTest {
    private CustomerEndPoint endPoint;

    @BeforeEach
    void setUp() {
        endPoint = new CustomerEndPoint();
    }

    @Test
    void addGetUpdateAndDeleteCustomer() {
        Customer c = new Customer();
        c.setName("Marco");
        c.setSurname("Rossi");

        // add
        boolean r = endPoint.addCustomer(c);
        assertTrue(r);

        // get list
        List<Customer> l = endPoint.getCustomerList();
        assertFalse(l.isEmpty());

        // get (max id in the list)
        Customer c1 = endPoint.getCustomer(selectMaxId(l));
        assertNotNull(c1);

        // update
        c1.setName("Luca");
        r = endPoint.updateCustomer(c1);
        assertTrue(r);

        // delete
        c1 = endPoint.deleteCustomer(c1.getId());
        assertNotNull(c1);
    }

    private String selectMaxId(List<Customer> l) {
        int max = -1;
        for (Customer c : l) {
            int tmp = Integer.parseInt(c.getId());
            if (tmp > max)
                max = tmp;
        }
        return String.valueOf(max);
    }

}