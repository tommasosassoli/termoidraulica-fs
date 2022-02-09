package tfs.gui.view.controller;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tfs.business.endpoint.CustomerEndPoint;
import tfs.business.model.customer.Customer;
import tfs.gui.resolvers.ViewResolver;
import tfs.service.LogService;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class CustomerEditController extends AbstractController {
	private String clientID;
	private CustomerEndPoint endPoint = new CustomerEndPoint();
	private Customer client;
	@FXML
	private TextField ID;
	@FXML
	protected TextField name;
	@FXML
	private ImageView nameRequired;
	@FXML
	protected TextField surname;
	@FXML
	private ImageView surnameRequired;
	@FXML
	protected TextField residence;
	@FXML
	protected TextField municipality;
	@FXML
	protected TextField province;
	@FXML
	protected TextField cap;
	@FXML
	protected TextField fiscalCode;
	@FXML
	protected TextArea extraNotes;

	public CustomerEditController(ViewManager viewManager) {
		super(viewManager);
	}

	private Customer getCustomer() {
		if (client != null)
			return client;
		else if (clientID != null)
			client = endPoint.getCustomer(clientID);
		return client;
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

	@Override
	public void refresh(Object arg) {
		try {
			clientID = (String) arg;
			client = null;
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for CustomerEditController not valid");
		}

		if (getCustomer() != null) {
			ID.setText(client.getId());
			name.setText(client.getName());
			surname.setText(client.getSurname());
			residence.setText(client.getResidence());
			municipality.setText(client.getMunicipality());
			province.setText(client.getProvince());
			cap.setText(client.getCap());
			fiscalCode.setText(client.getCf());
			extraNotes.setText(client.getNotes());
		}
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

	@FXML
	private void save() {
		if (validateInput()) {
			client.setName(name.getText());
			client.setSurname(surname.getText());
			client.setResidence(residence.getText());
			client.setMunicipality(municipality.getText());
			client.setProvince(province.getText());
			client.setCap(cap.getText());
			client.setCf(fiscalCode.getText());
			client.setNotes(extraNotes.getText());

			endPoint.updateCustomer(client);
			this.getViewManager().activate(ViewResolver.HOME);
		}
	}

	@FXML
	private void delete() {
		if (ViewManager.launchConfirmDialog("Sicuro di voler ELIMINARE questo cliente?")) {
			endPoint.deleteCustomer(clientID);

			this.getViewManager().activate(ViewResolver.HOME);
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.CLIENT_LIST);
	}

}
