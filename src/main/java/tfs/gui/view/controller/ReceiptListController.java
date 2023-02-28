package tfs.gui.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import tfs.business.endpoint.ReceiptEndPoint;
import tfs.business.model.receipt.Receipt;
import tfs.business.model.tax.TaxRate;

import tfs.gui.resolvers.ViewResolver;
import tfs.gui.util.StringComparator;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;
import tfs.gui.view.tasks.OpenViewTask;
import tfs.service.LogService;

public class ReceiptListController extends AbstractController {
	private ReceiptEndPoint endPoint = new ReceiptEndPoint();

	@FXML
	private AnchorPane progressIndicatorLayer;
	@FXML
	private TextField searchBar;
	@FXML
	private ImageView cancelFilter;
	@FXML
	private ComboBox<String> boxSelector;
	@FXML
	private TableView<Receipt> receiptsTable = new TableView<>();
	@FXML
	private TableColumn<Receipt, String> foreignIdCol = new TableColumn<>();
	@FXML
	private TableColumn<Receipt, String> descriptionCol = new TableColumn<>();
	@FXML
	private TableColumn<Receipt, String> dateCol = new TableColumn<>();


	private ObservableList<Receipt> masterList;

	public ReceiptListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		foreignIdCol.setCellValueFactory(new PropertyValueFactory<>("foreignId"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("formatDate"));

		// set double click
		receiptsTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				openReceipt();
		});

		// receipt selector
		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("In scadenza");
		items.add("Tutti");
		boxSelector.setItems(items);
		boxSelector.getSelectionModel().selectFirst();
	}

	@Override
	public void refresh() {
		String sel = boxSelector.getSelectionModel().getSelectedItem();

		if (sel.equals("Tutti"))
			masterList = getReceipts();
		else if (sel.equals("In scadenza"))
			masterList = getExpiringReceipts();
		else
			LogService.warning(ReceiptListController.class, "Error during box selection", true);

		receiptsTable.setItems(masterList);
	}

	private ObservableList<Receipt> getReceipts() {
		ObservableList<Receipt> list = FXCollections.observableArrayList();
		list.addAll(endPoint.getReceiptList());

		return list;
	}

	private ObservableList<Receipt> getExpiringReceipts() {
		ObservableList<Receipt> list = FXCollections.observableArrayList();
		list.addAll(endPoint.getExpiringReceipt());

		return list;
	}

	@FXML
	private void applyFilter() {
		String name = searchBar.getText();

		if (!name.isEmpty()) {
			ObservableList<Receipt> filterList = FXCollections.observableArrayList();
			for (Receipt c : masterList) {
				if (StringComparator.containsInsensitive(c.getDescription(), " ", name, " "))	//controllo se le parole matchano
					filterList.add(c);
			}

			receiptsTable.setItems(filterList);
			cancelFilter.setVisible(true);
		}
	}

	@FXML
	private void removeFilter() {
		receiptsTable.setItems(masterList);
		searchBar.clear();
		cancelFilter.setVisible(false);
	}

	private void openReceipt() {
		progressIndicatorLayer.setVisible(true);
		String id = receiptsTable.getSelectionModel().getSelectedItem().getId();

		OpenViewTask task = new OpenViewTask(getViewManager(), ViewResolver.RECEIPT_EDIT, id);
		task.setOnSucceeded(e -> progressIndicatorLayer.setVisible(false));
		task.setOnFailed(e -> progressIndicatorLayer.setVisible(false));

		new Thread(task).start();
	}

	@FXML
	private void boxSelectorChange() {
		this.refresh();
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
}
