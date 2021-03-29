package fatture.management;

import java.io.IOException;

import fatture.model.DatiAzienda;
import fatture.service.LogService;

public class GestioneDatiAzienda {
	private static GestioneDatiAzienda instance;
	private DatiAzienda datiAzienda;

	private GestioneDatiAzienda() {
		this.loadFile();
	}

	public DatiAzienda getDatiAzienda() {
		return datiAzienda;
	}

	public void loadFile() {
		LogService.trace(GestioneDatiAzienda.class, "Loading company data");

		try {
			datiAzienda = GestioneDati.instance().getDatiAzienda();
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load dati_azienda file", true, e);
			datiAzienda = new DatiAzienda();
		}
	}

	public void commitDatiAzienda() {
		LogService.trace(GestioneDatiAzienda.class, "Committing company data");

		try {
			GestioneDati.instance().commitDatiAzienda(datiAzienda);
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot save dati_azienda file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(GestioneDatiAzienda.class, "Refreshing company data");

		commitDatiAzienda();
		loadFile();
	}

	public static GestioneDatiAzienda instance() {
		if (instance == null)
			instance = new GestioneDatiAzienda();
		return instance;
	}

}
