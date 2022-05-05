package tfs.gui.view.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.view.ViewManager;

public class OpenViewTask extends Task<Void> {
    private final ViewManager viewManager;
    private final ViewResolver viewName;
    private final Object arg;

    public OpenViewTask(ViewManager viewManager, ViewResolver viewName, Object arg) {
        this.viewManager = viewManager;
        this.viewName = viewName;
        this.arg = arg;
    }

    @Override
    protected Void call() throws Exception {
        viewManager.refreshView(viewName, arg);
        Platform.runLater(() -> viewManager.activate(viewName));
        return null;
    }
}
