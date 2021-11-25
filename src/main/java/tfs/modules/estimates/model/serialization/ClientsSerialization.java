package tfs.modules.estimates.model.serialization;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import tfs.modules.common.model.serialization.AbstractSerialization;
import tfs.modules.estimates.model.Client;

@JacksonXmlRootElement(localName = "clients_list")
public class ClientsSerialization extends AbstractSerialization<Client> {
	/**
	 * This is the object that will be written on the clients.xml file. This object
	 * simply contains a list of clients.
	 */

	@JacksonXmlProperty(localName = "client")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<Client> clients;

	public ClientsSerialization() {
		super();
	}

	public ClientsSerialization(Collection<Client> list) {
		super();
		this.clients = list;
	}
	
	@JsonIgnore
	public Collection<Client> getCollection() {
		return clients;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return clients == null || clients.isEmpty();
	}
}
