package tfs.estimates.view;

import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import tfs.estimates.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class RicercaClientiController {
	@FXML
	private TextField searchBar;
	@FXML
	private ImageView cancelButton;
	@FXML
	private TableView<Client> tableClienti = new TableView<Client>();
	@FXML
	private TableColumn<Client, String> columnID = new TableColumn<Client, String>("id");
	@FXML
	private TableColumn<Client, String> columnNome = new TableColumn<Client, String>("nome");

	private ObservableList<Client> masterList;

	public void initialize() {
		columnID.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
		columnNome.setCellValueFactory(new PropertyValueFactory<Client, String>("nomeCognome"));

		masterList = getClienti();

		tableClienti.setItems(masterList);

		tableClienti.setOnMouseClicked(new EventHandler<MouseEvent>() { // imposto il double click
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1)
					openCliente();
			}
		});

	}

	private ObservableList<Client> getClienti() {
		ObservableList<Client> list = FXCollections.observableArrayList();
		list.addAll(ClientsManagement.instance().getClients());

		return list;
	}

	@FXML
	private void filterList() {
		String name = searchBar.getText();

		if (!name.isEmpty()) {
			ObservableList<Client> filterList = FXCollections.observableArrayList();
			for (Client c : masterList) {
				if (StringComparator.containsInsensitive(c.toString(), " ", name, " "))	//controllo se le parole matchano
					filterList.add(c);
			}

			tableClienti.setItems(filterList);
			cancelButton.setVisible(true);
		}
	}

	@FXML
	private void unfilterList() {
		tableClienti.setItems(masterList);
		searchBar.clear();
		cancelButton.setVisible(false);
	}

	@FXML
	private void openCliente() {
		Client c = tableClienti.getSelectionModel().getSelectedItem();
		ModificaClienteController.setIDCliente(c.getId());		
		MainViewController.instance().setView("ModificaCliente");
	}

	@FXML
	private void backButton() {
		MainViewController.instance().setView("Principale");
	}
}
