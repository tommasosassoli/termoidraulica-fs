package tfs.estimates.view.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import tfs.estimates.resolvers.ViewResolver;
import tfs.estimates.service.AutoSaveService;
import tfs.estimates.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import tfs.estimates.view.ViewManager;

public class EstimateInsertController extends AbstractController {
	private ObservableList<Client> clientsMasterList;
	@FXML
	private ComboBox<Client> clientsBar;
	@FXML
	private Hyperlink removeFilter;
	@FXML
	private DatePicker estimateDate;

	public EstimateInsertController(ViewManager viewManager) {
		super(viewManager);
	}

	public void initialize() {
		ObservableList<Client> lc = FXCollections.observableArrayList();
		lc.addAll(ClientsManagement.instance().getClients());
		clientsMasterList = lc;
		clientsBar.setItems(lc);

		estimateDate.setValue(LocalDate.now().plusDays(1));
	}

	@FXML
	private void insert() {
		// inserisce la fattura e passa alla pagina per inserire i materiali
		LocalDateTime date = LocalDateTime.of(estimateDate.getValue(), LocalTime.MIDNIGHT);

		if (clientsBar.getValue() != null) {
			String id = EstimatesManagement.instance().addEstimate(getSelectedClient(), date);

			AutoSaveService.setModified();
			this.getViewManager().activate(ViewResolver.ESTIMATE_EDIT, id);
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
		ObservableList<Client> listFilter = FXCollections.observableArrayList();
		for (Client c : clientsMasterList) {
			if (StringComparator.containsInsensitive(c.toString(), " ", name, " "))
				listFilter.add(c);
		}
		clientsBar.setItems(listFilter);
	}
	
	private Client getSelectedClient() {
		int index = clientsBar.getSelectionModel().getSelectedIndex();
		ObservableList<Client> clientiList = clientsBar.getItems();
		return clientiList.get(index);
	}
}
