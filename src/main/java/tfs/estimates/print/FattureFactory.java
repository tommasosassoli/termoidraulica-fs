package tfs.estimates.print;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import tfs.estimates.management.logic.CompanyDataManagement;
import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.model.Estimate;
import tfs.estimates.service.LogService;
import tfs.estimates.resolvers.FileResolver;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class FattureFactory {
	private static Estimate estimate;

	private static Collection<Estimate> getFattura() {
		ArrayList<Estimate> list = new ArrayList<>();
		list.add(estimate);
		return list;
	}

	private static HashMap<String, Object> getParamIntestazione() {
		// parametri per intestazione fattura
		HashMap<String, Object> param = new HashMap<>();
		param.put("NOME_AZIENDA", CompanyDataManagement.instance().getCompanyData().getCompanyName());
		param.put("TELEFONO", CompanyDataManagement.instance().getCompanyData().getPhone());
		param.put("SEDE", CompanyDataManagement.instance().getCompanyData().getHeadquarter());
		param.put("CITTA", CompanyDataManagement.instance().getCompanyData().getMunicipality());
		param.put("CAP", CompanyDataManagement.instance().getCompanyData().getCap());
		param.put("PROVINCIA", CompanyDataManagement.instance().getCompanyData().getProvince());
		param.put("IBAN", CompanyDataManagement.instance().getCompanyData().getIban());

		return param;
	}

	public static void printReportFattura(String id, FileResolver fileName) {
		try {
			LogService.info(FattureFactory.class, "Printing...");

			FattureFactory.estimate = EstimatesManagement.instance().getEstimate(id);
			Collection<Estimate> estimateCol = getFattura();
			JRBeanCollectionDataSource mainData = new JRBeanCollectionDataSource(estimateCol);

			LogService.trace(FattureFactory.class, "Reading and compiling report file");

			InputStream in = FattureFactory.class.getResourceAsStream(fileName + ".jrxml");
			JasperReport jr = JasperCompileManager.compileReport(in);
			JasperPrint jp = JasperFillManager.fillReport(jr, getParamIntestazione(), mainData);

			LogService.trace(FattureFactory.class, "Opening report view");

			JasperViewer jv = new JasperViewer(jp, false);
			jv.setTitle(CompanyDataManagement.instance().getCompanyData().getCompanyName());
			jv.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jv.requestFocus();
			jv.setVisible(true);
		} catch (JRException | NullPointerException e) {
			LogService.error(FattureFactory.class, "Error during fattura print with id: " + id, true, e);
		}
	}
	
	public static void printReportFattura(String id) {
		FattureFactory.printReportFattura(id, FileResolver.REPORT_ESTIMATE);
	}
}
