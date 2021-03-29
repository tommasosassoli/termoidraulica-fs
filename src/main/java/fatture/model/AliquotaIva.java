package fatture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AliquotaIva {
	@JsonProperty("aliquota")
	private double aliquota;
	
	@JsonProperty("descrizione_aliquota")
	private String descrizione;
	
	public AliquotaIva() {
		
	}

	@JsonIgnore
	public double getAliquota() {
		return aliquota;
	}

	@JsonIgnore
	public String getDescrizione() {
		return descrizione;
	}

	public String toString() {
		return aliquota + " - " + descrizione;
	}

}
