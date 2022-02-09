package tfs.business.dao.daofactory;

import tfs.business.dao.daoimplementation.PropertiesCompanyDataDao;
import tfs.business.dao.daointerface.CompanyDataDao;

public class CompanyDataDaoFactory {
    public static CompanyDataDao getDao() {
        return new PropertiesCompanyDataDao();
    }
}
