package tfs.estimates.view;

import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificaClienteController extends InserimentoClienteController {

	private static String IDcliente;
	@FXML
	private TextField ID;

	public static void setIDCliente(String IDcliente) {
		ModificaClienteController.IDcliente = IDcliente;
	}

	public static String getIDCliente() {
		return IDcliente;
	}

	public void initialize() {
		super.initialize();
		initializeCliente();
	}

	private void initializeCliente() {
		if (IDcliente != null) {
			Client c = ClientsManagement.instance().getClient(IDcliente);

			ID.setText(c.getId());

			nome.setText(c.getName());
			cognome.setText(c.getSurname());
			residenza.setText(c.getResidence());
			comune.setText(c.getMunicipality());
			provincia.setText(c.getProvince());
			cap.setText(c.getCap());
			codiceFiscale.setText(c.getCf());
			note.setText(c.getNotes());
		} else
			goRicercaClienti();
	}

	@FXML
	private void updateCliente() {
		if (super.validateInput()) {
			Client c = new Client(IDcliente, nome.getText(), cognome.getText(), residenza.getText(),
					comune.getText(), provincia.getText(), cap.getText(), codiceFiscale.getText(), note.getText());

			ClientsManagement.instance().updateClient(IDcliente, c);

			goRicercaClienti();
		}
	}

	@FXML
	private void delCliente() {
		if (MainViewController.launchConfirmDialog("Sicuro di voler ELIMINARE questo cliente?")) {
			ClientsManagement.instance().deleteClient(getIDCliente());
			goPrincipale();
		}
	}

	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
	}

	@FXML
	private void goRicercaClienti() {
		MainViewController.instance().setView("RicercaClienti");
	}

}
