package fatture.view;

import fatture.management.GestioneClienti;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class InserimentoClienteController {

	@FXML
	protected TextField nome;
	@FXML
	private ImageView nomeRequired;
	@FXML
	protected TextField cognome;
	@FXML
	private ImageView cognomeRequired;
	@FXML
	protected TextField denominazione;
	@FXML
	private ImageView denominazioneRequired;
	@FXML
	protected TextField residenza;
	@FXML
	protected TextField comune;
	@FXML
	protected TextField provincia;
	@FXML
	protected TextField cap;
	@FXML
	protected TextField codiceFiscale;
	@FXML
	protected TextArea note;
	@FXML
	protected RadioButton soggFisicoCheckBox;
	@FXML
	protected RadioButton soggGiuridicoCheckBox;

	public void initialize() {
		initializeTextProperty();
	}

	private void initializeTextProperty() {

		nome.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = nome.getText();
				String l1 = txt.substring(0, 1).toUpperCase();
				nome.setText(l1 + txt.substring(1).toLowerCase());
			}
		});

		cognome.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = cognome.getText();
				String l1 = txt.substring(0, 1).toUpperCase();
				cognome.setText(l1 + txt.substring(1).toLowerCase());
			}
		});

		denominazione.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = denominazione.getText();
				String l1 = txt.substring(0, 1).toUpperCase();
				denominazione.setText(l1 + txt.substring(1).toLowerCase());
			}
		});

		comune.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal)
				comune.setText(comune.getText().toUpperCase());
		});

		provincia.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = provincia.getText().toUpperCase();
				if (txt.length() == 2 && txt.matches("[a-zA-Z]+"))
					provincia.setText(txt);
				else
					provincia.setText("");
			}
		});

		codiceFiscale.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String txt = codiceFiscale.getText();
				if ((txt.length() == 16 || txt.length() == 11) && txt.matches("[a-zA-Z0-9]+"))
					codiceFiscale.setText(txt.toUpperCase());
				else
					codiceFiscale.setText("");
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
	private void enableSoggettoFisico() {
		nome.setDisable(false);
		cognome.setDisable(false);
		denominazione.setDisable(true);
	}

	@FXML
	private void enableSoggettoGiuridico() {
		nome.setDisable(true);
		cognome.setDisable(true);
		denominazione.setDisable(false);
	}

	@FXML
	private void clear() {
		nome.clear();
		cognome.clear();
		denominazione.clear();
		soggFisicoCheckBox.setSelected(true);
		residenza.clear();
		comune.clear();
		provincia.clear();
		cap.clear();
		codiceFiscale.clear();
		note.clear();
	}

	@FXML
	private void insert() {
		if (validateInput()) {
			boolean result;
			
			if (soggFisicoCheckBox.isSelected()) {
				result = GestioneClienti.instance().addCliente(nome.getText(), cognome.getText(),
						residenza.getText(), comune.getText(), provincia.getText(), cap.getText(),
						codiceFiscale.getText(), note.getText());

			} else {
				result = GestioneClienti.instance().addCliente(denominazione.getText(),
						residenza.getText(), comune.getText(), provincia.getText(), cap.getText(),
						codiceFiscale.getText(), note.getText());

			}
			if (result)
				MainViewController.instance().setView("Principale");
			else
				MainViewController.launchInfoDialog("Non è stato possibile inserire il cliente");
		}
	}

	protected boolean validateInput() {
		if (soggFisicoCheckBox.isSelected()) {
			if (nome.getText().isEmpty()) {
				nomeRequired.setVisible(true);
				return false;
			} else if (cognome.getText().isEmpty()) {
				cognomeRequired.setVisible(true);
				return MainViewController.launchConfirmDialog(""
						+ "La casella del cognome e' vuota.\nVuoi inserire ugualmente il cliente?");
			} else {
				nomeRequired.setVisible(false);
				cognomeRequired.setVisible(false);
				return true;
			}
		} else if (denominazione.getText().isEmpty()) {
			denominazioneRequired.setVisible(true);
			return false;
		} else {
			denominazioneRequired.setVisible(false);
			return true;
		}
	}

	@FXML
	private void backButton() {
		MainViewController.instance().setView("Principale");
	}
}
