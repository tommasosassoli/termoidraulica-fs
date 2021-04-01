package tfs.estimates.management.logic;

import java.io.IOException;

import tfs.estimates.management.storage.DataManagement;
import tfs.estimates.model.CompanyData;
import tfs.estimates.service.LogService;

public class CompanyDataManagement {
	private static CompanyDataManagement instance;
	private CompanyData companyData;

	private CompanyDataManagement() {
		this.loadFile();
	}

	public void loadFile() {
		LogService.trace(CompanyDataManagement.class, "Loading company data");

		try {
			companyData = DataManagement.instance().getCompanyData();
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load dati_azienda file", true, e);
			companyData = new CompanyData();
		}
	}

	public void commitDatiAzienda() {
		LogService.trace(CompanyDataManagement.class, "Committing company data");

		try {
			DataManagement.instance().commitCompanyData(companyData);
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot save dati_azienda file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(CompanyDataManagement.class, "Refreshing company data");

		commitDatiAzienda();
		loadFile();
	}

	public CompanyData getCompanyData() {
		return companyData;
	}

	public static CompanyDataManagement instance() {
		if (instance == null)
			instance = new CompanyDataManagement();
		return instance;
	}

}
