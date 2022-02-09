package tfs.gui.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tfs.business.endpoint.ReceiptEndPoint;
import tfs.business.model.receipt.Receipt;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReceiptInsertController extends AbstractController {
	private ReceiptEndPoint endPoint = new ReceiptEndPoint();
	@FXML
	private TextField foreignId;
	@FXML
	private TextField description;
	@FXML
	private DatePicker date;

	public ReceiptInsertController(ViewManager viewManager) {
		super(viewManager);
	}

	public void refresh() {
		foreignId.clear();
		description.clear();
		date.setValue(LocalDate.now());
	}

	@FXML
	private void insert() {
		if (validateInput()) {
			LocalDateTime d = LocalDateTime.of(date.getValue(), LocalTime.MIDNIGHT);

			Receipt r = new Receipt(foreignId.getText(), description.getText(), d);
			boolean result = endPoint.addReceipt(r);

			if (result) {
				this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
			} else
				ViewManager.launchInfoDialog("Non è stato possibile inserire la ricevuta");
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	private boolean validateInput() {
		if (description.getText().isEmpty()) {
			ViewManager.launchInfoDialog("Inserisci una descrizione");
			return false;
		} else if (foreignId.getText().isEmpty()) {
			return ViewManager.launchConfirmDialog(""
					+ "La casella id e' vuota.\nVuoi inserire ugualmente la ricevuta?");
		} else {
			return true;
		}
	}
}
