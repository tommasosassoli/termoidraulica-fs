package tfs.gui.view.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tfs.business.endpoint.ReceiptEndPoint;
import tfs.business.model.receipt.Receipt;
import tfs.business.model.tax.TaxRate;

import tfs.gui.resolvers.ViewResolver;
import tfs.gui.util.StringComparator;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class ReceiptListController extends AbstractController {
	private ReceiptEndPoint endPoint = new ReceiptEndPoint();
	@FXML
	private TextField searchBar;
	@FXML
	private ImageView cancelFilter;
	@FXML
	private TableView<Receipt> receiptsTable = new TableView<>();
	@FXML
	private TableColumn<Receipt, String> foreignIdCol = new TableColumn<>();
	@FXML
	private TableColumn<Receipt, String> descriptionCol = new TableColumn<>();
	@FXML
	private TableColumn<Receipt, String> dateCol = new TableColumn<>();
	@FXML
	private TableColumn<TaxRate, String> taxCol = new TableColumn<>();


	private ObservableList<Receipt> masterList;

	public ReceiptListController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		foreignIdCol.setCellValueFactory(new PropertyValueFactory<>("foreignId"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("formatDate"));
		taxCol.setCellValueFactory(new PropertyValueFactory<>("taxRate"));

		// set double click
		receiptsTable.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1)
				openReceipt();
		});

	}

	@Override
	public void refresh() {
		masterList = getReceipts();
		receiptsTable.setItems(masterList);
	}

	private ObservableList<Receipt> getReceipts() {
		ObservableList<Receipt> list = FXCollections.observableArrayList();
		list.addAll(endPoint.getReceiptList());

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
		Receipt c = receiptsTable.getSelectionModel().getSelectedItem();
		this.getViewManager().activate(ViewResolver.RECEIPT_EDIT, c.getId());
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}
}
