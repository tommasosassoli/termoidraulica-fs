package tfs.gui.view.controller;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

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
	@FXML
	private ComboBox<String> dateFilterBox;

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

		// date filter
		List<String> years = new ArrayList<>();
		for (int i = Year.now().getValue() ; i >= 2019; i--)
			years.add(String.valueOf(i));
		years.add("Tutti");

		dateFilterBox.setItems(FXCollections.observableArrayList(years));
		dateFilterBox.getSelectionModel().selectFirst();
	}

	@Override
	public void refresh() {
		String item = dateFilterBox.getSelectionModel().getSelectedItem();
		if (item.equals("Tutti"))
			estimateTable.setItems(getEstimates());
		else
			estimateTable.setItems(getEstimates(Integer.parseInt(item)));
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
	private void changeDateFilter() {
		this.refresh();
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

	private ObservableList<Estimate> getEstimates(int year) {
		estimateMasterList = FXCollections.observableArrayList();
		List<Estimate> list = estimateEndPoint.getEstimateList();
		for (Estimate e : list) {
			int y = e.getExpirationDate().getYear();
			if (y == year)
				estimateMasterList.add(e);
		}
		return estimateMasterList;
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
