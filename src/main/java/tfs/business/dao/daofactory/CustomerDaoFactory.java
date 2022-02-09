package tfs.business.dao.daofactory;

import tfs.business.dao.daoimplementation.RDBCustomerDao;
import tfs.business.dao.daointerface.CustomerDao;

public class CustomerDaoFactory {
    public static CustomerDao getDao() {
        return new RDBCustomerDao();
    }
}
