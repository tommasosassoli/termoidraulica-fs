package tfs.business.dao.daointerface;

import tfs.business.model.customer.Customer;

import java.util.List;

public interface CustomerDao extends DaoInterface{
    boolean addCustomer(Customer c);
    List<Customer> getCustomerList();
    Customer getCustomer(String id);
    boolean updateCustomer(Customer c);
    Customer deleteCustomer(String id);
}
