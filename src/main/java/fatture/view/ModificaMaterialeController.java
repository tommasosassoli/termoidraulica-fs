package fatture.view;

import fatture.management.GestioneAliquoteIva;
import fatture.model.AliquotaIva;
import fatture.model.Materiale;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class ModificaMaterialeController {
	private static Materiale materiale;
	private static boolean modified = false;

	@FXML
	private TextArea descrizione;
	@FXML
	private TextField um;
	@FXML
	private TextField prezzoUnitario;
	@FXML
	private TextField prezzo;
	@FXML
	private RadioButton scontoCheckBox;
	@FXML
	private ToggleGroup scontoMaggiorazione;
	@FXML
	private RadioButton maggiorazioneCheckBox;
	@FXML
	private TextField valoreScontoMaggiorazione;
	@FXML
	private TextField quantita;
	@FXML
	private ComboBox<AliquotaIva> iva;

	public static Materiale getMateriale() {
		return materiale;
	}

	public static void setMateriale(Materiale materiale) {
		ModificaMaterialeController.materiale = materiale;
	}

	public static Materiale modificaMateriale(Materiale materiale) {
		setMateriale(materiale);
		MainViewController.launchNewWindow("ModificaMateriale", "Modifica materiale", true, false);

		if (modified)
			return getMateriale();
		else
			return null;
	}

	public void initialize() {
		if (materiale != null) {

			initializeFormatter();
			initializeEvent();

			descrizione.setText(materiale.getDescrizione());
			um.setText(materiale.getUm());
			prezzoUnitario.setText(String.valueOf(materiale.getPrezzo()));
			prezzo.setText(String.valueOf(materiale.getPrezzo() * materiale.getQt()));

			if (materiale.getScontoMaggiorazione() <= 0)
				scontoCheckBox.setSelected(true);
			else
				maggiorazioneCheckBox.setSelected(true);
			valoreScontoMaggiorazione.setText(String.valueOf(Math.abs(materiale.getScontoMaggiorazione())));

			quantita.setText(String.valueOf(materiale.getQt()));
			iva.setItems(FXCollections.observableArrayList(GestioneAliquoteIva.instance().getAliquote()));
			iva.setValue(materiale.getAliquotaIva());

		} else
			close();
	}

	private void initializeFormatter() {
		descrizione.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.TAB) {
				this.um.requestFocus();
				event.consume();
			}
		});

		um.setOnMouseClicked(mc -> {
			um.selectAll();
		});

		prezzoUnitario.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		prezzoUnitario.setOnMouseClicked(mc -> {
			prezzoUnitario.selectAll();
		});

		prezzo.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		prezzo.setOnMouseClicked(mc -> {
			prezzo.selectAll();
		});

		valoreScontoMaggiorazione.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		valoreScontoMaggiorazione.setOnMouseClicked(mc -> {
			valoreScontoMaggiorazione.selectAll();
		});

		quantita.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		quantita.setOnMouseClicked(mc -> {
			quantita.selectAll();
		});

	}

	private void initializeEvent() {
		quantita.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantita.getText());
				double pUnit = Double.parseDouble(prezzoUnitario.getText());

				prezzo.setText(String.valueOf(qt * pUnit));
			}
		});

		prezzoUnitario.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantita.getText());
				double pUnit = Double.parseDouble(prezzoUnitario.getText());

				prezzo.setText(String.valueOf(qt * pUnit));
			}
		});

		prezzo.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantita.getText());
				double pTot = Double.parseDouble(prezzo.getText());

				prezzoUnitario.setText(String.valueOf(pTot / qt));
			}
		});
	}

	@FXML
	private void viewKeyEvent(KeyEvent event) {
		if (event.getCode().equals(KeyCode.TAB)) {
			this.um.requestFocus();
		}
	}

	@FXML
	private void cancelEdit() {
		close();
	}

	@FXML
	private void endEdit() {
		materiale.setDescrizione(descrizione.getText());
		materiale.setUm(um.getText());
		materiale.setPrezzo(Double.parseDouble(prezzoUnitario.getText()));

		Double scontoMagg = Double.parseDouble(valoreScontoMaggiorazione.getText());
		if (scontoCheckBox.isSelected() && scontoMagg != 0)
			scontoMagg = -scontoMagg;
		materiale.setScontoMaggiorazione(scontoMagg);

		materiale.setQt(Double.parseDouble(quantita.getText()));
		materiale.setAliquotaIva(iva.getValue());

		modified = true;
		close();
	}

	private void close() {
		((Stage) descrizione.getScene().getWindow()).close(); // recupero la scena e la chiudo
	}

}
