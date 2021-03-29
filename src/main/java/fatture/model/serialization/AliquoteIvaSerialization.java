package fatture.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import fatture.model.AliquotaIva;

@JacksonXmlRootElement(localName = "lista_aliquote_iva")
public class AliquoteIvaSerialization extends AbstractSerialization<AliquotaIva> {
	/**
	 * This is the object that will be written on the fatture.xml file. This object
	 * simply contains a list of fattura.
	 */

	@JacksonXmlProperty(localName = "aliquota_iva")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<AliquotaIva> aliquote;

	public AliquoteIvaSerialization() {
		super();
	}

	public AliquoteIvaSerialization(Collection<AliquotaIva> list) {
		super();
		this.aliquote = list;
	}
	
	@JsonIgnore
	public Collection<AliquotaIva> getCollection() {
		return aliquote;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return aliquote == null || aliquote.isEmpty();
	}
}
