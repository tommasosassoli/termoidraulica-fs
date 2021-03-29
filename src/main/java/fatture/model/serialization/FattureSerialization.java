package fatture.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import fatture.model.Fattura;

@JacksonXmlRootElement(localName = "lista_fatture")
public class FattureSerialization extends AbstractSerialization<Fattura> {
	/**
	 * This is the object that will be written on the fatture.xml file. This object
	 * simply contains a list of fattura.
	 */

	@JacksonXmlProperty(localName = "fattura")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<Fattura> fatture;

	public FattureSerialization() {
		super();
	}

	public FattureSerialization(Collection<Fattura> list) {
		super();
		this.fatture = list;
	}
	
	@JsonIgnore
	public Collection<Fattura> getCollection() {
		return fatture;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return fatture == null || fatture.isEmpty();
	}
}
