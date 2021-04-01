package tfs.estimates.management.storage;

import tfs.estimates.model.TaxRate;
import tfs.estimates.model.Client;
import tfs.estimates.model.CompanyData;
import tfs.estimates.model.Estimate;
import tfs.estimates.model.serialization.TaxRatesSerialization;
import tfs.estimates.model.serialization.ClientsSerialization;
import tfs.estimates.model.serialization.EstimatesSerialization;
import tfs.estimates.service.LogService;
import tfs.estimates.util.FileNameResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Mid layer between the management classes and the file storage class
 */
public class DataManagement {
	private static DataManagement instance;

	private int nextClientID = -1;
	private int nextEstimateID = -1;

	public static DataManagement instance() {
		if (instance == null)
			instance = new DataManagement();
		return instance;
	}

	public List<Client> getClients() throws IOException {
		LogService.trace(DataManagement.class, "Getting clients");

		ClientsSerialization cs = FileManagement.instance().loadXmlFile(FileNameResolver.CLIENTS,
				ClientsSerialization.class);

		LogService.debug(DataManagement.class, (cs.isEmpty()) ? "Clients list is empty" : "Clients list is not empty");

		return (!cs.isEmpty()) ? new ArrayList<>(cs.getCollection()) : null;
	}

	public void commitClients(ClientsSerialization clients) throws IOException {
		LogService.trace(DataManagement.class, "Putting clients");

		FileManagement.instance().saveXmlFile(FileNameResolver.CLIENTS, clients);
		saveNextId();
	}

	public List<Estimate> getEstimates() throws IOException {
		LogService.trace(DataManagement.class, "Getting Estimate");

		EstimatesSerialization fs = FileManagement.instance().loadXmlFile(FileNameResolver.ESTIMATES,
				EstimatesSerialization.class);

		LogService.debug(DataManagement.class, (fs.isEmpty()) ? "Estimate list is empty" : "Estimate list is not empty");

		return (!fs.isEmpty()) ? new ArrayList<>(fs.getCollection()) : null;
	}

	public void commitEstimates(EstimatesSerialization fatture) throws IOException {
		LogService.trace(DataManagement.class, "Putting Estimate");

		FileManagement.instance().saveXmlFile(FileNameResolver.ESTIMATES, fatture);
		saveNextId();
	}

	public CompanyData getCompanyData() throws IOException {
		LogService.trace(DataManagement.class, "Getting company data");

		Properties p = FileManagement.instance().loadProperties(FileNameResolver.COMPANY_DATA);

		LogService.debug(DataManagement.class, (p.isEmpty()) ? "Company data is empty" : "Company data is not empty");

		return new CompanyData(p);
	}

	public void commitCompanyData(CompanyData data) throws IOException {
		LogService.trace(DataManagement.class, "Putting company data");

		FileManagement.instance().saveProperties(FileNameResolver.COMPANY_DATA, data.exportInProperties());
	}

	public List<TaxRate> getTaxRates() throws IOException {
		LogService.trace(DataManagement.class, "Getting tax rates");

		TaxRatesSerialization as = FileManagement.instance().loadXmlFile(FileNameResolver.TAX_RATES,
				TaxRatesSerialization.class);

		LogService.debug(DataManagement.class, (as.isEmpty()) ? "Tax rate list is empty" : "Tax rate is not empty");

		return (!as.isEmpty()) ? new ArrayList<>(as.getCollection()) : null;
	}

	private void loadNextId() {
		LogService.trace(DataManagement.class, "Getting next id");

		try {
			Properties ids = FileManagement.instance().loadProperties(FileNameResolver.NEXT_ID);

			this.nextClientID = Integer.parseInt(ids.getProperty("next_client_id"));
			this.nextEstimateID = Integer.parseInt(ids.getProperty("next_estimate_id"));

		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load next_id file", true, e);
		}
	}

	private void saveNextId() {
		LogService.trace(DataManagement.class, "Putting next id");

		int clienteId = nextClientID;
		int fatturaId = nextEstimateID;

		loadNextId();

		if (clienteId > nextClientID || fatturaId > nextEstimateID) {
			Properties prop = new Properties();
			prop.put("next_client_id", String.valueOf(nextClientID));
			prop.put("next_estimate_id", String.valueOf(nextEstimateID));

			try {
				FileManagement.instance().saveProperties(FileNameResolver.NEXT_ID, prop);
			} catch (IOException e) {
				LogService.error(this.getClass(), "Cannot save next_id file", true, e);
			}
		}
	}

	public String getNextClientID() {
		String id;
		if (nextClientID == -1)
			loadNextId();
		id = Integer.toString(nextClientID);
		nextClientID++;
		return id;
	}

	public String getNextEstimateID() {
		String id;
		if (nextEstimateID == -1)
			loadNextId();
		id = Integer.toString(nextEstimateID);
		nextEstimateID++;
		return id;
	}
}
