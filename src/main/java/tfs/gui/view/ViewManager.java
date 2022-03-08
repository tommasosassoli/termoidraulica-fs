package tfs.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tfs.business.dao.daofactory.CompanyDataDaoFactory;
import tfs.business.model.companydata.CompanyData;
import tfs.gui.resolvers.ViewResolver;
import tfs.service.LogService;
import tfs.gui.view.controller.MenuBarController;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ViewManager {
	private final Stage primaryStage;
	private BorderPane mainLayout;
	private final HashMap<ViewResolver, Pane> views = new HashMap<>();
	private static CompanyData companyData = CompanyDataDaoFactory.getDao().getCompanyData();


	public ViewManager(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle(companyData.getCompanyName());
	}

	public void start() {
		initLayout();
		initViews();
		activate(ViewResolver.HOME);

		primaryStage.show();
	}

	private void initLayout() {
		// init the root layout
		mainLayout = (BorderPane) this.loadFXML(ViewResolver.ROOT_LAYOUT, null);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);

		// init the menu bar
		MenuBar hb = (MenuBar) this.loadFXML(ViewResolver.MENU_BAR, new MenuBarController(this));
		mainLayout.setTop(hb);

		// init the icon
		primaryStage.getIcons().add(new Image(ViewManager.class.getResourceAsStream("/views/icons/logo_ts.png")));
	}

	private void initViews() {		//TODO make tests
		for(ViewResolver v : ViewResolver.getViews()) {
			try {
				LogService.trace(ViewManager.class, "Loading view: " + v.getName());
				if(v.getControllerClassName() != null) {
					Class<?> c = Class.forName(v.getControllerClassName());
					Constructor<?> constructor = c.getConstructor(ViewManager.class);
					AbstractController controller = (AbstractController) constructor.newInstance(this);

					Pane pane = (Pane) loadFXML(v, controller);
					if (pane != null)
						pane.setUserData(controller);
					views.put(v, pane);
				}
			} catch (ReflectiveOperationException e) {
				LogService.error(ViewManager.class, "Cannot load view " + v.getName(), true, e);
			}
		}
	}

	private Object loadFXML(ViewResolver name, AbstractController controller) {
		try {
			FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/views" + name));
			loader.setController(controller);
			return loader.load();
		} catch (IOException e) {
			LogService.warning(ViewManager.class, "Error: cannot load view " + name.getName(), true, e);
			return null;
		}
	}

	public void activate(ViewResolver name) {
		mainLayout.setCenter(views.get(name));//TODO test
		((AbstractController) views.get(name).getUserData()).refresh();
	}

	public void activate(ViewResolver name, Object arg) {
		mainLayout.setCenter(views.get(name));//TODO test
		((AbstractController) views.get(name).getUserData()).refresh(arg);
	}

	public static void launchInfoDialog(String str) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(companyData.getCompanyName());
		alert.setHeaderText(null);
		alert.setContentText(str);

		alert.showAndWait();
	}
	
	public static void launchWarningDialog(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(str);
	
		alert.showAndWait();
	}

	public static void launchErrorDialog(String str) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(str);
	
		alert.showAndWait();
	}

	public static boolean launchConfirmDialog(String str) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(companyData.getCompanyName());
		alert.setHeaderText(null);
		alert.setContentText(str);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}
	

	public static String launchTextInputDialog(String str) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(companyData.getCompanyName());
		dialog.setHeaderText(null);
		dialog.setContentText(str);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			return result.get();
		else
			return null;
	}

	public static <T> T launchListInputDialog(String str, List<T> list, T defaultChoice) {
		ChoiceDialog<T> dialog = new ChoiceDialog<>(defaultChoice, list);
		dialog.setTitle(companyData.getCompanyName());
		dialog.setHeaderText(null);
		dialog.setContentText(str);

		Optional<T> result = dialog.showAndWait();
		if (result.isPresent())
			return result.get();
		else
			return null;
	}

	public void launchNewWindow(ViewResolver name, String title, boolean showAndWait, boolean maximized){
		launchNewWindow(name, title, showAndWait, maximized, null);
	}

	public void launchNewWindow(ViewResolver name, String title, boolean showAndWait, boolean maximized, Object arg) {
		Pane view = views.get(name);
		Pane root = new Pane();
		root.getChildren().addAll(view);
		root.setUserData(view.getUserData());

		Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(maximized);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);

        if (arg != null)
			((AbstractController) root.getUserData()).refresh(arg);
        else
			((AbstractController) root.getUserData()).refresh();

        if(showAndWait)
        	stage.showAndWait();
        else
        	stage.show();
	}

}
