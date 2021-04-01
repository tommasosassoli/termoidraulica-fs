package tfs.estimates.view;

import tfs.estimates.management.logic.TaxRateManagement;
import tfs.estimates.model.TaxRate;
import tfs.estimates.model.Item;
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
	private static Item item;
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
	private ComboBox<TaxRate> iva;

	public static Item getMateriale() {
		return item;
	}

	public static void setMateriale(Item item) {
		ModificaMaterialeController.item = item;
	}

	public static Item modificaMateriale(Item item) {
		setMateriale(item);
		MainViewController.launchNewWindow("ModificaMateriale", "Modifica materiale", true, false);

		if (modified)
			return getMateriale();
		else
			return null;
	}

	public void initialize() {
		if (item != null) {

			initializeFormatter();
			initializeEvent();

			descrizione.setText(item.getDescription());
			um.setText(item.getUm());
			prezzoUnitario.setText(String.valueOf(item.getPrice()));
			prezzo.setText(String.valueOf(item.getPrice() * item.getQt()));

			if (item.getDiscount() <= 0)
				scontoCheckBox.setSelected(true);
			else
				maggiorazioneCheckBox.setSelected(true);
			valoreScontoMaggiorazione.setText(String.valueOf(Math.abs(item.getDiscount())));

			quantita.setText(String.valueOf(item.getQt()));
			iva.setItems(FXCollections.observableArrayList(TaxRateManagement.instance().getTaxRates()));
			iva.setValue(item.getTaxRate());

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
		item.setDescription(descrizione.getText());
		item.setUm(um.getText());
		item.setPrice(Double.parseDouble(prezzoUnitario.getText()));

		Double scontoMagg = Double.parseDouble(valoreScontoMaggiorazione.getText());
		if (scontoCheckBox.isSelected() && scontoMagg != 0)
			scontoMagg = -scontoMagg;
		item.setDiscount(scontoMagg);

		item.setQt(Double.parseDouble(quantita.getText()));
		item.setTaxRate(iva.getValue());

		modified = true;
		close();
	}

	private void close() {
		((Stage) descrizione.getScene().getWindow()).close(); // recupero la scena e la chiudo
	}

}
