package tfs.business.model.estimate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import tfs.business.model.customer.Customer;
import tfs.business.model.tax.TaxRate;
import tfs.business.dao.daofactory.CustomerDaoFactory;
import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.model.tax.Taxable;

public class Estimate {
	private String id;
	private String clientId;
	private Customer customer;
	private LocalDateTime expirationDate;
	private LocalDateTime insertDate;
	private final List<ItemGroup> itemGroups = new ArrayList<>();
	private double deposit;
	
	public Estimate() {
		this(null, null, null);
	}

	public Estimate(String id, Customer customer, LocalDateTime expirationDate) {
		this(id, customer, expirationDate, LocalDateTime.now());
	}

	public Estimate(String id, Customer customer, LocalDateTime expirationDate, LocalDateTime insertDate) {
		this.id = id;
		if (customer != null) {
			this.customer = new Customer(customer);
			this.clientId = customer.getId();
		}
		this.expirationDate = expirationDate;
		this.insertDate = insertDate;
	}

	public String getId() {
		return this.id;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public LocalDateTime getInsertDate() {
		return insertDate;
	}

	public String getFormatExpiringDate() {
		LocalDateTime exp = this.expirationDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		return exp.format(formatter);
	}

	public String getFormatInsertDate() {
		LocalDateTime exp = this.insertDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy - H : m : s");
		return exp.format(formatter);
	}

	public Customer getCustomer() {
		if (customer == null)
			customer = CustomerDaoFactory.getDao().getCustomer(clientId);
		return new Customer(customer);
	}

	public String getCustomerId() {
		return clientId;
	}

	public void setCustomer(String clientID) {
		this.clientId = clientID;
		customer = null;
	}

	public List<ItemGroup> getItemGroups() {
		ArrayList<ItemGroup> l = new ArrayList<>();
		for (ItemGroup g : itemGroups)
			l.add(new ItemGroup(g));
		return l;
	}

	public void addItemGroup(ItemGroup g) {
		itemGroups.add(new ItemGroup(g));
	}

	public void overrideItemGroups(List<ItemGroup> list) {
		itemGroups.clear();
		for (ItemGroup g : list)
			itemGroups.add(new ItemGroup(g));
	}

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

	public List<Taxable> getTaxableList() {
		List<TaxRate> taxRates = TaxRateDaoFactory.getDao().getTaxRatesList();
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
	public double getEstimateSubtotal() {
		double res = 0;
		for (ItemGroup m : itemGroups)
			res += m.getItemGroupSubtotal();

		return res;
	}
	
	/**
	 * @return estimateSubtotal - deposit 
	 * */
	public double getTaxableTotal() {
		double res = getEstimateSubtotal() - getDeposit();
		return (res > 0) ? res : 0;
	}

	/**
	 * @return the calculated tax
	 * */
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
	public double getEstimateTotal() {
		return getTaxableTotal() + getCalculatedTax();
	}

}
