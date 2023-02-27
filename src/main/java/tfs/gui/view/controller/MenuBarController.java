package tfs.gui.view.controller;

import javafx.scene.control.CheckMenuItem;
import tfs.gui.resolvers.ViewResolver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class MenuBarController extends AbstractController {
	@FXML
	private CheckMenuItem darkThemeButton;

	public MenuBarController(ViewManager viewManager) {
		super(viewManager);
	}

	@FXML
	private void goHome() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	
	@FXML
	private void closeApp() {
		Platform.exit();
	}

	@FXML
	private void toggleDarkTheme() {
		boolean darkTheme = getViewManager().toggleDarkTheme();
		darkThemeButton.setSelected(darkTheme);
	}
}
