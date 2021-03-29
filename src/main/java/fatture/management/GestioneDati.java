package fatture.management;

import fatture.model.AliquotaIva;
import fatture.model.Cliente;
import fatture.model.DatiAzienda;
import fatture.model.Fattura;
import fatture.model.serialization.AliquoteIvaSerialization;
import fatture.model.serialization.ClientiSerialization;
import fatture.model.serialization.FattureSerialization;
import fatture.service.LogService;
import fatture.util.FileNameResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Gestione dati con utilizzo della classe gestioneFile ( salvataggio su file)
 */
public class GestioneDati {
	private static GestioneDati instance;

	private int nextClienteID = -1;
	private int nextFatturaID = -1;

	public static GestioneDati instance() {
		if (instance == null)
			instance = new GestioneDati();
		return instance;
	}

	public List<Cliente> getClienti() throws IOException {
		LogService.trace(GestioneDati.class, "Getting clients");

		ClientiSerialization cs = GestioneFile.instance().loadXmlFile(FileNameResolver.CLIENTI,
				ClientiSerialization.class);

		LogService.debug(GestioneDati.class, (cs.isEmpty()) ? "Clients list is empty" : "Clients list is not empty");

		return (!cs.isEmpty()) ? new ArrayList<>(cs.getCollection()) : null;
	}

	public void commitClienti(ClientiSerialization clienti) throws IOException {
		LogService.trace(GestioneDati.class, "Putting clients");

		GestioneFile.instance().saveXmlFile(FileNameResolver.CLIENTI, clienti);
		saveNextId();
	}

	public List<Fattura> getFatture() throws IOException {
		LogService.trace(GestioneDati.class, "Getting quotes");

		FattureSerialization fs = GestioneFile.instance().loadXmlFile(FileNameResolver.FATTURE,
				FattureSerialization.class);

		LogService.debug(GestioneDati.class, (fs.isEmpty()) ? "Quotes list is empty" : "Quotes list is not empty");

		return (!fs.isEmpty()) ? new ArrayList<>(fs.getCollection()) : null;
	}

	public void commitFatture(FattureSerialization fatture) throws IOException {
		LogService.trace(GestioneDati.class, "Putting quotes");

		GestioneFile.instance().saveXmlFile(FileNameResolver.FATTURE, fatture);
		saveNextId();
	}

	public DatiAzienda getDatiAzienda() throws IOException {
		LogService.trace(GestioneDati.class, "Getting company data");

		Properties p = GestioneFile.instance().loadProperties(FileNameResolver.DATI_AZIENDA);

		LogService.debug(GestioneDati.class, (p.isEmpty()) ? "Company data is empty" : "Company data is not empty");

		return new DatiAzienda(p);
	}

	public void commitDatiAzienda(DatiAzienda dati) throws IOException {
		LogService.trace(GestioneDati.class, "Putting company data");

		GestioneFile.instance().saveProperties(FileNameResolver.DATI_AZIENDA, dati.exportInProperties());
	}

	public List<AliquotaIva> getAliquote() throws IOException {
		LogService.trace(GestioneDati.class, "Getting aliquote iva");

		AliquoteIvaSerialization as = GestioneFile.instance().loadXmlFile(FileNameResolver.ALIQUOTE_IVA,
				AliquoteIvaSerialization.class);

		LogService.debug(GestioneDati.class, (as.isEmpty()) ? "Aliquote iva list is empty" : "Aliquote iva is not empty");

		return (!as.isEmpty()) ? new ArrayList<>(as.getCollection()) : null;
	}

	private void loadNextId() {
		LogService.trace(GestioneDati.class, "Getting next id");

		try {
			Properties ids = GestioneFile.instance().loadProperties(FileNameResolver.NEXT_ID);

			this.nextClienteID = Integer.parseInt(ids.getProperty("next_id_clienti"));
			this.nextFatturaID = Integer.parseInt(ids.getProperty("next_id_fatture"));

		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load next_id file", true, e);
		}
	}

	private void saveNextId() {
		LogService.trace(GestioneDati.class, "Putting next id");

		int clienteId = nextClienteID;
		int fatturaId = nextFatturaID;

		loadNextId();

		if (clienteId > nextClienteID || fatturaId > nextFatturaID) {
			Properties prop = new Properties();
			prop.put("next_id_clienti", String.valueOf(nextClienteID));
			prop.put("next_id_fatture", String.valueOf(nextFatturaID));

			try {
				GestioneFile.instance().saveProperties(FileNameResolver.NEXT_ID, prop);
			} catch (IOException e) {
				LogService.error(this.getClass(), "Cannot save next_id file", true, e);
			}
		}
	}

	public String getNextClienteID() {
		String id;
		if (nextClienteID == -1)
			loadNextId();
		id = Integer.toString(nextClienteID);
		nextClienteID++;
		return id;
	}

	public String getNextFatturaID() {
		String id;
		if (nextFatturaID == -1)
			loadNextId();
		id = Integer.toString(nextFatturaID);
		nextFatturaID++;
		return id;
	}
}
