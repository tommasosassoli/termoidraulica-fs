package tfs.business.model.estimate;

import tfs.business.model.tax.TaxRate;
import tfs.business.dao.daofactory.TaxRateDaoFactory;

public class Item {
	private String description;
	private String um;
	private double qt;
	private double price;
	private TaxRate taxRate;
	private double discount;

	public Item() {
		this.qt = 0;
		this.taxRate = TaxRateDaoFactory.getDao().getTaxRate(22);
		this.discount = 0;
	}

	public String getDescription() {
		return (description != null) ? description : "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUm() {
		return (um != null) ? um : "";
	}

	public void setUm(String um) {
		this.um = um;
	}

	public double getQt() {
		return qt;
	}

	public void setQt(double qt) {
		if (qt < 0)
			qt = 0;
		this.qt = qt;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0)
			price = 0;
		this.price = price;
	}

	public TaxRate getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(TaxRate taxRate) {
		this.taxRate = taxRate;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		if (discount < 0)
			discount = 0;
		this.discount = discount;
	}

	public double getItemSubtotal() {
		double taxable = qt * price;
		double priceDiscount = (discount * taxable) / 100;
		return taxable - priceDiscount;	//discounted price
	}

	public boolean isEmpty() {
		return (getDescription().isEmpty() && getUm().isEmpty() && getQt() == 0 && getPrice() == 0);
	}
}
