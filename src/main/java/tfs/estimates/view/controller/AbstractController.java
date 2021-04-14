package tfs.estimates.view.controller;

import tfs.estimates.view.ViewManager;

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
