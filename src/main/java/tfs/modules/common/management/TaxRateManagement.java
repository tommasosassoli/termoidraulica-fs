package tfs.modules.common.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tfs.storage.DataManagement;
import tfs.modules.common.model.TaxRate;
import tfs.service.LogService;

public class TaxRateManagement {
	private static TaxRateManagement instance;
	private ArrayList<TaxRate> taxRates = new ArrayList<>();

	private TaxRateManagement() {
		LogService.trace(TaxRateManagement.class, "Loading aliquote iva data");

		try {
			taxRates = (ArrayList<TaxRate>) DataManagement.instance().getTaxRates();
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load the aliquote_iva file", true, e);
		}
	}
	
	public List<TaxRate> getTaxRates() {
		return taxRates;
	}
	
	public TaxRate getFromValue(double valueIva) {
		for(TaxRate a: taxRates) {
			if(a.getTaxRateValue() == valueIva)
				return a;
		}
		return null;
	}

	public static TaxRateManagement instance() {
		if(instance == null)
			instance = new TaxRateManagement();
		return instance;
	}
}
