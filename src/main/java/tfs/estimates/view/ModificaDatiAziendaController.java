package tfs.estimates.view;

import tfs.estimates.management.logic.CompanyDataManagement;
import tfs.estimates.service.AutoSaveService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificaDatiAziendaController {
	@FXML
	private TextField nomeAzienda;
	@FXML
	private TextField iban;
	@FXML
	private TextField telefono;
	@FXML
	private TextField sede;
	@FXML
	private TextField comune;
	@FXML
	private TextField provincia;
	@FXML
	private TextField cap;
	@FXML
	private TextField piva;
	
	public void initialize() {
		nomeAzienda.setText(CompanyDataManagement.instance().getCompanyData().getCompanyName());
		telefono.setText(CompanyDataManagement.instance().getCompanyData().getPhone());
		sede.setText(CompanyDataManagement.instance().getCompanyData().getHeadquarter());
		comune.setText(CompanyDataManagement.instance().getCompanyData().getMunicipality());
		provincia.setText(CompanyDataManagement.instance().getCompanyData().getProvince());
		cap.setText(CompanyDataManagement.instance().getCompanyData().getCap());
		iban.setText(CompanyDataManagement.instance().getCompanyData().getIban());
		piva.setText(CompanyDataManagement.instance().getCompanyData().getPiva());
	}
	
	@FXML
	private void saveData() {
		CompanyDataManagement.instance().getCompanyData().setCompanyName(nomeAzienda.getText());
		CompanyDataManagement.instance().getCompanyData().setPhone(telefono.getText());
		CompanyDataManagement.instance().getCompanyData().setHeadquarter(sede.getText());
		CompanyDataManagement.instance().getCompanyData().setMunicipality(comune.getText());
		CompanyDataManagement.instance().getCompanyData().setProvince(provincia.getText());
		CompanyDataManagement.instance().getCompanyData().setCap(cap.getText());
		CompanyDataManagement.instance().getCompanyData().setIban(iban.getText());
		CompanyDataManagement.instance().getCompanyData().setPiva(piva.getText());
		
		AutoSaveService.setModified();
		
		MainViewController.instance().setView("Principale");
	}
	
	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
	}
}
