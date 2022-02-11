package tfs.business.endpoint;

import tfs.business.dao.daofactory.CompanyDataDaoFactory;
import tfs.business.model.companydata.CompanyData;

public class CompanyDataEndPoint {
    public CompanyData getCompanyData() {
        return CompanyDataDaoFactory.getDao().getCompanyData();
    }

    public boolean updateCompanyData(CompanyData cd) {
        return CompanyDataDaoFactory.getDao().updateCompanyData(cd);
    }
}
