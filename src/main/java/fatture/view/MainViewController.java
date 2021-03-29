package fatture.view;

import java.util.Map;
import java.util.Optional;

import fatture.management.GestioneDatiAzienda;
import fatture.util.ViewsUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainViewController {

	private static MainViewController instance;
	private Stage primaryStage;
	private BorderPane rootLayout;

	public static MainViewController instance() {
		if (instance == null)
			instance = new MainViewController();
		return instance;
	}

	public void loadView(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());

		loadRootLayout();
		loadPrincipale();
		loadMenuBar();
		setScreen();

		primaryStage.show();
	}

	private void setScreen() {
		primaryStage.setMaximized(true);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());

		primaryStage.setMaxWidth(primaryScreenBounds.getWidth());
		primaryStage.setMinWidth(primaryScreenBounds.getWidth());

		primaryStage.setMaxHeight(primaryScreenBounds.getHeight());
		primaryStage.setMinHeight(primaryScreenBounds.getHeight());

	}
	

	private void loadRootLayout() {
		rootLayout = (BorderPane) ViewsUtils.getFXML("RootLayout");
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
	}
	

	private void loadPrincipale() {
		AnchorPane principale = (AnchorPane) ViewsUtils.getFXML("Principale");
		rootLayout.setCenter(principale);
	}

	private void loadMenuBar() {
		MenuBar hb = (MenuBar) ViewsUtils.getFXML("MenuBar");
		rootLayout.setTop(hb);
	}

	public void setView(String name) {
		AnchorPane view = (AnchorPane) ViewsUtils.getFXML(name);
		rootLayout.setCenter(view);
	}

	public void minimize() {
		primaryStage.setIconified(true);
	}
	
	public Stage getStage() {
		return primaryStage;
	}

	public static void launchInfoDialog(String str) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
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
		alert.setTitle(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
		alert.setHeaderText(null);
		alert.setContentText(str);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}
	

	public static String launchInputDialog(String str) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
		dialog.setHeaderText(null);
		dialog.setContentText(str);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			return result.get();
		else
			return null;
	}
	
	public static void launchNewWindow(String scene, String title, boolean showAndWait, boolean maximized) {
		Parent root = (Parent) ViewsUtils.getFXML(scene);
		Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(maximized);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainViewController.instance().getStage());
        if(showAndWait)
        	stage.showAndWait();
        else
        	stage.show();
	}

}
