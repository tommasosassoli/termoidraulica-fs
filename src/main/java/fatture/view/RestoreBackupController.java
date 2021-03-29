package fatture.view;

import java.io.File;

import fatture.service.AutoSaveService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import static fatture.util.FileNameResolver.ROOT_PATH;

public class RestoreBackupController {
	@FXML
	private ListView<File> backupList;

	public void initialize() {
		File[] files = new File(ROOT_PATH + "/backup/").listFiles();
		
		backupList.setItems(FXCollections.observableArrayList(files));
	}

	@FXML
	private void restore() {
		boolean result = AutoSaveService.restoreBackup(backupList.getSelectionModel().getSelectedItem().getName());
		if(result) {
			((Stage) backupList.getScene().getWindow()).close(); // recupero la scena e la chiudo
		}
		else
			MainViewController.launchInfoDialog("ERRORE durante il recupero del backup");
	}
}
