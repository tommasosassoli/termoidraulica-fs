package tfs.modules.riba.model.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import tfs.modules.common.model.serialization.AbstractSerialization;
import tfs.modules.riba.model.Receipt;


import java.util.Collection;

@JacksonXmlRootElement(localName = "receipts_list")
public class ReceiptsSerialization extends AbstractSerialization<Receipt> {
	/**
	 * This is the object that will be written on the receipts.xml file. This object
	 * simply contains a list of receipts.
	 */

	@JacksonXmlProperty(localName = "receipts")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Collection<Receipt> receipts;

	public ReceiptsSerialization() {
		super();
	}

	public ReceiptsSerialization(Collection<Receipt> list) {
		super();
		this.receipts = list;
	}
	
	@JsonIgnore
	public Collection<Receipt> getCollection() {
		return receipts;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return receipts == null || receipts.isEmpty();
	}
}
