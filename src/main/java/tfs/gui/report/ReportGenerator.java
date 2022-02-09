package tfs.gui.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tfs.business.endpoint.CompanyDataEndPoint;
import tfs.business.model.companydata.CompanyData;
import tfs.business.model.estimate.Estimate;
import tfs.business.resolvers.FileResolver;
import tfs.service.LogService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ReportGenerator {
	private final Estimate estimate;
	private final FileResolver fileName;
	private boolean printSuccess = false;

	private CompanyDataEndPoint cdEndPoint = new CompanyDataEndPoint();
	private CompanyData cd;

	public ReportGenerator(Estimate estimate, FileResolver fileName) {
		this.estimate = estimate;
		this.fileName = fileName;
		this.cd = cdEndPoint.getCompanyData();
	}

	private HashMap<String, Object> getHeadParam() {
		HashMap<String, Object> param = new HashMap<>();
		param.put("NOME_AZIENDA", cd.getCompanyName());
		param.put("TELEFONO", cd.getPhone());
		param.put("SEDE", cd.getHeadquarter());
		param.put("CITTA", cd.getMunicipality());
		param.put("CAP", cd.getCap());
		param.put("PROVINCIA", cd.getProvince());
		param.put("IBAN", cd.getIban());

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
			jv.setTitle(cd.getCompanyName());
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
