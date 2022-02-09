package tfs.gui.view.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import tfs.business.endpoint.CustomerEndPoint;
import tfs.business.endpoint.EstimateEndPoint;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.gui.resolvers.ViewResolver;
import tfs.gui.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import tfs.gui.view.AbstractController;
import tfs.gui.view.ViewManager;

public class EstimateInsertController extends AbstractController {
	private EstimateEndPoint estimateEndPoint = new EstimateEndPoint();
	private CustomerEndPoint customerEndPoint = new CustomerEndPoint();
	private ObservableList<Customer> clientsMasterList;
	@FXML
	private ComboBox<Customer> clientsBar;
	@FXML
	private Hyperlink removeFilter;
	@FXML
	private DatePicker estimateDate;

	public EstimateInsertController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		ObservableList<Customer> lc = FXCollections.observableArrayList();
		lc.addAll(customerEndPoint.getCustomerList());
		clientsMasterList = lc;
		clientsBar.setItems(lc);

		estimateDate.setValue(LocalDate.now().plusDays(1));
	}

	@FXML
	private void insert() {
		// inserisce la fattura e passa alla pagina per inserire i materiali
		LocalDateTime date = LocalDateTime.of(estimateDate.getValue(), LocalTime.MIDNIGHT);

		if (clientsBar.getValue() != null) {
			Estimate e = new Estimate(null, getSelectedCustomer(), date);
			estimateEndPoint.addEstimate(e);

			this.getViewManager().activate(ViewResolver.HOME);	//FIXME goto edit page directly
		} else
			ViewManager.launchInfoDialog("Selezionare un cliente");
	}

	@FXML
	private void applyFilter() {
		clientsBarFilter(ViewManager.launchTextInputDialog("Nome cliente: "));
		removeFilter.setVisible(true);
	}

	@FXML
	private void removeFilter() {
		clientsBar.setItems(clientsMasterList);
		removeFilter.setVisible(false);
	}

	@FXML
	private void backButton() {
		this.getViewManager().activate(ViewResolver.HOME);
	}

	private void clientsBarFilter(String name) {
		ObservableList<Customer> listFilter = FXCollections.observableArrayList();
		for (Customer c : clientsMasterList) {
			if (StringComparator.containsInsensitive(c.toString(), " ", name, " "))
				listFilter.add(c);
		}
		clientsBar.setItems(listFilter);
	}
	
	private Customer getSelectedCustomer() {
		int index = clientsBar.getSelectionModel().getSelectedIndex();
		ObservableList<Customer> clientiList = clientsBar.getItems();
		return clientiList.get(index);
	}
}
