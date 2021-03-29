package fatture.print;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import fatture.management.GestioneDatiAzienda;
import fatture.management.GestioneFatture;
import fatture.model.Fattura;
import fatture.service.LogService;
import fatture.util.FileNameResolver;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class FattureFactory {
	private static Fattura fattura;

	private static Collection<Fattura> getFattura() {
		ArrayList<Fattura> list = new ArrayList<>();
		list.add(fattura);
		return list;
	}

	private static HashMap<String, Object> getParamIntestazione() {
		// parametri per intestazione fattura
		HashMap<String, Object> param = new HashMap<>();
		param.put("NOME_AZIENDA", GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
		param.put("TELEFONO", GestioneDatiAzienda.instance().getDatiAzienda().getTelefono());
		param.put("SEDE", GestioneDatiAzienda.instance().getDatiAzienda().getsede());
		param.put("CITTA", GestioneDatiAzienda.instance().getDatiAzienda().getCitta());
		param.put("CAP", GestioneDatiAzienda.instance().getDatiAzienda().getCap());
		param.put("PROVINCIA", GestioneDatiAzienda.instance().getDatiAzienda().getProvincia());
		param.put("IBAN", GestioneDatiAzienda.instance().getDatiAzienda().getIban());

		return param;
	}

	public static void printReportFattura(String id, FileNameResolver fileName) {
		try {
			LogService.info(FattureFactory.class, "Printing...");

			FattureFactory.fattura = GestioneFatture.instance().getFattura(id);
			Collection<Fattura> fatturaCol = getFattura();
			JRBeanCollectionDataSource mainData = new JRBeanCollectionDataSource(fatturaCol);

			LogService.trace(FattureFactory.class, "Reading and compiling report file");

			InputStream in = FattureFactory.class.getResourceAsStream(fileName + ".jrxml");
			JasperReport jr = JasperCompileManager.compileReport(in);
			JasperPrint jp = JasperFillManager.fillReport(jr, getParamIntestazione(), mainData);

			LogService.trace(FattureFactory.class, "Opening report view");

			JasperViewer jv = new JasperViewer(jp, false);
			jv.setTitle(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
			jv.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jv.requestFocus();
			jv.setVisible(true);
		} catch (JRException | NullPointerException e) {
			LogService.error(FattureFactory.class, "Error during fattura print with id: " + id, true, e);
		}
	}
	
	public static void printReportFattura(String id) {
		FattureFactory.printReportFattura(id, FileNameResolver.REPORT_FATTURA);
	}
}
