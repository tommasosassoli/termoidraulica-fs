package tfs.main;

import tfs.service.LogService;
import tfs.gui.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainEstimates extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		LogService.info(MainEstimates.class, "::: Welcome in Termoidraulica-fs software! :::");

		try {
			ViewManager viewManager = new ViewManager(primaryStage);
			viewManager.start();
		} catch (Exception e) {
			LogService.warning(ViewManager.class, "Exception ", false, e);
		}
	}

	@Override
	public void stop() {
		LogService.info(MainEstimates.class, "::: Application closed by user. Bye ;-) :::");
		System.exit(0);
	}

}
