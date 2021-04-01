package tfs.estimates.view;

import java.io.File;

import tfs.estimates.service.AutoSaveService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tfs.estimates.util.FileNameResolver;

public class RestoreBackupController {
	@FXML
	private ListView<File> backupList;

	public void initialize() {
		File[] files = new File(FileNameResolver.ROOT_PATH + "/backup/").listFiles();
		
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
