package fatture.view;

import fatture.service.AutoSaveService;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MenuBarController {

	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
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
		MainViewController.launchNewWindow("RestoreBackup", "RestoreBackup", false, false);
	}
}
