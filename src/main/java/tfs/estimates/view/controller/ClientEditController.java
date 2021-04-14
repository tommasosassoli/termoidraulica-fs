package tfs.estimates.view.controller;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.service.AutoSaveService;
import tfs.estimates.service.LogService;
import tfs.estimates.view.ViewManager;

public class ClientEditController extends AbstractController {
	private String clientID;
	private Client client;
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

	public ClientEditController(ViewManager viewManager) {
		super(viewManager);
	}

	private Client getClient() {
		if (client != null)
			return client;
		else if (clientID != null)
			client = ClientsManagement.instance().getClient(clientID);
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
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for ClientEditController not valid");
		}

		if (getClient() != null) {
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

			AutoSaveService.setModified();
			this.getViewManager().activate(ViewResolver.HOME);
		}
	}

	@FXML
	private void delete() {
		if (ViewManager.launchConfirmDialog("Sicuro di voler ELIMINARE questo cliente?")) {
			ClientsManagement.instance().deleteClient(clientID);

			AutoSaveService.setModified();
			this.getViewManager().activate(ViewResolver.HOME);
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.CLIENT_LIST);
	}

}
