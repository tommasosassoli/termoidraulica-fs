package tfs.estimates.view.controller;

import java.time.LocalDateTime;

import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.model.Client;
import tfs.estimates.model.Estimate;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tfs.estimates.view.ViewManager;

public class EstimateListController extends AbstractController {
	private ObservableList<Estimate> estimateMasterList;
	@FXML
	private TableView<Estimate> estimateTable = new TableView<>();
	@FXML
	private TableColumn<Estimate, String> estimateIdCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, Client> clientsCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, LocalDateTime> insertDateCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, LocalDateTime> expirationDateCol = new TableColumn<>();
	@FXML
	private TextField searchBar;
	@FXML
	private ComboBox<String> searchField;
	@FXML
	private ImageView removeFilter;

	public EstimateListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		estimateIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		clientsCol.setCellValueFactory(new PropertyValueFactory<>("client"));
		insertDateCol.setCellValueFactory(new PropertyValueFactory<>("formatInsertDate"));
		expirationDateCol.setCellValueFactory(new PropertyValueFactory<>("formatExpiringDate"));

		// set double click
		estimateTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				estimateOpen();
		});

		searchField.setItems(FXCollections.observableArrayList("ID", "Clients")); // init searchField
		searchField.setValue("Clients");
	}

	@Override
	public void refresh() {
		estimateTable.setItems(getEstimates());
	}

	@FXML
	private void applyFilter() {
		ObservableList<Estimate> list = FXCollections.observableArrayList();

		switch (searchField.getValue()) {
		case "ID": {
			for (Estimate f : estimateMasterList) {
				if (StringComparator.contains(f.getId(), " ", searchBar.getText(), " ")) // confronto ID
					list.add(f);
			}
		}
		case "Clients": {
			for (Estimate f : estimateMasterList) {
				if (StringComparator.containsInsensitive(f.getClient().toString(), " ", searchBar.getText(), " "))
					list.add(f);
			}
		}
		}

		estimateTable.setItems(list);
		removeFilter.setVisible(true);
	}

	@FXML
	private void removeFilter() {
		estimateTable.setItems(estimateMasterList);
		searchBar.setText("");
		removeFilter.setVisible(false);
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	private ObservableList<Estimate> getEstimates() {
		ObservableList<Estimate> list = FXCollections.observableArrayList();
		list.addAll(EstimatesManagement.instance().getEstimates());
		estimateMasterList = list;
		return list;
	}

	private void estimateOpen() {
		String id = estimateTable.getSelectionModel().getSelectedItem().getId();
		this.getViewManager().activate(ViewResolver.ESTIMATE_EDIT, id);
	}
}
