package tfs.estimates.management.logic;

import tfs.estimates.management.storage.DataManagement;
import tfs.estimates.model.Client;
import tfs.estimates.model.serialization.ClientsSerialization;
import tfs.estimates.service.AutoSaveService;
import tfs.estimates.service.LogService;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ClientsManagement {
	private static ClientsManagement instance;
	private final HashMap<String, Client> clients = new HashMap<>();

	private ClientsManagement() {
		loadData();
	}

	public void loadData() {
		LogService.trace(ClientsManagement.class, "Loading clients data");

		try {
			clients.clear();
			List<Client> lc = DataManagement.instance().getClients();

			if (lc != null)
				for (Client c : lc) {
					clients.put(c.getId(), c);
				}
		} catch (IOException e) {
			LogService.info(this.getClass(), "File clienti is empty");
		}
	}

	public void commitData() {
		LogService.trace(ClientsManagement.class, "Committing clients data");

		try {
			DataManagement.instance().commitClients(new ClientsSerialization(clients.values()));
		} catch (IOException e) {
			LogService.error(this.getClass(), "Error during saving the clienti file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(ClientsManagement.class, "Refreshing clients data");

		commitData();
		loadData();
	}

	public static ClientsManagement instance() {
		if (instance == null)
			instance = new ClientsManagement();
		return instance;
	}

	public String calcID() {
		return DataManagement.instance().getNextClientID();
	}

	// ******************* functions

	public String addClient(String nome, String cognome, String residenza, String comune, String provincia,
							 String cap, String cf, String note) {

		String id = calcID();
		Client c = new Client(id, nome, cognome, residenza, comune, provincia, cap, cf, note);

		clients.put(id, c);
		return id;
	}

	public Collection<Client> getClients() {
		return clients.values();
	}

	public Client getClient(String id) {
		return clients.get(id);
	}

	public Client deleteClient(String id) {
		return clients.remove(id);
	}
}
