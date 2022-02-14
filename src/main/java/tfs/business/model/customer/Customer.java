package tfs.business.model.customer;

public class Customer {
	
	private String id = null;
	private String name;
	private String surname;
	private String residence;
	private String municipality;
	private String province;
	private String cap;
	private String cf;
	private String notes;
	
	public Customer() {
	}

	public Customer(Customer c) {
		this(c.getId(), c.getName(), c.getSurname(), c.getResidence(), c.getMunicipality(),
				c.getProvince(), c.getCap(), c.getCf(), c.getNotes());
	}

	public Customer(String id, String name, String surname, String residence, String municipality, String province,
					String cap, String cf, String notes) {

		this.id = id;
		this.name = name;
		this.surname = surname;
		this.residence = residence;
		this.municipality = municipality;
		this.province = province;
		this.cap = cap;
		this.cf = cf;
		this.notes = notes;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		if (this.id == null)
			this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if (province.length() > 2)
			province = province.substring(0,2);
		this.province = province;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		if (cap.length() > 5)
			cap = cap.substring(0,5);
		this.cap = cap;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		if (cf.length() > 16)
			cf = cf.substring(0,16);
		this.cf = cf;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSurnameName() {
		return toString();
	}

	public String toString() {
		return surname + " " + name;
	}

}
