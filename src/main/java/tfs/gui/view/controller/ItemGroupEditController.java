package tfs.gui.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tfs.business.model.estimate.ItemGroup;
import tfs.service.LogService;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

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
	private void endEdit() {
		itemGroup.setDescription(description.getText());

		close();
	}

	private void close() {
		((Stage) description.getScene().getWindow()).close(); // get the scene and close
	}

}
