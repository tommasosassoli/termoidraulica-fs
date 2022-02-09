package tfs.business.dao.daoimplementation;

import tfs.business.data.PropertiesFileHandler;
import tfs.business.model.companydata.CompanyData;
import tfs.business.dao.daointerface.CompanyDataDao;
import tfs.business.resolvers.FileResolver;
import tfs.service.LogService;

import java.io.IOException;
import java.util.Properties;

public class PropertiesCompanyDataDao implements CompanyDataDao {
    @Override
    public CompanyData getCompanyData() {
        PropertiesFileHandler file = new PropertiesFileHandler(FileResolver.COMPANY_DATA);
        try {
            Properties p = file.loadProperties();
            return new CompanyData(p);
        } catch (IOException e) {
            LogService.error(PropertiesCompanyDataDao.class, "Cannot load Company Data file", true, e);
        }
        return null;
    }

    @Override
    public boolean updateCompanyData(CompanyData c) {
        PropertiesFileHandler file = new PropertiesFileHandler(FileResolver.COMPANY_DATA);
        try {
            file.saveProperties(c.exportInProperties());
            return true;
        } catch (IOException e) {
            LogService.error(PropertiesCompanyDataDao.class, "Cannot save Company Data file", true, e);
        }
        return false;
    }
}
