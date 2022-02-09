package tfs.business.endpoint;

import tfs.business.dao.daofactory.CustomerDaoFactory;
import tfs.business.model.customer.Customer;

import java.util.List;

public class CustomerEndPoint {

    public boolean addCustomer(Customer c) {
        return CustomerDaoFactory.getDao().addCustomer(c);
    }

    public List<Customer> getCustomerList() {
        return CustomerDaoFactory.getDao().getCustomerList();
    }

    public Customer getCustomer(String id) {
        return CustomerDaoFactory.getDao().getCustomer(id);
    }

    public boolean updateCustomer(Customer c) {
        return CustomerDaoFactory.getDao().updateCustomer(c);
    }

    public Customer deleteCustomer(String id) {
        return CustomerDaoFactory.getDao().deleteCustomer(id);
    }
}
