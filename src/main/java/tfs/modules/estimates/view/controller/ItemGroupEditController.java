package tfs.modules.estimates.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tfs.modules.estimates.model.ItemGroup;
import tfs.service.AutoSaveService;
import tfs.service.LogService;
import tfs.view.AbstractController;
import tfs.view.ViewManager;

public class ItemGroupEditController extends AbstractController {
	private ItemGroup itemGroup;
	@FXML
	private TextArea description;

	public ItemGroupEditController(ViewManager viewManager) {
		super(viewManager);
	}

	@Override
	public void refresh(Object arg) {
		try {
			itemGroup = (ItemGroup) arg;
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for ItemGroupEditController not valid");
		}

		if (itemGroup != null) {
			description.setText(itemGroup.getDescription());
		}
	}

	@FXML
	private void cancelEdit() {
		close();
	}

	@FXML
	private void endEdit() {
		itemGroup.setDescription(description.getText());

		AutoSaveService.setModified();
		close();
	}

	private void close() {
		((Stage) description.getScene().getWindow()).close(); // get the scene and close
	}

}
