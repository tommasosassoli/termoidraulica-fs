package tfs.gui.view.controller;

import tfs.business.endpoint.CustomerEndPoint;
import tfs.business.model.customer.Customer;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class CustomerListController extends AbstractController {
	private CustomerEndPoint endPoint = new CustomerEndPoint();
	@FXML
	private TextField searchBar;
	@FXML
	private ImageView cancelButton;
	@FXML
	private TableView<Customer> clientsTable = new TableView<>();
	@FXML
	private TableColumn<Customer, String> idColumn = new TableColumn<>();
	@FXML
	private TableColumn<Customer, String> nameColumn = new TableColumn<>();

	private ObservableList<Customer> masterList;

	public CustomerListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("surnameName"));

		// set double click
		clientsTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				openCustomer();
		});

	}

	@Override
	public void refresh() {
		masterList = getCustomers();
		clientsTable.setItems(masterList);
	}

	private ObservableList<Customer> getCustomers() {
		ObservableList<Customer> list = FXCollections.observableArrayList();
		list.addAll(endPoint.getCustomerList());

		return list;
	}

	@FXML
	private void filterList() {
		String name = searchBar.getText();

		if (!name.isEmpty()) {
			ObservableList<Customer> filterList = FXCollections.observableArrayList();
			for (Customer c : masterList) {
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

	private void openCustomer() {
		Customer c = clientsTable.getSelectionModel().getSelectedItem();
		this.getViewManager().activate(ViewResolver.CLIENT_EDIT, c.getId());
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
}
