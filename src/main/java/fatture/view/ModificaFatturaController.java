package fatture.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import fatture.management.GestioneClienti;
import fatture.management.GestioneFatture;
import fatture.model.AliquotaIva;
import fatture.model.Cliente;
import fatture.model.Fattura;
import fatture.model.Materiale;
import fatture.print.FattureFactory;
import fatture.service.AutoSaveService;
import fatture.util.FileNameResolver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;

/**
 * Questa view è trattata come una fattura, ma è da considerarsi una view
 * preventivo
 */
public class ModificaFatturaController extends InserimentoFatturaController {
	private static String IDfattura;
	private Fattura fattura;
	private boolean modified = false;
	@FXML
	private TextField id;
	@FXML
	private TableView<Materiale> tableMateriali;
	@FXML
	private TableColumn<Materiale, String> colDescrizione = new TableColumn<Materiale, String>("descrizione");
	@FXML
	private TableColumn<Materiale, String> colUM = new TableColumn<Materiale, String>("um");
	@FXML
	private TableColumn<Materiale, Double> colQT = new TableColumn<Materiale, Double>("qt");
	@FXML
	private TableColumn<Materiale, Double> colScontoMaggiorazione = new TableColumn<Materiale, Double>(
			"scontoMaggiorazione");
	@FXML
	private TableColumn<Materiale, AliquotaIva> colIVA = new TableColumn<Materiale, AliquotaIva>("aliquotaIva");
	@FXML
	private TableColumn<Materiale, Double> colPrezzoSingolo = new TableColumn<Materiale, Double>("prezzo");
	@FXML
	private TableColumn<Materiale, Double> colPrezzoCalcolato = new TableColumn<Materiale, Double>("subtotale");
	@FXML
	private Button salvaButton = new Button();
	@FXML
	private TextField acconto;
	@FXML
	private Button accontoButton = new Button();
	@FXML
	private TextField valueScontoMaggiorazione;
	@FXML
	private RadioButton scontoCheckBox;
	@FXML
	private RadioButton maggiorazioneCheckBox;
	@FXML
	private Label subTotale;
	@FXML
	private Label totaleImposte;
	@FXML
	private Label totaleImponibile;
	@FXML
	private Label totaleFattura;

	NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();

	public static String getIDfattura() {
		return IDfattura;
	}

	public static void setIDfattura(String iDfattura) {
		IDfattura = iDfattura;
	}

	public Fattura getFattura() {
		if (fattura != null) {
			return fattura;
		} else if (IDfattura != null) {
			fattura = GestioneFatture.instance().getFattura(IDfattura);
		}
		return fattura;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		if (this.modified)
			salvaButton.setDisable(false);
		else
			salvaButton.setDisable(true);

		AutoSaveService.setModified();
	}

	@Override
	public void initialize() {
		initializeFattura();
		initializeClientiBar();
		initializeMateriali();
		initializeBottomFattura();

		Platform.runLater(new Runnable() { // metto a fuoco la tabella
			@Override
			public void run() {
				tableMateriali.requestFocus();
			}
		});
	}

	private void initializeFattura() {
		if (getFattura() != null) {

			id.setText(fattura.getId());
			clientiBar.setValue(fattura.getCliente());
			dataFattura.setValue(fattura.getDataScadenza().toLocalDate());

		} else
			MainViewController.instance().setView("Principale");
	}

	private void initializeClientiBar() {
		ObservableList<Cliente> lc = FXCollections.observableArrayList();
		lc.addAll(GestioneClienti.instance().getClienti());
		clientiMasterList = lc;
		clientiBar.setItems(lc);
	}

