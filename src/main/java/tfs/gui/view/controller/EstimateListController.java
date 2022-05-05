package tfs.gui.view.controller;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import tfs.business.endpoint.EstimateEndPoint;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;
import tfs.gui.view.tasks.OpenViewTask;

public class EstimateListController extends AbstractController {
	private EstimateEndPoint estimateEndPoint = new EstimateEndPoint();
	private ObservableList<Estimate> estimateMasterList;

	@FXML
	private AnchorPane progressIndicatorLayer;
	@FXML
	private TableView<Estimate> estimateTable = new TableView<>();
	@FXML
	private TableColumn<Estimate, String> estimateIdCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, Customer> clientsCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, LocalDateTime> insertDateCol = new TableColumn<>();
	@FXML
	private TableColumn<Estimate, LocalDateTime> expirationDateCol = new TableColumn<>();
	@FXML
	private TextField searchBar;
	@FXML
	private ComboBox<String> searchField;
	@FXML
	private ImageView cancelFilter;

	public EstimateListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		estimateIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		clientsCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
		insertDateCol.setCellValueFactory(new PropertyValueFactory<>("formatInsertDate"));
		expirationDateCol.setCellValueFactory(new PropertyValueFactory<>("formatExpiringDate"));

		// set double click
		estimateTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				estimateOpen();
		});

		searchField.setItems(FXCollections.observableArrayList("ID", "Customers")); // init searchField
		searchField.setValue("Customers");
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
		case "Customers": {
			for (Estimate f : estimateMasterList) {
				if (StringComparator.containsInsensitive(f.getCustomer().toString(), " ", searchBar.getText(), " "))
					list.add(f);
			}
		}
		}

		estimateTable.setItems(list);
		cancelFilter.setVisible(true);
	}

	@FXML
	private void removeFilter() {
		estimateTable.setItems(estimateMasterList);
		searchBar.setText("");
		cancelFilter.setVisible(false);
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	private ObservableList<Estimate> getEstimates() {
		ObservableList<Estimate> list = FXCollections.observableArrayList();
		list.addAll(estimateEndPoint.getEstimateList());
		estimateMasterList = list;
		return list;
	}

	private void estimateOpen() {
		progressIndicatorLayer.setVisible(true);
		String id = estimateTable.getSelectionModel().getSelectedItem().getId();

		OpenViewTask task = new OpenViewTask(getViewManager(), ViewResolver.ESTIMATE_EDIT, id);
		task.setOnSucceeded(e -> progressIndicatorLayer.setVisible(false));
		task.setOnFailed(e -> progressIndicatorLayer.setVisible(false));

		new Thread(task).start();
	}
}
