package tfs.estimates.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import tfs.estimates.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;

public class InserimentoFatturaController {
	protected ObservableList<Client> clientiMasterList;
	@FXML
	protected ComboBox<Client> clientiBar;
	@FXML
	protected Hyperlink removeFilter;
	@FXML
	protected DatePicker dataFattura;

	public void initialize() {
		ObservableList<Client> lc = FXCollections.observableArrayList();
		lc.addAll(ClientsManagement.instance().getClients());
		clientiMasterList = lc;
		clientiBar.setItems(lc);

		dataFattura.setValue(LocalDate.now().plusDays(1));
	}

	@FXML
	private void insertFattura() {
		// inserisce la fattura e passa alla pagina per inserire i materiali
		LocalDateTime date = LocalDateTime.of(dataFattura.getValue(), LocalTime.MIDNIGHT);

		if (clientiBar.getValue() != null) {
			String id = EstimatesManagement.instance().addEstimate(getSelectedCliente(), date);

			ModificaFatturaController.setIDfattura(id);
			MainViewController.instance().setView("ModificaFattura");
		} else
			MainViewController.launchInfoDialog("Selezionare un cliente");
	}

	private Client getSelectedCliente() {
		int index = clientiBar.getSelectionModel().getSelectedIndex();
		ObservableList<Client> clientiList = clientiBar.getItems();
		return clientiList.get(index);
	}

	@FXML
	protected void searchCliente() {
		clientiBarFilter(MainViewController.launchInputDialog("Nome cliente: "));
		removeFilter.setVisible(true);
	}

	@FXML
	protected void removeFilter() {
		clientiBar.setItems(clientiMasterList);
		removeFilter.setVisible(false);
	}

	protected void clientiBarFilter(String name) {
		ObservableList<Client> listFilter = FXCollections.observableArrayList();
		for (Client c : clientiMasterList) {
			if (StringComparator.containsInsensitive(c.toString(), " ", name, " "))
				listFilter.add(c);
		}
		clientiBar.setItems(listFilter);
	}

	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
	}
}
