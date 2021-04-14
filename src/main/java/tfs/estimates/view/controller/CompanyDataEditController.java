package tfs.estimates.view.controller;

import tfs.estimates.management.logic.CompanyDataManagement;
import tfs.estimates.model.CompanyData;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.service.AutoSaveService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tfs.estimates.view.ViewManager;

public class CompanyDataEditController extends AbstractController {
	private CompanyData companyData;
	@FXML
	private TextField companyName;
	@FXML
	private TextField iban;
	@FXML
	private TextField phone;
	@FXML
	private TextField headquarter;
	@FXML
	private TextField municipality;
	@FXML
	private TextField province;
	@FXML
	private TextField cap;
	@FXML
	private TextField piva;

	public CompanyDataEditController(ViewManager viewManager) {
		super(viewManager);
	}

	@Override
	public void refresh() {
		companyData = CompanyDataManagement.instance().getCompanyData();
		companyName.setText(companyData.getCompanyName());
		phone.setText(companyData.getPhone());
		headquarter.setText(companyData.getHeadquarter());
		municipality.setText(companyData.getMunicipality());
		province.setText(companyData.getProvince());
		cap.setText(companyData.getCap());
		iban.setText(companyData.getIban());
		piva.setText(companyData.getPiva());
	}
	
	@FXML
	private void save() {
		companyData.setCompanyName(companyName.getText());
		companyData.setPhone(phone.getText());
		companyData.setHeadquarter(headquarter.getText());
		companyData.setMunicipality(municipality.getText());
		companyData.setProvince(province.getText());
		companyData.setCap(cap.getText());
		companyData.setIban(iban.getText());
		companyData.setPiva(piva.getText());
		
		AutoSaveService.setModified();
		this.getViewManager().activate(ViewResolver.HOME);
	}
	
	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
}
