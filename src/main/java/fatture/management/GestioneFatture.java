package fatture.management;

import fatture.model.Cliente;
import fatture.model.Fattura;
import fatture.model.Materiale;
import fatture.model.SAL;
import fatture.model.serialization.FattureSerialization;
import fatture.service.AutoSaveService;
import fatture.service.LogService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GestioneFatture {
	private static GestioneFatture instance;
	private final HashMap<String, Fattura> fatture = new HashMap<>();

	private GestioneFatture() {
		loadData();
	}

	public void loadData() {
		LogService.trace(GestioneFatture.class, "Loading quotes data");

		try {
			fatture.clear();
			List<Fattura> lf = GestioneDati.instance().getFatture();

			if (lf != null)
				for (Fattura f : lf)
					fatture.put(f.getId(), f);

		} catch (IOException e) {
			LogService.info(this.getClass(), "File fatture is empty");
		}
	}

	public void commitData() {
		LogService.trace(GestioneFatture.class, "Committing quotes data");

		try {
			GestioneDati.instance().commitFatture(new FattureSerialization(fatture.values()));

		} catch (IOException e) {
			LogService.error(this.getClass(), "Error during saving the fatture file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(GestioneFatture.class, "Refreshing quotes data");

		commitData();
		loadData();
	}

	public String calcID() {
		return GestioneDati.instance().getNextFatturaID();
	}

	public static GestioneFatture instance() {
		if (instance == null)
			instance = new GestioneFatture();
		return instance;
	}

	// ******************** funzioni

	public String addFattura(Cliente c, LocalDateTime data) {
		String id = calcID();
		Fattura f = new Fattura(id, c, data);

		fatture.put(f.getId(), f);
		AutoSaveService.setModified();

		return f.getId();
	}

	public Fattura getFattura(String id) {
		return fatture.get(id);
	}

	public Collection<Fattura> getFatture() {
		return fatture.values();
	}

	public Fattura delFattura(String id) {
		AutoSaveService.setModified();
		return fatture.remove(id);
	}

	public void updateFattura(String id, String idCliente, LocalDateTime data) {
		Fattura f = fatture.get(id);

		f.setDataScadenza(data);
		f.setCliente(idCliente);
		AutoSaveService.setModified();
	}

	public void updateMateriali(String id, List<Materiale> list) {
		this.fatture.get(id).overrideMateriali(list);
		AutoSaveService.setModified();
	}

	public void updateSAL(String id, List<SAL> list) {
		if (list != null) {
			Fattura f = fatture.get(id);
			f.overrideSal(list);
			AutoSaveService.setModified();
		}
	}

	/*
	 * public String toString() { String str = ""; for (int i = 0; i <
	 * fatture.size(); i++) { str += "\n" + (i + 1) + "  " +
	 * fatture.get(i).toString(); } return str; }
	 */
}
