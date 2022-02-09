package tfs.business.dao.daofactory;

import tfs.business.dao.daoimplementation.RDBTaxRateDao;
import tfs.business.dao.daointerface.TaxRateDao;

public class TaxRateDaoFactory {
    public static TaxRateDao getDao() {
        return new RDBTaxRateDao();
    }
}
