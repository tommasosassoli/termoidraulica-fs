package fatture.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import fatture.management.GestioneAliquoteIva;

public class Materiale {
	@JsonProperty("descrizione")
	private String descrizione;
	
	@JsonProperty("unita_misura")
	private String um;
	
	@JsonProperty("quantita")
	private double qt;
	
	@JsonProperty("prezzo")
	private double prezzo;
	
	@JsonProperty("aliquota_iva")
	private AliquotaIva iva;
	
	@JsonProperty("sconto_maggiorazione")
	private double scontoMaggiorazione;
	
	@JacksonXmlElementWrapper(localName = "lista_DDT")
	@JsonProperty("DDT")
	private final ArrayList<DDT> ddt = new ArrayList<>();
	
	public Materiale(String descrizione, String um, double qt, double prezzo,
			AliquotaIva iva) {
		this.descrizione = descrizione;
		this.um = um;
		this.qt = qt;
		this.prezzo = prezzo;
		this.iva = iva;
		this.scontoMaggiorazione = 0;
	}

	public Materiale() {
		this.qt = 0;
		this.prezzo = 0;
		this.iva = GestioneAliquoteIva.instance().getAliquote().get(0); // 22%
		this.scontoMaggiorazione = 0;
	}

	@JsonIgnore
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@JsonIgnore
	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	@JsonIgnore
	public double getQt() {
		return qt;
	}

	public void setQt(double qt) {
		this.qt = qt;
	}

	@JsonIgnore
	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	@JsonIgnore
	public AliquotaIva getAliquotaIva() {
		return iva;
	}

	public void setAliquotaIva(AliquotaIva iva) {
		this.iva = iva;
	}

	@JsonIgnore
	public double getScontoMaggiorazione() {
		return scontoMaggiorazione;
	}

	public void setScontoMaggiorazione(double scontoMaggiorazione) {
		this.scontoMaggiorazione = scontoMaggiorazione;
	}

	@JsonIgnore
	public List<DDT> getListDdt() {
		return ddt;
	}

	public void overrideDdt(List<DDT> list) {
		this.ddt.clear();
		this.ddt.addAll(list);
	}

	@JsonIgnore
	public double getSubtotale() {
		double imponibile = qt * prezzo;
		double scontoPrezzo = (scontoMaggiorazione * imponibile) / 100;
		return imponibile + scontoPrezzo;	//imponibile scontato
	}

}
