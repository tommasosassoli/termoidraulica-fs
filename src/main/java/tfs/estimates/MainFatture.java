package tfs.estimates;

import tfs.estimates.service.AutoSaveService;
import tfs.estimates.service.LogService;
import tfs.estimates.view.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFatture extends Application{
	private Thread autoSave;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		LogService.info(MainFatture.class, "::: Welcome in Termoidraulica-fs software! :::");
		MainViewController.instance().loadView(primaryStage);
		
		//start saveManagement thread
		AutoSaveService sm = new AutoSaveService();
		autoSave = new Thread(sm, "Auto Save Thread");
		autoSave.start();
	}

	@Override
	public void stop() {
		AutoSaveService.saveAndBackup();
		autoSave.interrupt();
		LogService.info(AutoSaveService.class, "::: Application closed by user. Bye ;-) :::");
		System.exit(0);
	}

}
