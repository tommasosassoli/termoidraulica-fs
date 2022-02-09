package tfs.business.dao.daointerface;

import tfs.business.model.companydata.CompanyData;

public interface CompanyDataDao extends DaoInterface {
    CompanyData getCompanyData();
    boolean updateCompanyData(CompanyData c);
}
