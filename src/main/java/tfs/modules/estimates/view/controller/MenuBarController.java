package tfs.modules.estimates.view.controller;

import tfs.resolvers.ViewResolver;
import tfs.service.AutoSaveService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import tfs.view.AbstractController;
import tfs.view.ViewManager;

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
