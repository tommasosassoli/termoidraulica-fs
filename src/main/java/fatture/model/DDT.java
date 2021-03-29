package fatture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DDT {
	/** documento di trasporto */
	
	@JsonProperty("numero")
	private String numeroDDT;
	
	@JsonProperty("data")
	private LocalDate dataDDT;
	
	public DDT() {
		
	}

	public DDT(String numeroDDT, LocalDate dataDDT) {
		this.numeroDDT = numeroDDT;
		this.dataDDT = dataDDT;
	}

	@JsonIgnore
	public String getNumeroDDT() {
		return numeroDDT;
	}

	public void setNumeroDDT(String numeroDDT) {
		this.numeroDDT = numeroDDT;
	}

	@JsonIgnore
	public LocalDate getDataDDT() {
		return dataDDT;
	}

	public void setDataDDT(LocalDate dataDDT) {
		this.dataDDT = dataDDT;
	}

}
