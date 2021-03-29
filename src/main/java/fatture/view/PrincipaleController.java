package fatture.view;

import fatture.view.MainViewController;
import javafx.fxml.FXML;

public class PrincipaleController {

	@FXML
	private void loadInserimentoCliente() {
		MainViewController.instance().setView("InserimentoCliente");
	}

	@FXML
	private void loadRicercaCliente() {
		MainViewController.instance().setView("RicercaClienti");
	}

	@FXML
	private void loadInserimentoFattura() {
		MainViewController.instance().setView("InserimentoFattura");
	}

	@FXML
	private void loadRiceraFattura() {
		MainViewController.instance().setView("RicercaFattura");
	}

	@FXML
	private void loadInserimentoPreventivo() {
		MainViewController.instance().setView("InserimentoPreventivo");
	}

	@FXML
	private void loadRicercaPreventivo() {
		MainViewController.instance().setView("RicercaPreventivo");
	}

	@FXML
	private void loadModificaDatiAzienda() {
		MainViewController.instance().setView("ModificaDatiAzienda");
	}
}
