package tfs.business.model.tax;

public class Taxable {
	private double taxable = 0;
	private final TaxRate taxRate;
	
	public Taxable(double taxable, TaxRate taxRate) {
		this.taxable = taxable;
		this.taxRate = taxRate;
	}

	public double getTaxable() {
		return taxable;
	}

	public void setTaxable(double taxable) {
		if (taxable < 0)
			taxable = 0;
		this.taxable = taxable;
	}

	public TaxRate getTaxRate() {
		return taxRate;
	}
	
	public double getTax() {
		return (taxable * taxRate.getTaxRateValue()) / 100;
	}

}
