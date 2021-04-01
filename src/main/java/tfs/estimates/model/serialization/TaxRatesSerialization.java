package tfs.estimates.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import tfs.estimates.model.TaxRate;

@JacksonXmlRootElement(localName = "tax_rates_list")
public class TaxRatesSerialization extends AbstractSerialization<TaxRate> {
	/**
	 * This is the object that will be written on the tax_rates.xml file. This object
	 * simply contains a list of tax rates.
	 */

	@JacksonXmlProperty(localName = "tax_rate")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<TaxRate> taxRates;

	public TaxRatesSerialization() {
		super();
	}

	@JsonIgnore
	public Collection<TaxRate> getCollection() {
		return taxRates;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return taxRates == null || taxRates.isEmpty();
	}
}
