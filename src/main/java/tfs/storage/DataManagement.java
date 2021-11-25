package tfs.storage;

import tfs.modules.common.model.TaxRate;
import tfs.modules.estimates.model.Client;
import tfs.modules.common.model.CompanyData;
import tfs.modules.estimates.model.Estimate;
import tfs.modules.common.model.serialization.TaxRatesSerialization;
import tfs.modules.estimates.model.serialization.ClientsSerialization;
import tfs.modules.estimates.model.serialization.EstimatesSerialization;
import tfs.modules.riba.model.Receipt;
import tfs.modules.riba.model.serialization.ReceiptsSerialization;
import tfs.service.LogService;
import tfs.resolvers.FileResolver;

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

		ClientsSerialization cs = FileManagement.instance().loadXmlFile(FileResolver.CLIENTS,
				ClientsSerialization.class);

		LogService.debug(DataManagement.class, (cs.isEmpty()) ? "Clients list is empty" : "Clients list is not empty");

		return (!cs.isEmpty()) ? new ArrayList<>(cs.getCollection()) : null;
	}

	public void commitClients(ClientsSerialization clients) throws IOException {
		LogService.trace(DataManagement.class, "Putting clients");

		FileManagement.instance().saveXmlFile(FileResolver.CLIENTS, clients);
		saveNextId();
	}

	public List<Estimate> getEstimates() throws IOException {
		LogService.trace(DataManagement.class, "Getting Estimate");

		EstimatesSerialization fs = FileManagement.instance().loadXmlFile(FileResolver.ESTIMATES,
				EstimatesSerialization.class);

		LogService.debug(DataManagement.class, (fs.isEmpty()) ? "Estimate list is empty" : "Estimate list is not empty");

		return (!fs.isEmpty()) ? new ArrayList<>(fs.getCollection()) : null;
	}

	public void commitEstimates(EstimatesSerialization fatture) throws IOException {
		LogService.trace(DataManagement.class, "Putting Estimate");

		FileManagement.instance().saveXmlFile(FileResolver.ESTIMATES, fatture);
		saveNextId();
	}

	public CompanyData getCompanyData() throws IOException {
		LogService.trace(DataManagement.class, "Getting company data");

		Properties p = FileManagement.instance().loadProperties(FileResolver.COMPANY_DATA);

		LogService.debug(DataManagement.class, (p.isEmpty()) ? "Company data is empty" : "Company data is not empty");

		return new CompanyData(p);
	}

	public void commitCompanyData(CompanyData data) throws IOException {
		LogService.trace(DataManagement.class, "Putting company data");

		FileManagement.instance().saveProperties(FileResolver.COMPANY_DATA, data.exportInProperties());
	}

	public List<TaxRate> getTaxRates() throws IOException {
		LogService.trace(DataManagement.class, "Getting tax rates");

		TaxRatesSerialization as = FileManagement.instance().loadXmlFile(FileResolver.TAX_RATES,
				TaxRatesSerialization.class);

		LogService.debug(DataManagement.class, (as.isEmpty()) ? "Tax rate list is empty" : "Tax rate is not empty");

		return (!as.isEmpty()) ? new ArrayList<>(as.getCollection()) : null;
	}

	public List<Receipt> getReceipts() throws IOException {
		LogService.trace(DataManagement.class, "Getting receipts");

		ReceiptsSerialization rs = FileManagement.instance().loadXmlFile(FileResolver.RECEIPTS,
				ReceiptsSerialization.class);

		LogService.debug(DataManagement.class, (rs.isEmpty()) ? "Receipts list is empty" : "Receipts list is not empty");

		return (!rs.isEmpty()) ? new ArrayList<>(rs.getCollection()) : null;
	}

	public void commitReceipts(ReceiptsSerialization receipts) throws IOException {
		LogService.trace(DataManagement.class, "Putting receipts");

		FileManagement.instance().saveXmlFile(FileResolver.RECEIPTS, receipts);
		saveNextId();
	}

	private void loadNextId() {
		LogService.trace(DataManagement.class, "Getting next id");

		try {
			Properties ids = FileManagement.instance().loadProperties(FileResolver.NEXT_ID);

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
				FileManagement.instance().saveProperties(FileResolver.NEXT_ID, prop);
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
