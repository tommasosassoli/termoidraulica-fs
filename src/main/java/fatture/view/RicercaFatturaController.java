package fatture.view;

import java.time.LocalDateTime;

import fatture.management.GestioneFatture;
import fatture.model.Cliente;
import fatture.model.Fattura;
import fatture.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class RicercaFatturaController {
	private ObservableList<Fattura> masterListFatture;
	@FXML
	private TableView<Fattura> tableFatture = new TableView<Fattura>();
	@FXML
	private TableColumn<Fattura, String> colIDFatture = new TableColumn<Fattura, String>("id");
	@FXML
	private TableColumn<Fattura, Cliente> colClienti = new TableColumn<Fattura, Cliente>("cliente");
	@FXML
	private TableColumn<Fattura, LocalDateTime> colDataInserimento = new TableColumn<Fattura, LocalDateTime>(
			"datainserimento");
	@FXML
	private TableColumn<Fattura, LocalDateTime> colDataScadenza = new TableColumn<Fattura, LocalDateTime>(
			"datascadenza");
	@FXML
	private TextField searchBar;
	@FXML
	private ComboBox<String> searchField;
	@FXML
	private ImageView cancelFilter;

	public void initialize() {
		colIDFatture.setCellValueFactory(new PropertyValueFactory<Fattura, String>("id"));
		colClienti.setCellValueFactory(new PropertyValueFactory<Fattura, Cliente>("cliente"));
		colDataInserimento
				.setCellValueFactory(new PropertyValueFactory<Fattura, LocalDateTime>("formatDataInserimento"));
		colDataScadenza.setCellValueFactory(new PropertyValueFactory<Fattura, LocalDateTime>("formatDataScadenza"));

		tableFatture.setItems(getFatture());

		tableFatture.setOnMouseClicked(new EventHandler<MouseEvent>() { // imposto il double click
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1)
					openFattura();
			}
		});

		searchField.setItems(FXCollections.observableArrayList("ID", "Clienti")); // inizializzo la searchFiled
		searchField.setValue("Clienti");
	}

	private ObservableList<Fattura> getFatture() {
		ObservableList<Fattura> list = FXCollections.observableArrayList();
		list.addAll(GestioneFatture.instance().getFatture());
		masterListFatture = list;
		return list;
	}

	private void openFattura() {
		ModificaFatturaController.setIDfattura(tableFatture.getSelectionModel().getSelectedItem().getId());
		MainViewController.instance().setView("ModificaFattura");
	}

	@FXML
	private void filterList() {
		ObservableList<Fattura> list = FXCollections.observableArrayList();

		switch (searchField.getValue()) {
		case "ID": {
			for (Fattura f : masterListFatture) {
				if (StringComparator.contains(f.getId(), " ", searchBar.getText(), " ")) // confronto ID
					list.add(f);
			}
		}
		case "Clienti": {
			for (Fattura f : masterListFatture) {
				if (StringComparator.containsInsensitive(f.getCliente().toString(), " ", searchBar.getText(), " ")) // confronto
																													// nomi
																													// clienti
					list.add(f);
			}
		}
		}

		tableFatture.setItems(list);
		cancelFilter.setVisible(true);
	}

	@FXML
	private void unFilterList() {
		tableFatture.setItems(masterListFatture);
		searchBar.setText("");
		cancelFilter.setVisible(false);
	}

	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
	}

}
