package tfs.estimates.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.management.logic.TaxRateManagement;

public class Estimate {
	@JsonProperty("estimate_id")
	private String id;
	
	@JsonProperty("client_id")
	private String clientId;
	
	@JsonIgnore
	private Client client;
	
	@JsonProperty("expiration_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss") 
	private LocalDateTime expirationDate;
	
	@JsonProperty("insert_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss") 
	private LocalDateTime insertDate;
	
	@JacksonXmlElementWrapper(localName = "item_groups_list")
	@JsonProperty("item_group")
	private final List<ItemGroup> itemGroups = new ArrayList<>();
	
	@JsonProperty("deposit")
	private double deposit;
	
	public Estimate() {
		
	}

	public Estimate(String id, Client client, LocalDateTime expirationDate) {
		this.id = id;
		this.client = client;
		this.expirationDate = expirationDate;
		this.insertDate = LocalDateTime.now();
	}

	@JsonIgnore
	public String getId() {
		return this.id;
	}

	@JsonIgnore
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	@JsonIgnore
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	@JsonIgnore
	public LocalDateTime getInsertDate() {
		return this.insertDate;
	}

	@JsonIgnore
	public String getFormatExpiringDate() {
		LocalDateTime exp = this.expirationDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		return exp.format(formatter);
	}
	
	@JsonProperty("expiration_date")
	private void setFormatExpiringDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
		this.expirationDate = LocalDateTime.parse(data, formatter);
	}

	@JsonIgnore
	public String getFormatInsertDate() {
		LocalDateTime exp = this.insertDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy - H : m : s");
		return exp.format(formatter);
	}
	
	@JsonProperty("insert_date")
	private void setFormatInsertDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
		this.insertDate = LocalDateTime.parse(data, formatter);
	}

	@JsonIgnore
	public Client getClient() {
		if (client == null)
			client = ClientsManagement.instance().getClient(clientId);
		return client;
	}

	public void setClient(String clientID) {
		this.clientId = clientID;
		client = null;
	}

	@JsonIgnore
	public List<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public void addItemGroup(ItemGroup g) {
		itemGroups.add(g);
	}

	public void overrideItemGroups(List<ItemGroup> list) {
		itemGroups.clear();
		itemGroups.addAll(list);
	}

	@JsonIgnore
	public double getDeposit() {
		return deposit;
	}

	/**
	 * @param deposit must be positive*/
	public void setDeposit(double deposit) {
		if(deposit < 0)
			deposit = 0;
		this.deposit = deposit;
	}

	@JsonIgnore
	public List<Taxable> getTaxableList() {
		List<TaxRate> taxRates = TaxRateManagement.instance().getTaxRates();
		List<Taxable> taxables = new ArrayList<>(taxRates.size());

		for (TaxRate t : taxRates)
			taxables.add(new Taxable(0, t));

		for (ItemGroup g : itemGroups) {
			for (Item i : g.getItems()) {
				for (Taxable t : taxables) {
					if (t.getTaxRate().getTaxRateValue() == i.getTaxRate().getTaxRateValue()) {
						t.setTaxable(t.getTaxable() + i.getItemSubtotal());
					}
				}
			}
		}

		return taxables;
	}

	/**
	 * @return the subtotal for the estimate
	 * */
	@JsonIgnore
	public double getEstimateSubtotal() {
		double res = 0;
		for (ItemGroup m : itemGroups)
			res += m.getItemGroupSubtotal();

		return res;
	}
	
	/**
	 * @return estimateSubtotal - deposit 
	 * */
	@JsonIgnore
	public double getTaxableTotal() {
		double res = getEstimateSubtotal() - getDeposit();
		return (res > 0) ? res : 0;
	}

	/**
	 * @return the calculated tax
	 * */
	@JsonIgnore
	public double getCalculatedTax() {
		double totImposta = 0;
		for (Taxable i : getTaxableList()) {
			totImposta += i.getTax();
		}

		return totImposta;
	}

	/**
	 * @return the estimate total for the client
	 * */
	@JsonIgnore
	public double getEstimateTotal() {
		return getTaxableTotal() + getCalculatedTax();
	}

}
