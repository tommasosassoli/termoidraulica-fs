package fatture.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import fatture.management.GestioneClienti;
import fatture.management.GestioneFatture;
import fatture.model.Cliente;
import fatture.util.StringComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;

public class InserimentoFatturaController {
	protected ObservableList<Cliente> clientiMasterList;
	@FXML
	protected ComboBox<Cliente> clientiBar;
	@FXML
	protected Hyperlink removeFilter;
	@FXML
	protected DatePicker dataFattura;

	public void initialize() {
		ObservableList<Cliente> lc = FXCollections.observableArrayList();
		lc.addAll(GestioneClienti.instance().getClienti());
		clientiMasterList = lc;
		clientiBar.setItems(lc);

		dataFattura.setValue(LocalDate.now().plusDays(1));
	}

	@FXML
	private void insertFattura() {
		// inserisce la fattura e passa alla pagina per inserire i materiali
		LocalDateTime date = LocalDateTime.of(dataFattura.getValue(), LocalTime.MIDNIGHT);

		if (clientiBar.getValue() != null) {
			String id = GestioneFatture.instance().addFattura(getSelectedCliente(), date);

			ModificaFatturaController.setIDfattura(id);
			MainViewController.instance().setView("ModificaFattura");
		} else
			MainViewController.launchInfoDialog("Selezionare un cliente");
	}

	private Cliente getSelectedCliente() {
		int index = clientiBar.getSelectionModel().getSelectedIndex();
		ObservableList<Cliente> clientiList = clientiBar.getItems();
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
		ObservableList<Cliente> listFilter = FXCollections.observableArrayList();
		for (Cliente c : clientiMasterList) {
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
