package tfs.modules.estimates.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {
	
	@JsonProperty("client_id")
	private String id;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("surname")
	private String surname;
	
	@JsonProperty("residence")
	private String residence;
	
	@JsonProperty("municipality")
	private String municipality;
	
	@JsonProperty("province")
	private String province;
	
	@JsonProperty("cap")
	private String cap;
	
	@JsonProperty("fiscal_code")
	private String cf;
	
	@JsonProperty("extra_notes")
	private String notes;
	
	public Client() {
		
	}

	public Client(String id, String name, String surname, String residence, String municipality, String province,
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

	@JsonIgnore
	public String getId() {
		return this.id;
	}

	@JsonIgnore
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@JsonIgnore
	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	@JsonIgnore
	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	@JsonIgnore
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if (province.length() > 2)
			province = province.substring(0,2);
		this.province = province;
	}

	@JsonIgnore
	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		if (cap.length() > 5)
			cap = cap.substring(0,5);
		this.cap = cap;
	}

	@JsonIgnore
	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		if (cf.length() > 16)
			cf = cf.substring(0,16);
		this.cf = cf;
	}

	@JsonIgnore
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@JsonIgnore
	public String getSurnameName() {
		return toString();
	}

	public String toString() {
		return surname + " " + name;
	}

}
