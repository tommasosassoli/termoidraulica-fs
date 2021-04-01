package tfs.estimates.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import tfs.estimates.management.logic.TaxRateManagement;

public class Item {
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("unity_of_measure")
	private String um;
	
	@JsonProperty("quantity")
	private double qt;

	@JsonProperty("price")
	private double price;
	
	@JsonProperty("tax_rate")
	private TaxRate taxRate;
	
	@JsonProperty("discount")
	private double discount;

	public Item() {
		this.qt = 0;
		this.taxRate = TaxRateManagement.instance().getFromValue(22);
		this.discount = 0;
	}

	@JsonIgnore
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	@JsonIgnore
	public double getQt() {
		return qt;
	}

	public void setQt(double qt) {
		if (qt < 0)
			qt = 0;
		this.qt = qt;
	}

	@JsonIgnore
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0)
			price = 0;
		this.price = price;
	}

	@JsonIgnore
	public TaxRate getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(TaxRate taxRate) {
		this.taxRate = taxRate;
	}

	@JsonIgnore
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		if (discount < 0)
			discount = 0;
		this.discount = discount;
	}

	@JsonIgnore
	public double getItemSubtotal() {
		double taxable = qt * price;
		double priceDiscount = (discount * taxable) / 100;
		return taxable - priceDiscount;	//discounted price
	}

}
