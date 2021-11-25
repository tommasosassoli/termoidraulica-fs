package tfs.view.controller;

import java.io.File;

import tfs.service.AutoSaveService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tfs.resolvers.FileResolver;
import tfs.view.AbstractController;
import tfs.view.ViewManager;

public class RestoreBackupController extends AbstractController {
	@FXML
	private ListView<File> backupList;

	public RestoreBackupController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		File[] files = new File(FileResolver.ROOT_PATH + "/backup/").listFiles();
		
		backupList.setItems(FXCollections.observableArrayList(files));
	}

	@FXML
	private void restore() {
		boolean result = AutoSaveService.restoreBackup(backupList.getSelectionModel().getSelectedItem().getName());
		if(result) {
			((Stage) backupList.getScene().getWindow()).close(); // recupero la scena e la chiudo
		}
		else
			ViewManager.launchInfoDialog("ERRORE durante il recupero del backup");
	}
}
