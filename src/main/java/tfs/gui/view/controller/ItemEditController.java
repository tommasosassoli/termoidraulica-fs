package tfs.gui.view.controller;

import tfs.business.endpoint.TaxRateEndPoint;
import tfs.business.model.estimate.Item;
import tfs.business.model.tax.TaxRate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import tfs.service.LogService;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class ItemEditController extends AbstractController {
	private Item item;
	@FXML
	private TextArea description;
	@FXML
	private TextField um;
	@FXML
	private TextField quantity;
	@FXML
	private TextField price;
	@FXML
	private TextField calculatedPrice;
	@FXML
	private TextField discount;
	@FXML
	private ComboBox<TaxRate> taxRate;

	public ItemEditController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		description.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.TAB) {
				this.um.requestFocus();
				event.consume();
			}
		});

		um.setOnMouseClicked(mc -> um.selectAll());

		price.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		price.setOnMouseClicked(mc -> price.selectAll());

		calculatedPrice.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		calculatedPrice.setOnMouseClicked(mc -> calculatedPrice.selectAll());

		discount.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		discount.setOnMouseClicked(mc -> discount.selectAll());

		quantity.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		quantity.setOnMouseClicked(mc -> quantity.selectAll());

		quantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantity.getText());
				double pUnit = Double.parseDouble(price.getText());

				calculatedPrice.setText(String.valueOf(qt * pUnit));
			}
		});

		price.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantity.getText());
				double pUnit = Double.parseDouble(price.getText());

				calculatedPrice.setText(String.valueOf(qt * pUnit));
			}
		});

		calculatedPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				double qt = Double.parseDouble(quantity.getText());
				double pTot = Double.parseDouble(calculatedPrice.getText());

				price.setText(String.valueOf(pTot / qt));
			}
		});
	}

	@Override
	public void refresh(Object arg) {
		try {
			item = (Item) arg;
		} catch (ClassCastException e) {
			LogService.warning(getClass(), "Init params for ItemEditController not valid");
		}

		if (item != null) {
			description.setText(item.getDescription());
			um.setText(item.getUm());
			quantity.setText(String.valueOf(item.getQt()));
			price.setText(String.valueOf(item.getPrice()));
			calculatedPrice.setText(String.valueOf(item.getPrice() * item.getQt()));
			discount.setText(String.valueOf(Math.abs(item.getDiscount())));
			taxRate.setItems(FXCollections.observableArrayList(new TaxRateEndPoint().getTaxRatesList()));
			taxRate.setValue(item.getTaxRate());
		}
	}

	@FXML
	private void viewKeyEvent(KeyEvent event) {
		if (event.getCode().equals(KeyCode.TAB)) {
			this.um.requestFocus();
		}
	}

	@FXML
	private void endEdit() {
		item.setDescription(description.getText());
		item.setUm(um.getText());
		item.setQt(Double.parseDouble(quantity.getText()));
		item.setPrice(Double.parseDouble(price.getText()));
		item.setDiscount(Double.parseDouble(discount.getText()));
		item.setTaxRate(taxRate.getValue());

		close();
	}

	private void close() {
		((Stage) description.getScene().getWindow()).close(); // get the scene and close
	}

}
