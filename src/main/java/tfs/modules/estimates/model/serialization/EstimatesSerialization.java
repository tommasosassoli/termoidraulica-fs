package tfs.modules.estimates.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import tfs.modules.common.model.serialization.AbstractSerialization;
import tfs.modules.estimates.model.Estimate;

@JacksonXmlRootElement(localName = "estimates_list")
public class EstimatesSerialization extends AbstractSerialization<Estimate> {
	/**
	 * This is the object that will be written on the fatture.xml file. This object
	 * simply contains a list of fattura.
	 */

	@JacksonXmlProperty(localName = "estimate")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<Estimate> estimates;

	public EstimatesSerialization() {
		super();
	}

	public EstimatesSerialization(Collection<Estimate> list) {
		super();
		this.estimates = list;
	}
	
	@JsonIgnore
	public Collection<Estimate> getCollection() {
		return estimates;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return estimates == null || estimates.isEmpty();
	}
}
