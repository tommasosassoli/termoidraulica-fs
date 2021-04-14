package tfs.estimates.view.controller;

import javafx.fxml.FXML;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.view.ViewManager;

public class HomeController extends AbstractController {

	public HomeController(ViewManager viewManager) {
		super(viewManager);
	}

	@FXML
	private void clientInsert() {
		this.getViewManager().activate(ViewResolver.CLIENT_INSERT);
	}

	@FXML
	private void clientList() {
		this.getViewManager().activate(ViewResolver.CLIENT_LIST);
	}

	@FXML
	private void estimateInsert() {
		this.getViewManager().activate(ViewResolver.ESTIMATE_INSERT);
	}

	@FXML
	private void estimateList() {
		this.getViewManager().activate(ViewResolver.ESTIMATE_LIST);
	}

	@FXML
	private void companyDataEdit() {
		this.getViewManager().activate(ViewResolver.COMPANY_DATA_EDIT);
	}
}
