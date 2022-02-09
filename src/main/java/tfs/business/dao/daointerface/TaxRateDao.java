package tfs.business.dao.daointerface;

import tfs.business.model.tax.TaxRate;

import java.util.List;

public interface TaxRateDao extends DaoInterface {
    List<TaxRate> getTaxRatesList();
    TaxRate getTaxRate(int taxRate);
}
