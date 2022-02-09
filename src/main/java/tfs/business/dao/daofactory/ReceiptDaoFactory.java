package tfs.business.dao.daofactory;

import tfs.business.dao.daoimplementation.RDBReceiptDao;
import tfs.business.dao.daointerface.ReceiptDao;

public class ReceiptDaoFactory {
    public static ReceiptDao getDao() {
        return new RDBReceiptDao();
    }
}
