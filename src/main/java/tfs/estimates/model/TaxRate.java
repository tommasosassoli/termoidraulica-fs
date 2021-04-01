package tfs.estimates.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxRate {
	@JsonProperty("tax_value")
	private double taxRate;
	
	@JsonProperty("tax_description")
	private String description;
	
	public TaxRate() {
		
	}

	@JsonIgnore
	public double getTaxRateValue() {
		return taxRate;
	}

	@JsonIgnore
	public String getDescription() {
		return description;
	}

	public String toString() {
		return taxRate + " - " + description;
	}

}
