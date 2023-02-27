package tfs.gui.view.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import tfs.business.endpoint.CustomerEndPoint;
import tfs.business.endpoint.EstimateEndPoint;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.business.model.estimate.Item;
import tfs.business.model.estimate.ItemGroup;
import tfs.business.model.tax.TaxRate;
import tfs.business.resolvers.FileResolver;
import tfs.gui.resolvers.ViewResolver;
import tfs.service.LogService;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;
import tfs.gui.view.tasks.PrintTask;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class EstimateEditController extends AbstractController {
	private EstimateEndPoint estimateEndPoint = new EstimateEndPoint();
	private CustomerEndPoint customerEndPoint = new CustomerEndPoint();
	private String estimateID;
	private Estimate estimate;

	private ObservableList<Customer> observableCustomers;
	private ObservableList<ItemGroup> observableGroups;
	private ObservableList<Item> observableItems;

	NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();

	@FXML
	private AnchorPane progressIndicatorLayer;
	@FXML
	private ComboBox<Customer> clientsBar = new ComboBox<>();
	@FXML
	private DatePicker estimateDate = new DatePicker();
	@FXML
	private TextField id;
	@FXML
	private TableView<ItemGroup> itemGroupTable = new TableView<>();
	@FXML
	private TableColumn<ItemGroup, String> subDescriptionCol = new TableColumn<>();
	@FXML
	private TableColumn<ItemGroup, Double> itemGroupSubtotalCol = new TableColumn<>();
	@FXML
	private TableView<Item> itemsTable = new TableView<>();
	@FXML
	private TableColumn<Item, String> descriptionCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, String> umCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, Double> qtCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, Double> discountCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, TaxRate> taxCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, Double> priceCol = new TableColumn<>();
	@FXML
	private TableColumn<Item, Double> calculatedPriceCol = new TableColumn<>();
	@FXML
	private final Button saveButton = new Button();
	@FXML
	private TextField deposit;
	@FXML
	private final Button depositButton = new Button();
	@FXML
	private Label subTotal;
	@FXML
	private Label calculatedTax;
	@FXML
	private Label taxableTotal;
	@FXML
	private Label estimateTotal;

	public EstimateEditController(ViewManager viewManager) {
		super(viewManager);
	}

	public Estimate getEstimate() {
		if (estimate != null) {
			return estimate;
		} else if (estimateID != null) {
			estimate = estimateEndPoint.getEstimate(estimateID);
		}
		return estimate;
	}

	public void initialize() {
		// item group table
		subDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		itemGroupSubtotalCol.setCellValueFactory(new PropertyValueFactory<>("itemGroupSubtotal"));

		itemGroupTable.getSelectionModel().selectedItemProperty().addListener((obj, oldSelection, newSelection) -> {
			if (newSelection != null)
				this.refreshItems();
		});

		itemGroupTable.setRowFactory(st -> {
			TableRow<ItemGroup> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					editItemGroup();
				}
			});
			return row;
		});

		// items table
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		umCol.setCellValueFactory(new PropertyValueFactory<>("um"));
		qtCol.setCellValueFactory(new PropertyValueFactory<>("qt"));
		discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
		taxCol.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		calculatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("itemSubtotal"));

		// events on items table
		itemsTable.setRowFactory(tv -> {
			TableRow<Item> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					editItem();
				}
			});
			return row;
		});

		itemsTable.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				this.editItem();
			} else if (event.getCode().equals(KeyCode.ADD) || event.getCode().equals(KeyCode.PLUS)) {
				this.addItem();
			} else if (event.getCode().equals(KeyCode.DELETE)) {
				this.removeItem();

			} else if (event.getCode().equals(KeyCode.UP) && event.isControlDown()) { // to swap elements
				this.swapItem(true);
				event.consume();
			} else if (event.getCode().equals(KeyCode.DOWN) && event.isControlDown()) {
				this.swapItem(false);
				event.consume();
			}
			this.refreshTotals();
		});

		// totals
		deposit.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		deposit.setOnMouseClicked(mc -> deposit.selectAll());
		deposit.setOnAction(ac -> onDepositButtonAction());

		Platform.runLater(() -> itemsTable.requestFocus());
	}

	@Override
	public void refresh(Object arg) {
		try {
			estimateID = (String) arg;
			estimate = null;
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for EstimateEditController not valid");
		}

		if (getEstimate() != null) {
			refreshEstimate();
			refreshCustomersBar();
			refreshItemGroups();
			refreshItems();
			refreshTotals();
		}
	}

	private void refreshEstimate() {
		id.setText(estimate.getId());
		estimateDate.setValue(estimate.getExpirationDate().toLocalDate());
	}

	private void refreshCustomersBar() {
		observableCustomers = FXCollections.observableArrayList(customerEndPoint.getCustomerList());
		clientsBar.setItems(observableCustomers);
		clientsBar.setValue(estimate.getCustomer());
	}

	private void refreshItemGroups() {
		List<ItemGroup> list = estimate.getItemGroups();
		observableGroups = FXCollections.observableList(list);
		itemGroupTable.setItems(observableGroups);
	}

	private void refreshItems() {
		ItemGroup group = itemGroupTable.getSelectionModel().getSelectedItem();
		if (group != null) {
			List<Item> list = group.getItems();
			observableItems = FXCollections.observableList(list);
			itemsTable.setItems(observableItems);
			itemsTable.setDisable(false);
		} else {
			itemsTable.getItems().clear();
			itemsTable.setDisable(true);
		}
	}

	private void refreshTotals() {
		double subTot = estimate.getEstimateSubtotal();
		double imponibile = estimate.getTaxableTotal();
		double imposte = estimate.getCalculatedTax();
		double totale = estimate.getEstimateTotal();

		subTotal.setText(numberFormat.format(subTot));
		deposit.setText(String.valueOf(getEstimate().getDeposit()));
		calculatedTax.setText(numberFormat.format(imposte));
		taxableTotal.setText(numberFormat.format(imponibile));
		estimateTotal.setText(numberFormat.format(totale));
	}

	@FXML
	private void viewKeyEvent(KeyEvent event) {
		if (event.isControlDown()) {
			if (event.getCode().equals(KeyCode.Z)) {
				this.backButton();
			}
		}
	}

	@FXML
	private void changeCustomer() {
		Customer c = clientsBar.getValue();
		estimate.setCustomer(c);
		notifyChanges();
	}

	@FXML
	private void changeDate() {
		LocalDate d = estimateDate.getValue();
		LocalDateTime dt = LocalDateTime.of(d, LocalTime.MIDNIGHT);
		estimate.setExpirationDate(dt);
		notifyChanges();
	}

	@FXML
	private void estimatePrint() {
		if (!itemGroupTable.getItems().isEmpty())
			print(FileResolver.REPORT_ESTIMATE);
		else
			ViewManager.launchInfoDialog("Non sono cotenuti elementi nella tabella");
	}

	@FXML
	private void estimatePrintB() {
		if (!itemGroupTable.getItems().isEmpty())
			print(FileResolver.REPORT_ESTIMATE_B);
		else
			ViewManager.launchInfoDialog("Non sono cotenuti elementi nella tabella");
	}

	@FXML
	private void addItemGroup() {
		ItemGroup newGroup = new ItemGroup();
		itemGroupTable.getItems().add(newGroup);
		itemGroupTable.getSelectionModel().selectLast();

		launchEditItemGroupPopup(newGroup);
		if (newGroup.isEmpty()) {
			boolean remove = ViewManager.launchConfirmDialog("Il sottototale inserito è vuoto, vuoi rimuoverlo?");
			if (remove)
				itemGroupTable.getItems().remove(newGroup);
		}
		estimate.overrideItemGroups(observableGroups.stream().toList());
		itemGroupTable.refresh();
		notifyChanges();
	}

	private void editItemGroup() {
		ItemGroup group = itemGroupTable.getSelectionModel().getSelectedItem();
		if(group != null) {
			launchEditItemGroupPopup(group);

			estimate.overrideItemGroups(observableGroups.stream().toList());
			itemGroupTable.refresh();
			notifyChanges();
		}
	}

	@FXML
	private void removeItemGroup() {
		if (!itemGroupTable.getSelectionModel().isEmpty()
				&& ViewManager.launchConfirmDialog("Rimuovere il sottototale selezionato?")) {
			itemGroupTable.getItems().remove(itemGroupTable.getSelectionModel().getSelectedItem());

			estimate.overrideItemGroups(observableGroups.stream().toList());
			itemGroupTable.refresh();
			this.refreshTotals();
			notifyChanges();
		}
	}

	@FXML
	private void swapItemGroupUp() {
		swapItemGroup(true);
	}

	@FXML
	private void swapItemGroupDown() {
		swapItemGroup(false);
	}

	@FXML
	private void addItem() {
		if (!itemsTable.isDisable()) {
			Item newItem = new Item();
			itemsTable.getItems().add(newItem);
			itemsTable.getSelectionModel().selectLast();

			launchEditItemPopup(newItem);
			if (newItem.isEmpty()) {
				itemsTable.getItems().remove(newItem);
			}
			overrideItemsForItemGroup();
			itemsTable.refresh();
			itemGroupTable.refresh();
			refreshTotals();
			notifyChanges();
		}
	}

	private void editItem() {
		Item item = itemsTable.getSelectionModel().getSelectedItem();
		if (item != null) {
			launchEditItemPopup(item);

			overrideItemsForItemGroup();
			itemGroupTable.refresh();
			itemsTable.refresh();
			refreshTotals();
			notifyChanges();
		}
	}

	@FXML
	private void removeItem() {
		if (!itemsTable.isDisable()) {
			if (!itemsTable.getSelectionModel().isEmpty()
					&& ViewManager.launchConfirmDialog("Rimuovere la riga selezionata?")) {
				itemsTable.getItems().remove(itemsTable.getSelectionModel().getSelectedItem());

				overrideItemsForItemGroup();
				itemsTable.refresh();
				itemGroupTable.refresh();
				refreshTotals();
				notifyChanges();
			}
		}
	}

	private void overrideItemsForItemGroup() {
		ItemGroup g = itemGroupTable.getSelectionModel().getSelectedItem();
		if (g != null) {
			g.overrideItem(itemsTable.getItems());
			estimate.overrideItemGroups(itemGroupTable.getItems());
		}
	}

	@FXML
	private void swapItemUp() {
		swapItem(true);
	}

	@FXML
	private void swapItemDown() {
		swapItem(false);
	}

	@FXML
	private void crossItem() {
		ItemGroup from = itemGroupTable.getSelectionModel().getSelectedItem();
		Item item = itemsTable.getSelectionModel().getSelectedItem();
		int itemIndex = itemsTable.getSelectionModel().getSelectedIndex();

		ItemGroup to = ViewManager.launchListInputDialog("Seleziona un subtotale",
				itemGroupTable.getItems().stream().toList(), from);

		if (to != null && to != from) {
			from.removeItem(itemIndex);
			to.addItem(item);
		}

		estimate.overrideItemGroups(itemGroupTable.getItems());
		notifyChanges();
		itemGroupTable.refresh();
		refreshItems();
	}

	@FXML
	private void onDepositButtonAction() {
		if (!deposit.isDisable()) {
			double val = Double.parseDouble(deposit.getText());
			double sub = getEstimate().getEstimateSubtotal();

			if (val <= sub) {
				getEstimate().setDeposit(val);

				deposit.setDisable(true);
				depositButton.setText("modifica");

			} else {
				ViewManager.launchInfoDialog("Valore acconto non valido!");
				getEstimate().setDeposit(sub);
				deposit.setText(String.valueOf(sub));
			}
			notifyChanges();
			this.refreshTotals();

		} else {
			deposit.setDisable(false);
			depositButton.setText("ok");
			deposit.requestFocus();
		}
	}

	@FXML
	private void delete() {
		if (ViewManager.launchConfirmDialog("Sei sicuro di voler ELIMINARE questo preventivo?")) {
			estimateEndPoint.deleteEstimate(getEstimate().getId());
			this.getViewManager().activate(ViewResolver.ESTIMATE_LIST);
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.ESTIMATE_LIST);
	}

	private void swapItem(boolean up) {
		int draggedIndex = itemsTable.getSelectionModel().getSelectedIndex();
		Item draggedItem = itemsTable.getItems().remove(draggedIndex);

		int dropIndex;
		if (up)
			dropIndex = draggedIndex - 1;
		else
			dropIndex = draggedIndex + 1;

		if (dropIndex < 0)
			dropIndex = 0;
		else if (dropIndex > itemsTable.getItems().size())
			dropIndex = itemsTable.getItems().size();

		itemsTable.getItems().add(dropIndex, draggedItem);
		itemsTable.getSelectionModel().select(dropIndex);
		itemsTable.scrollTo(dropIndex);

		overrideItemsForItemGroup();
		notifyChanges();
	}

	private void swapItemGroup(boolean up) {
		int draggedIndex = itemGroupTable.getSelectionModel().getSelectedIndex();
		ItemGroup draggedItem = itemGroupTable.getItems().remove(draggedIndex);

		int dropIndex;
		if (up)
			dropIndex = draggedIndex - 1;
		else
			dropIndex = draggedIndex + 1;

		if (dropIndex < 0)
			dropIndex = 0;
		else if (dropIndex > itemGroupTable.getItems().size())
			dropIndex = itemGroupTable.getItems().size();

		itemGroupTable.getItems().add(dropIndex, draggedItem);
		itemGroupTable.getSelectionModel().select(dropIndex);
		itemGroupTable.scrollTo(dropIndex);

		notifyChanges();
	}

	private void launchEditItemGroupPopup(ItemGroup group) {
		getViewManager().launchNewWindow(ViewResolver.ITEM_GROUP_EDIT, "Modifica Descrizione Sottototale",
				true, false, group);
	}

	private void launchEditItemPopup(Item item) {
		getViewManager().launchNewWindow(ViewResolver.ITEM_EDIT, "Modifica Riga Preventivo",
				true, false, item);
	}

	private void print(FileResolver fileName) {
		progressIndicatorLayer.setVisible(true);

		PrintTask task = new PrintTask(getEstimate(), fileName);
		task.setOnSucceeded(e -> progressIndicatorLayer.setVisible(false));
		task.setOnFailed(e -> progressIndicatorLayer.setVisible(false));

		new Thread(task).start();
	}

	private void notifyChanges() {
		estimateEndPoint.updateEstimate(getEstimate());
	}
}
