package fatture.view;

import fatture.management.GestioneClienti;
import fatture.model.Cliente;
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
			Cliente c = GestioneClienti.instance().getCliente(IDcliente);

			ID.setText(c.getId());

			if (c.isSoggettoFisico()) {
				soggFisicoCheckBox.setSelected(true);
				nome.setText(c.getNome());
				cognome.setText(c.getCognome());
				denominazione.setDisable(true);
			} else {
				soggGiuridicoCheckBox.setSelected(true);
				denominazione.setText(c.getDenominazione());
				nome.setDisable(true);
				cognome.setDisable(true);
			}

			residenza.setText(c.getResidenza());
			comune.setText(c.getComune());
			provincia.setText(c.getProvincia());
			cap.setText(c.getCap());
			codiceFiscale.setText(c.getCf());
			note.setText(c.getNote());
		} else
			goRicercaClienti();
	}

	@FXML
	private void updateCliente() {
		if (super.validateInput()) {
			GestioneClienti.instance().updateCliente(IDcliente, nome.getText(), cognome.getText(), residenza.getText(),
					comune.getText(), provincia.getText(), cap.getText(), codiceFiscale.getText(), note.getText());

			goRicercaClienti();
		}
	}

	@FXML
	private void delCliente() {
		if (MainViewController.launchConfirmDialog("Sicuro di voler ELIMINARE questo cliente?")) {
			GestioneClienti.instance().delCliente(getIDCliente());
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
