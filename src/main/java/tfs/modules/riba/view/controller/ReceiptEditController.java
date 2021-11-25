package tfs.modules.riba.view.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.converter.*;
import tfs.modules.common.management.TaxRateManagement;
import tfs.modules.common.model.TaxRate;
import tfs.modules.riba.management.ReceiptsManagement;
import tfs.modules.riba.model.Receipt;
import tfs.modules.riba.model.Riba;
import tfs.resolvers.ViewResolver;
import tfs.service.AutoSaveService;
import tfs.service.LogService;
import tfs.view.AbstractController;
import tfs.view.ViewManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

public class ReceiptEditController extends AbstractController {
	private Receipt receipt;

	private ObservableList<Riba> observableRiba;

	NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();

	@FXML
	private TextField foreignId;
	@FXML
	private TextField description;
	@FXML
	private DatePicker date = new DatePicker();
	@FXML
	private ComboBox<TaxRate> taxRate;
	@FXML
	private Text totalNoTax;
	@FXML
	private Text totalWithTax;
	@FXML
	private TableView<Riba> ribaTable = new TableView<>();
	@FXML
	private TableColumn<Riba, Integer> numberCol = new TableColumn<>();
	@FXML
	private TableColumn<Riba, Double> amountCol = new TableColumn<>();
	@FXML
	private TableColumn<Riba, LocalDate> expireDateCol = new TableColumn<>();
	@FXML
	private TableColumn<Riba, Boolean> paidCol = new TableColumn<>();

	public ReceiptEditController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		expireDateCol.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
		paidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));

		taxRate.setItems(FXCollections.observableArrayList(TaxRateManagement.instance().getTaxRates()));

		amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		amountCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, Double> t) -> {
			(t.getRowValue()).setAmount(t.getNewValue());
			this.refreshAll();
			notifyChanges();
		});

		expireDateCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		expireDateCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, LocalDate> t) -> {
			(t.getRowValue()).setExpireDate(t.getNewValue());
			notifyChanges();
		});


		paidCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isPaid()));
		paidCol.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
		paidCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, Boolean> t) -> {
			(t.getRowValue()).setPaid(t.getNewValue());
			notifyChanges();
		});
	}

	@Override
	public void refresh(Object arg) {
		try {
			receipt = (Receipt) arg;
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for ReceiptEditController not valid");
		}

		if (receipt != null) {
			refreshAll();
		}
	}

	private void refreshAll() {
		foreignId.setText(receipt.getForeignId());
		description.setText(receipt.getDescription());
		date.setValue(receipt.getDate().toLocalDate());
		taxRate.setValue(receipt.getTaxRate());

		totalNoTax.setText(numberFormat.format(receipt.getTotalWithoutTaxRate()));
		totalWithTax.setText(numberFormat.format(receipt.getTotal()));

		observableRiba = FXCollections.observableArrayList(receipt.getRibaList());
		ribaTable.setItems(observableRiba);
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
	private void addRiba() {
		if (!ribaTable.isDisable()) {
			Riba newRiba = new Riba();
			receipt.addRiba(newRiba);

			ribaTable.getItems().add(newRiba);
			ribaTable.getSelectionModel().selectLast();
			ribaTable.refresh();
			notifyChanges();
		}
	}

	@FXML
	private void removeRiba() {
		if (!ribaTable.isDisable()) {
			if (!ribaTable.getSelectionModel().isEmpty()
					&& ViewManager.launchConfirmDialog("Rimuovere la riga selezionata?")) {
				ribaTable.getItems().remove(ribaTable.getSelectionModel().getSelectedItem());

				ribaTable.refresh();
				notifyChanges();
			}
		}
	}

/*	@FXML
	private void swapItemUp() {
		swapItem(true);
	}

	@FXML
	private void swapItemDown() {
		swapItem(false);
	}
*/
	@FXML
	private void delete() {
		if (ViewManager.launchConfirmDialog("Sei sicuro di voler ELIMINARE questa ricevuta?")) {
			ReceiptsManagement.instance().deleteReceipt(receipt);
			this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
			notifyChanges();
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
	}

/*	private void swapItem(boolean up) {
		int draggedIndex = ribaTable.getSelectionModel().getSelectedIndex();
		Riba draggedItem = ribaTable.getItems().remove(draggedIndex);

		int dropIndex;
		if (up)
			dropIndex = draggedIndex - 1;
		else
			dropIndex = draggedIndex + 1;

		if (dropIndex < 0)
			dropIndex = 0;
		else if (dropIndex > ribaTable.getItems().size())
			dropIndex = ribaTable.getItems().size();

		ribaTable.getItems().add(dropIndex, draggedItem);
		ribaTable.getSelectionModel().select(dropIndex);
		ribaTable.scrollTo(dropIndex);

		notifyChanges();
	}
*/
	private void notifyChanges() {
		AutoSaveService.setModified();
	}
}
