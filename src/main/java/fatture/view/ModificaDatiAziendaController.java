package fatture.view;

import fatture.management.GestioneDatiAzienda;
import fatture.service.AutoSaveService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModificaDatiAziendaController {
	@FXML
	private TextField nomeAzienda;
	@FXML
	private TextField iban;
	@FXML
	private TextField telefono;
	@FXML
	private TextField sede;
	@FXML
	private TextField comune;
	@FXML
	private TextField provincia;
	@FXML
	private TextField cap;
	@FXML
	private TextField piva;
	
	public void initialize() {
		nomeAzienda.setText(GestioneDatiAzienda.instance().getDatiAzienda().getNomeAzienda());
		telefono.setText(GestioneDatiAzienda.instance().getDatiAzienda().getTelefono());
		sede.setText(GestioneDatiAzienda.instance().getDatiAzienda().getsede());
		comune.setText(GestioneDatiAzienda.instance().getDatiAzienda().getCitta());
		provincia.setText(GestioneDatiAzienda.instance().getDatiAzienda().getProvincia());
		cap.setText(GestioneDatiAzienda.instance().getDatiAzienda().getCap());
		iban.setText(GestioneDatiAzienda.instance().getDatiAzienda().getIban());
		piva.setText(GestioneDatiAzienda.instance().getDatiAzienda().getPiva());
	}
	
	@FXML
	private void saveData() {
		GestioneDatiAzienda.instance().getDatiAzienda().setNomeAzienda(nomeAzienda.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setTelefono(telefono.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setsede(sede.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setCitta(comune.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setProvincia(provincia.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setCap(cap.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setIban(iban.getText());
		GestioneDatiAzienda.instance().getDatiAzienda().setPiva(piva.getText());
		
		AutoSaveService.setModified();
		
		MainViewController.instance().setView("Principale");
	}
	
	@FXML
	private void goPrincipale() {
		MainViewController.instance().setView("Principale");
	}
}
