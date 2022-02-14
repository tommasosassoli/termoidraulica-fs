package tfs.business.model.tax;

public class TaxRate {
	private double taxRate;
	private String description = null;
	
	public TaxRate(double taxRate, String description) {
		this.taxRate = taxRate;
		this.description = description;
	}

	public TaxRate(TaxRate t) {
		this.taxRate = t.getTaxRateValue();
		this.description = t.getDescription();
	}

	public double getTaxRateValue() {
		return taxRate;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return taxRate + " - " + description;
	}

}
