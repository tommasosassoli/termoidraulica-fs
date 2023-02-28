package tfs.gui.view.controller;

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
import tfs.business.dao.daofactory.ReceiptDaoFactory;
import tfs.business.endpoint.ReceiptEndPoint;
import tfs.business.endpoint.TaxRateEndPoint;
import tfs.business.model.receipt.Receipt;
import tfs.business.model.receipt.Riba;
import tfs.business.model.tax.TaxRate;
import tfs.gui.resolvers.ViewResolver;
import tfs.service.LogService;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

public class ReceiptEditController extends AbstractController {
	private ReceiptEndPoint endPoint = new ReceiptEndPoint();
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
	private Button saveBtn;
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

		taxRate.setItems(FXCollections.observableArrayList(new TaxRateEndPoint().getTaxRatesList()));

		amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		amountCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, Double> t) -> {
			(t.getRowValue()).setAmount(t.getNewValue());
			notifyRibaChanges();
			this.refreshAll();
		});

		expireDateCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		expireDateCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, LocalDate> t) -> {
			(t.getRowValue()).setExpireDate(t.getNewValue());
			notifyRibaChanges();
		});


		paidCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isPaid()));
		paidCol.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
		paidCol.setOnEditCommit((TableColumn.CellEditEvent<Riba, Boolean> t) -> {
			(t.getRowValue()).setPaid(t.getNewValue());
			notifyRibaChanges();
		});
	}

	@Override
	public void refresh(Object arg) {
		try {
			String id = (String) arg;
			receipt = ReceiptDaoFactory.getDao().getReceipt(id);
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

	@FXML
	private void swapRibaUp() {
		swapRiba(true);
	}

	@FXML
	private void swapRibaDown() {
		swapRiba(false);
	}

	@FXML
	private void delete() {
		if (ViewManager.launchConfirmDialog("Sei sicuro di voler ELIMINARE questa ricevuta?")) {
			endPoint.deleteReceipt(receipt.getId());
			this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
		}
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.RECEIPT_LIST);
	}

	private void swapRiba(boolean up) {
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

		notifyRibaChanges();
	}

	@FXML
	private void enableSaveBtn() {
		saveBtn.setDisable(false);
	}

	@FXML
	private void changeReceiptFields() {
		receipt.setForeignId(foreignId.getText());
		receipt.setDescription(description.getText());
		receipt.setDate(date.getValue());
		receipt.setTaxRate(taxRate.getValue());
		notifyChanges();
		saveBtn.setDisable(true);
	}

	private void notifyRibaChanges() {
		receipt.overrideRibaList(observableRiba.stream().toList());
		notifyChanges();
	}

	private void notifyChanges() {
		endPoint.updateReceipt(receipt);
	}
}
