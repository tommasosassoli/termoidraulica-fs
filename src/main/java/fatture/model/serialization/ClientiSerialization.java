package fatture.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import fatture.model.Cliente;

@JacksonXmlRootElement(localName = "lista_clienti")
public class ClientiSerialization extends AbstractSerialization<Cliente> {
	/**
	 * This is the object that will be written on the clienti.xml file. This object
	 * simply contains a list of clienti.
	 */

	@JacksonXmlProperty(localName = "cliente")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<Cliente> clienti;

	public ClientiSerialization() {
		super();
	}

	public ClientiSerialization(Collection<Cliente> list) {
		super();
		this.clienti = list;
	}
	
	@JsonIgnore
	public Collection<Cliente> getCollection() {
		return clienti;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return clienti == null || clienti.isEmpty();
	}
}
