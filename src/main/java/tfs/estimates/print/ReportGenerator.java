package tfs.estimates.print;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tfs.estimates.management.logic.CompanyDataManagement;
import tfs.estimates.model.Estimate;
import tfs.estimates.resolvers.FileResolver;
import tfs.estimates.service.LogService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ReportGenerator {
	private final Estimate estimate;
	private final FileResolver fileName;
	private boolean printSuccess = false;

	public ReportGenerator(Estimate estimate, FileResolver fileName) {
		this.estimate = estimate;
		this.fileName = fileName;
	}

	private HashMap<String, Object> getHeadParam() {
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

	public void print() {
		try {
			LogService.info(ReportGenerator.class, "Printing...");

			if (estimate == null || fileName == null)
				throw new NullPointerException();

			Collection<Estimate> estimateCol = new ArrayList<>();
			estimateCol.add(estimate);
			JRBeanCollectionDataSource mainData = new JRBeanCollectionDataSource(estimateCol);

			LogService.trace(ReportGenerator.class, "Reading and compiling report file");

			InputStream in = ReportGenerator.class.getResourceAsStream(fileName + ".jrxml");
			JasperReport jr = JasperCompileManager.compileReport(in);
			JasperPrint jp = JasperFillManager.fillReport(jr, getHeadParam(), mainData);

			LogService.trace(ReportGenerator.class, "Opening report view");

			JasperViewer jv = new JasperViewer(jp, false);
			jv.setTitle(CompanyDataManagement.instance().getCompanyData().getCompanyName());
			jv.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			jv.requestFocus();
			jv.setVisible(true);

			printSuccess = true;
		} catch (JRException | NullPointerException e) {
			LogService.error(ReportGenerator.class, "Error during estimate print", true, e);
		}
	}

	public boolean isPrintSuccessful() {
		return printSuccess;
	}
}
