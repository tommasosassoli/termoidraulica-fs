package tfs.view;

public abstract class AbstractController {
    private ViewManager viewManager;

    public AbstractController(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    protected ViewManager getViewManager() {
        return viewManager;
    }

    public void refresh() {

    }

    public void refresh(Object arg) {

    }
}
