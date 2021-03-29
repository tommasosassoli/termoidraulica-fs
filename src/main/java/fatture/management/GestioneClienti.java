package fatture.management;

import fatture.model.Cliente;
import fatture.model.serialization.ClientiSerialization;
import fatture.service.AutoSaveService;
import fatture.service.LogService;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GestioneClienti {
	private static GestioneClienti instance;
	private final HashMap<String, Cliente> clienti = new HashMap<>();

	private GestioneClienti() {
		loadData();
	}

	public void loadData() {
		LogService.trace(GestioneClienti.class, "Loading clients data");

		try {
			clienti.clear();
			List<Cliente> lc = GestioneDati.instance().getClienti();

			if (lc != null)
				for (Cliente c : lc) {
					clienti.put(c.getId(), c);
				}
		} catch (IOException e) {
			LogService.info(this.getClass(), "File clienti is empty");
		}
	}

	public void commitData() {
		LogService.trace(GestioneClienti.class, "Committing clients data");

		try {
			GestioneDati.instance().commitClienti(new ClientiSerialization(clienti.values()));
		} catch (IOException e) {
			LogService.error(this.getClass(), "Error during saving the clienti file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(GestioneClienti.class, "Refreshing clients data");

		commitData();
		loadData();
	}

	public static GestioneClienti instance() {
		if (instance == null)
			instance = new GestioneClienti();
		return instance;
	}

	public String calcID() {
		return GestioneDati.instance().getNextClienteID();
	}

	// ******************* funzioni

	public boolean addCliente(String nome, String cognome, String residenza, String comune, String provincia,
			String cap, String cf, String note) {

		String id = calcID();
		Cliente c = new Cliente(id, nome, cognome, residenza, comune, provincia, cap, cf, note);

		clienti.put(id, c); // inserisco il cliente
		AutoSaveService.setModified();
		return true;
	}

	public boolean addCliente(String denominazione, String residenza, String comune, String provincia, String cap,
			String cf, String note) {

		String id = calcID();
		Cliente c = new Cliente(id, denominazione, residenza, comune, provincia, cap, cf, note);

		clienti.put(id, c); // inserisco il cliente
		AutoSaveService.setModified();
		return true;
	}

	public Collection<Cliente> getClienti() {
		return clienti.values();
	}

	public Cliente getCliente(String id) {
		return clienti.get(id);
	}

	public Cliente delCliente(String id) {
		AutoSaveService.setModified();
		return clienti.remove(id);
	}

	public void updateCliente(String id, String nome, String cognome, String residenza, String comune, String provincia,
			String cap, String cf, String note) {

		Cliente temp = clienti.get(id);

		temp.setNome(nome);
		temp.setCognome(cognome);
		temp.setResidenza(residenza);
		temp.setComune(comune);
		temp.setProvincia(provincia);
		temp.setCap(cap);
		temp.setCf(cf);
		temp.setNote(note);
		AutoSaveService.setModified();
	}

	/*
	 * public String toString() { String str = ""; for (int i = 0; i <
	 * clienti.size(); i++) { str += "\n" + (i + 1) + "  " +
	 * clienti.get(i).toString(); } return str; }
	 */

}