	private void initializeMateriali() {
		colDescrizione.setCellFactory(tc -> {
			TableCell<Materiale, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(colDescrizione.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			text.setStyle("-fx-font: 15 system;");
			cell.setPadding(new Insets(10, 5, 10, 5));
			return cell;
		});
		colDescrizione.setCellValueFactory(new PropertyValueFactory<Materiale, String>("descrizione"));
		colUM.setCellValueFactory(new PropertyValueFactory<Materiale, String>("um"));
		colQT.setCellValueFactory(new PropertyValueFactory<Materiale, Double>("qt"));
		colScontoMaggiorazione.setCellValueFactory(new PropertyValueFactory<Materiale, Double>("scontoMaggiorazione"));
		colIVA.setCellValueFactory(new PropertyValueFactory<Materiale, AliquotaIva>("aliquotaIva"));
		colPrezzoSingolo.setCellValueFactory(new PropertyValueFactory<Materiale, Double>("prezzo"));
		colPrezzoCalcolato.setCellValueFactory(new PropertyValueFactory<Materiale, Double>("subtotale"));

		Collection<Materiale> collection = getFattura().getMateriali();
		tableMateriali.getItems().clear();

		if (collection != null) {
			ObservableList<Materiale> list = FXCollections.observableArrayList();
			list.addAll(collection);
			tableMateriali.setItems(list);
		}

		// IMPOSTO GLI EVENTI
		tableMateriali.setRowFactory(tv -> {
			TableRow<Materiale> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Materiale rowData = row.getItem();
					Materiale res = ModificaMaterialeController.modificaMateriale(rowData);
					if (res != null) {
						row.setItem(res);
						setModified();
						tableMateriali.refresh();
						this.calcTotFattura();
					}
				}
			});

			return row;
		});

		tableMateriali.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				this.openSelectedMateriale();
			} else if (event.getCode().equals(KeyCode.ADD) || event.getCode().equals(KeyCode.PLUS)) {
				this.addMaterialeToTable();
			} else if (event.getCode().equals(KeyCode.DELETE)) {
				this.removeMaterialeFromTable();

			} else if (event.getCode().equals(KeyCode.UP) && event.isControlDown()) { // to swap elements
				this.swapMateriale(true);
				event.consume();
			} else if (event.getCode().equals(KeyCode.DOWN) && event.isControlDown()) {
				this.swapMateriale(false);
				event.consume();

			} else {
				System.out.println("pressed: " + event.getCode().toString());
			}
			this.calcTotFattura();
		});
	}

	private void initializeBottomFattura() {
		acconto.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		acconto.setOnMouseClicked(mc -> {
			acconto.selectAll();
		});
		acconto.setOnAction(ac -> {
			onAccontoButtonAction();
		});
		acconto.setText(String.valueOf(getFattura().getAcconto()));

		this.calcTotFattura();
	}

	@FXML
	private void viewKeyEvent(KeyEvent event) {
		if (event.isControlDown()) {
			if (event.getCode().equals(KeyCode.S)) {
				if (isModified())
					this.updateFattura();
				else
					this.goRicercaFattura();
			} else if (event.getCode().equals(KeyCode.Z)) {
				this.goRicercaFattura();
			}
		}
	}

	private void swapMateriale(boolean up) {
		int draggedIndex = tableMateriali.getSelectionModel().getSelectedIndex();
		Materiale draggedMateriale = tableMateriali.getItems().remove(draggedIndex);

		int dropIndex;
		if (up)
			dropIndex = draggedIndex - 1;
		else
			dropIndex = draggedIndex + 1;

		if (dropIndex < 0)
			dropIndex = 0;
		else if (dropIndex > tableMateriali.getItems().size())
			dropIndex = tableMateriali.getItems().size();

		tableMateriali.getItems().add(dropIndex, draggedMateriale);
		tableMateriali.getSelectionModel().select(dropIndex);
		tableMateriali.scrollTo(dropIndex);
	}

	private void calcTotFattura() {
		save();

		double subTot = fattura.getSubTotaleFattura();
		double imponibile = fattura.getTotaleImponibile();
		double imposte = fattura.getTotaleImposta();
		double totale = fattura.getTotaleFattura();

		subTotale.setText(numberFormat.format(subTot));
		totaleImposte.setText(numberFormat.format(imposte));
		totaleImponibile.setText(numberFormat.format(imponibile));
		totaleFattura.setText(numberFormat.format(totale));
	}

	private boolean openSelectedMateriale() {
		Materiale m = tableMateriali.getSelectionModel().getSelectedItem();
		int index = tableMateriali.getSelectionModel().getSelectedIndex();
		Materiale res = ModificaMaterialeController.modificaMateriale(m);
		if (res != null) {
			tableMateriali.getItems().set(index, res);
			setModified();
			tableMateriali.refresh();
			this.calcTotFattura();
			return true;
		}
		return false;
	}

	@FXML
	private void addMaterialeToTable() {
		Materiale newMateriale = new Materiale();
		tableMateriali.getItems().add(newMateriale);
		tableMateriali.getSelectionModel().selectLast();
		if (!this.openSelectedMateriale()) { // se è vuoto
			tableMateriali.getItems().remove(newMateriale);
		}
	}

	@FXML
	private void removeMaterialeFromTable() {
		if (!tableMateriali.getSelectionModel().isEmpty()
				&& MainViewController.launchConfirmDialog("Rimuovere la riga selezionata?"))
			tableMateriali.getItems().remove(tableMateriali.getSelectionModel().getSelectedItem());
		this.calcTotFattura();
	}

	@FXML
	private void onAccontoButtonAction() {
		if (!acconto.isDisable()) {
			double val = Double.parseDouble(acconto.getText());
			double sub = getFattura().getSubTotaleFattura();

			if (val <= sub) {
				getFattura().setAcconto(val);

				acconto.setDisable(true);
				accontoButton.setText("modifica");

			} else {
				MainViewController.launchInfoDialog("Valore acconto non valido!");
				getFattura().setAcconto(sub);
				acconto.setText(String.valueOf(sub));
			}
			this.calcTotFattura();

		} else {
			acconto.setDisable(false);
			accontoButton.setText("ok");
			acconto.requestFocus();
		}
	}

	@FXML
	private void delFattura() {
		if (MainViewController.launchConfirmDialog("Sei sicuro di voler ELIMINARE questa fattura?")) {
			GestioneFatture.instance().delFattura(getIDfattura());
			MainViewController.instance().setView("RicercaFattura");
		}
	}

	@FXML
	private boolean updateFattura() {
		LocalDateTime date = LocalDateTime.of(dataFattura.getValue(), LocalTime.MIDNIGHT);

		if (date.isBefore(LocalDateTime.now())) {
			if (MainViewController
					.launchConfirmDialog("La data di riferimento è impostata prima di oggi, continuare?")) {
				save();
				return true;
			}
			return false;
		} else {
			save();
			return true;
		}
	}

	private void save() {
		LocalDateTime date = LocalDateTime.of(dataFattura.getValue(), LocalTime.MIDNIGHT);

		GestioneFatture.instance().updateFattura(IDfattura, clientiBar.getValue().getId(), date);
		GestioneFatture.instance().updateMateriali(IDfattura, tableMateriali.getItems());

		setModified(false);
	}

	@FXML
	private void printFattura() {//TODO aggiungi una rotella di caricamento ;-)
		if (!tableMateriali.getItems().isEmpty()) {
			if (updateFattura())
				FattureFactory.printReportFattura(IDfattura);
		} else
			MainViewController.launchInfoDialog("Non sono cotenuti elementi nella tabella");
	}

	@FXML
	private void printFatturaB() {
		if (!tableMateriali.getItems().isEmpty()) {
			if (updateFattura())
				FattureFactory.printReportFattura(IDfattura, FileNameResolver.REPORT_FATTURA_B);
		} else
			MainViewController.launchInfoDialog("Non sono cotenuti elementi nella tabella");
	}

	@FXML
	private void setModified() {
		setModified(true);
	}

	@FXML
	private void goRicercaFattura() {
		if (isModified()) {
			if (MainViewController.launchConfirmDialog("Uscire? Eventuali modifiche non verranno salvate."))
				MainViewController.instance().setView("RicercaFattura");
		} else
			MainViewController.instance().setView("RicercaFattura");
	}

}
