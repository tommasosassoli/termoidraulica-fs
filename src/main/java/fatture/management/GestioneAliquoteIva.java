package fatture.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fatture.model.AliquotaIva;
import fatture.service.LogService;

public class GestioneAliquoteIva {
	private static GestioneAliquoteIva instance;
	private ArrayList<AliquotaIva> aliquote = new ArrayList<>();

	private GestioneAliquoteIva() {
		LogService.trace(GestioneAliquoteIva.class, "Loading aliquote iva data");

		try {
			aliquote = (ArrayList<AliquotaIva>) GestioneDati.instance().getAliquote();
		} catch (IOException e) {
			LogService.error(this.getClass(), "Cannot load the aliquote_iva file", true, e);
		}
	}
	
	public List<AliquotaIva> getAliquote() {
		return aliquote;
	}
	
	public AliquotaIva getFromValue(double valueIva) {
		for(AliquotaIva a:aliquote) {
			if(a.getAliquota() == valueIva)
				return a;
		}
		return null;
	}

	public static GestioneAliquoteIva instance() {
		if(instance == null)
			instance = new GestioneAliquoteIva();
		return instance;
	}
}
