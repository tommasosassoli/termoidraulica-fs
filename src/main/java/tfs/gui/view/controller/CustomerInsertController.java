package tfs.gui.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import tfs.business.endpoint.CustomerEndPoint;
import tfs.business.model.customer.Customer;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class CustomerInsertController extends AbstractController {
	private CustomerEndPoint endPoint = new CustomerEndPoint();
	@FXML
	private TextField name;
	@FXML
	private ImageView nameRequired;
	@FXML
	private TextField surname;
	@FXML
	private ImageView surnameRequired;
	@FXML
	private TextField residence;
	@FXML
	private TextField municipality;
	@FXML
	private TextField province;
	@FXML
	private TextField cap;
	@FXML
	private TextField fiscalCode;
	@FXML
	private TextArea extraNotes;

	public CustomerInsertController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		name.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = name.getText();
				String l1 = txt.substring(0, 1).toUpperCase();
				name.setText(l1 + txt.substring(1).toLowerCase());
			}
		});

		surname.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = surname.getText();
				String l1 = txt.substring(0, 1).toUpperCase();
				surname.setText(l1 + txt.substring(1).toLowerCase());
			}
		});

		municipality.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal)
				municipality.setText(municipality.getText().toUpperCase());
		});

		province.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = province.getText().toUpperCase();
				if (txt.length() == 2 && txt.matches("[a-zA-Z]+"))
					province.setText(txt);
				else
					province.setText("");
			}
		});

		fiscalCode.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = fiscalCode.getText();
				if ((txt.length() == 16 || txt.length() == 11) && txt.matches("[a-zA-Z0-9]+"))
					fiscalCode.setText(txt.toUpperCase());
				else
					fiscalCode.setText("");
			}
		});

		cap.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = cap.getText();
				if (!(txt.length() == 5 && txt.matches("[0-9]+")))
					cap.setText("");
			}
		});
	}

	@FXML
	private void clear() {
		name.clear();
		surname.clear();
		residence.clear();
		municipality.clear();
		province.clear();
		cap.clear();
		fiscalCode.clear();
		extraNotes.clear();
	}

	@FXML
	private void insert() {
		if (validateInput()) {
			Customer c = new Customer(null, name.getText(), surname.getText(),
					residence.getText(), municipality.getText(), province.getText(), cap.getText(),
					fiscalCode.getText(), extraNotes.getText());

			boolean result = endPoint.addCustomer(c);
			if (result) {
				this.getViewManager().activate(ViewResolver.HOME);
			} else
				ViewManager.launchInfoDialog("Non è stato possibile inserire il cliente");
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	private boolean validateInput() {
		if (name.getText().isEmpty()) {
			nameRequired.setVisible(true);
			return false;
		} else if (surname.getText().isEmpty()) {
			surnameRequired.setVisible(true);
			return ViewManager.launchConfirmDialog(""
					+ "La casella del cognome e' vuota.\nVuoi inserire ugualmente il cliente?");
		} else {
			nameRequired.setVisible(false);
			surnameRequired.setVisible(false);
			return true;
		}
	}
}
