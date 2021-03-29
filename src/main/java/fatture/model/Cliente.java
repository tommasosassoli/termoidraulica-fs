package fatture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cliente {
	
	@JsonProperty("id_cliente")
	private String id;
	
	@JsonProperty("denominazione")
	private String denominazione;
	
	@JsonProperty("nome")
	private String nome;
	
	@JsonProperty("cognome")
	private String cognome;
	
	@JsonProperty("residenza")
	private String residenza;
	
	@JsonProperty("comune")
	private String comune;
	
	@JsonProperty("provincia")
	private String provincia;
	
	@JsonProperty("cap")
	private String cap;
	
	@JsonProperty("codice_fiscale")
	private String cf;
	
	@JsonProperty("note_aggiuntive")
	private String note;
	
	public Cliente() {
		
	}

	public Cliente(String id, String nome, String cognome, String residenza, String comune, String provincia,
			String cap, String cf, String note) {

		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.residenza = residenza;
		this.comune = comune;
		this.provincia = provincia;
		this.cap = cap;
		this.cf = cf;
		this.note = note;
	}

	public Cliente(String id, String denominazione, String residenza, String comune, String provincia, String cap,
			String cf, String note) {

		this.id = id;
		this.denominazione = denominazione;
		this.residenza = residenza;
		this.comune = comune;
		this.provincia = provincia;
		this.cap = cap;
		this.cf = cf;
		this.note = note;
	}

	@JsonIgnore
	public String getId() {
		return this.id;
	}

	@JsonIgnore
	public boolean isSoggettoFisico() {
		return denominazione == null || denominazione.isEmpty();
	}

	@JsonIgnore
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	@JsonIgnore
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonIgnore
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@JsonIgnore
	public String getResidenza() {
		return residenza;
	}

	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	@JsonIgnore
	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	@JsonIgnore
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@JsonIgnore
	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	@JsonIgnore
	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	@JsonIgnore
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@JsonIgnore
	public String getNomeCognome() {
		return toString();
	}

	public String toString() {
		if (isSoggettoFisico())
			return cognome + " " + nome;
		else
			return denominazione;
	}

}
