package fatture.view;

import fatture.management.GestioneClienti;
import fatture.model.Cliente;
import fatture.util.StringComparator;
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
	private TableView<Cliente> tableClienti = new TableView<Cliente>();
	@FXML
	private TableColumn<Cliente, String> columnID = new TableColumn<Cliente, String>("id");
	@FXML
	private TableColumn<Cliente, String> columnNome = new TableColumn<Cliente, String>("nome");

	private ObservableList<Cliente> masterList;

	public void initialize() {
		columnID.setCellValueFactory(new PropertyValueFactory<Cliente, String>("id"));
		columnNome.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nomeCognome"));

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

	private ObservableList<Cliente> getClienti() {
		ObservableList<Cliente> list = FXCollections.observableArrayList();
		list.addAll(GestioneClienti.instance().getClienti());

		return list;
	}

	@FXML
	private void filterList() {
		String name = searchBar.getText();

		if (!name.isEmpty()) {
			ObservableList<Cliente> filterList = FXCollections.observableArrayList();
			for (Cliente c : masterList) {
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
		Cliente c = tableClienti.getSelectionModel().getSelectedItem();
		ModificaClienteController.setIDCliente(c.getId());		
		MainViewController.instance().setView("ModificaCliente");
	}

	@FXML
	private void backButton() {
		MainViewController.instance().setView("Principale");
	}
}
