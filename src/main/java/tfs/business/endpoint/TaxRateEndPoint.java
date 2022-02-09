package tfs.business.endpoint;

import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.model.tax.TaxRate;

import java.util.List;

public class TaxRateEndPoint {

    public List<TaxRate> getTaxRatesList() {
        return TaxRateDaoFactory.getDao().getTaxRatesList();
    }

    public TaxRate getTaxRate(int taxRate) {
        return TaxRateDaoFactory.getDao().getTaxRate(taxRate);
    }
}
