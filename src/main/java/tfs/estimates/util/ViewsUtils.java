package tfs.estimates.util;

import tfs.estimates.MainFatture;
import tfs.estimates.service.LogService;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewsUtils {
	/**
	 * Classe per gestire le viste personalizzate con JavaFX
	 */

	public static Object getFXML(String name) {
		return loadFXML(buildPath(name));
	}

	private static Object loadFXML(String path) {
		try {
			FXMLLoader loader = new FXMLLoader(MainFatture.class.getResource(path));
			return loader.load();
		} catch (IOException e) {
			LogService.warning(ViewsUtils.class, "Error: cannot load view " + path, true, e);
			return null;
		}
	}

	private static String buildPath(String name) {
		return "/views/" + name + ".fxml";
	}
}
