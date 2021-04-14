package tfs.estimates;

import tfs.estimates.service.AutoSaveService;
import tfs.estimates.service.LogService;
import tfs.estimates.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainEstimates extends Application {
	private Thread autoSave;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		LogService.info(MainEstimates.class, "::: Welcome in Termoidraulica-fs software! :::");

		//start saveManagement thread
		AutoSaveService sm = new AutoSaveService();
		autoSave = new Thread(sm, "Auto Save Thread");
		autoSave.start();

		try {
			ViewManager viewManager = new ViewManager(primaryStage);
			viewManager.start();
		} catch (Exception e) {
			LogService.warning(ViewManager.class, "Exception ", false, e);
		}
	}

	@Override
	public void stop() {
		AutoSaveService.saveAndBackup();
		autoSave.interrupt();
		LogService.info(AutoSaveService.class, "::: Application closed by user. Bye ;-) :::");
		System.exit(0);
	}

}
