package tfs.estimates.view.controller;

import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.service.AutoSaveService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import tfs.estimates.view.ViewManager;
import tfs.estimates.view.controller.AbstractController;

public class MenuBarController extends AbstractController {

	public MenuBarController(ViewManager viewManager) {
		super(viewManager);
	}

	@FXML
	private void goHome() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
	
	@FXML
	private void saveModified() {
		AutoSaveService.saveAll();
	}
	
	@FXML
	private void closeApp() {
		Platform.exit();
	}
	
	@FXML
	private void refreshData() {
		AutoSaveService.refreshAll();
	}
	
	@FXML
	private void restoreBackup() {
		getViewManager().launchNewWindow(ViewResolver.RESTORE_BACKUP, "Ripristina Backup", false, false);
	}
}
