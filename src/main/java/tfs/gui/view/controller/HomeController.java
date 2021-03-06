package tfs.gui.view.controller;

import javafx.fxml.FXML;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

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
	private void receiptInsert() {
		this.getViewManager().activate(ViewResolver.RECEIPT_INSERT);
	}

	@FXML
	private void receiptList() {
		this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
	}

	@FXML
	private void companyDataEdit() {
		this.getViewManager().activate(ViewResolver.COMPANY_DATA_EDIT);
	}

}
