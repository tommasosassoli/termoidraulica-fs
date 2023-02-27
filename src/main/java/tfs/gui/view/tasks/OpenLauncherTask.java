package tfs.gui.view.tasks;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tfs.gui.view.ViewManager;
import tfs.service.LogService;

import java.io.IOException;

public class OpenLauncherTask extends Task<Void> {
    private Stage splashScreen;

    public OpenLauncherTask() {
        splashScreen = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/views/launcher/launcher.fxml"));
            Pane pane = loader.load();
            splashScreen.setScene(new Scene(pane));

        } catch (IOException e) {
            LogService.warning(ViewManager.class, "Error: cannot load splash view ", false, e);
        }
    }

    @Override
    protected Void call() throws Exception {
        if (splashScreen != null)
            splashScreen.show();
        return null;
    }
}
