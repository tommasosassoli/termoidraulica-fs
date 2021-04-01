package tfs.estimates.view;

import java.time.LocalDateTime;

import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.model.Client;
import tfs.estimates.model.Estimate;
import tfs.estimates.util.StringComparator;
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
	private ObservableList<Estimate> masterListFatture;
	@FXML
	private TableView<Estimate> tableFatture = new TableView<Estimate>();
	@FXML
	private TableColumn<Estimate, String> colIDFatture = new TableColumn<Estimate, String>("id");
	@FXML
	private TableColumn<Estimate, Client> colClienti = new TableColumn<Estimate, Client>("cliente");
	@FXML
	private TableColumn<Estimate, LocalDateTime> colDataInserimento = new TableColumn<Estimate, LocalDateTime>(
			"datainserimento");
	@FXML
	private TableColumn<Estimate, LocalDateTime> colDataScadenza = new TableColumn<Estimate, LocalDateTime>(
			"datascadenza");
	@FXML
	private TextField searchBar;
	@FXML
	private ComboBox<String> searchField;
	@FXML
	private ImageView cancelFilter;

	public void initialize() {
		colIDFatture.setCellValueFactory(new PropertyValueFactory<Estimate, String>("id"));
		colClienti.setCellValueFactory(new PropertyValueFactory<Estimate, Client>("cliente"));
		colDataInserimento
				.setCellValueFactory(new PropertyValueFactory<Estimate, LocalDateTime>("formatDataInserimento"));
		colDataScadenza.setCellValueFactory(new PropertyValueFactory<Estimate, LocalDateTime>("formatDataScadenza"));

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

	private ObservableList<Estimate> getFatture() {
		ObservableList<Estimate> list = FXCollections.observableArrayList();
		list.addAll(EstimatesManagement.instance().getEstimates());
		masterListFatture = list;
		return list;
	}

	private void openFattura() {
		ModificaFatturaController.setIDfattura(tableFatture.getSelectionModel().getSelectedItem().getId());
		MainViewController.instance().setView("ModificaFattura");
	}

	@FXML
	private void filterList() {
		ObservableList<Estimate> list = FXCollections.observableArrayList();

		switch (searchField.getValue()) {
		case "ID": {
			for (Estimate f : masterListFatture) {
				if (StringComparator.contains(f.getId(), " ", searchBar.getText(), " ")) // confronto ID
					list.add(f);
			}
		}
		case "Clienti": {
			for (Estimate f : masterListFatture) {
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
