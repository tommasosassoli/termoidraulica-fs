package tfs.estimates.view.controller;

import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tfs.estimates.view.ViewManager;

public class ClientListController extends AbstractController {
	@FXML
	private TextField searchBar;
	@FXML
	private ImageView cancelButton;
	@FXML
	private TableView<Client> clientsTable = new TableView<>();
	@FXML
	private TableColumn<Client, String> idColumn = new TableColumn<>();
	@FXML
	private TableColumn<Client, String> nameColumn = new TableColumn<>();

	private ObservableList<Client> masterList;

	public ClientListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("surnameName"));

		// set double click
		clientsTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				openClient();
		});

	}

	@Override
	public void refresh() {
		masterList = getClients();
		clientsTable.setItems(masterList);
	}

	private ObservableList<Client> getClients() {
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

			clientsTable.setItems(filterList);
			cancelButton.setVisible(true);
		}
	}

	@FXML
	private void unfilterList() {
		clientsTable.setItems(masterList);
		searchBar.clear();
		cancelButton.setVisible(false);
	}

	private void openClient() {
		Client c = clientsTable.getSelectionModel().getSelectedItem();
		this.getViewManager().activate(ViewResolver.CLIENT_EDIT, c.getId());
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
}
