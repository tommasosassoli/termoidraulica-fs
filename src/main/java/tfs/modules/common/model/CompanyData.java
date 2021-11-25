package tfs.modules.common.model;

import java.util.Properties;

public class CompanyData {
	private String companyName;
	private String phone;
	private String headquarter;
	private String municipality;
	private String cap;
	private String province;
	private String iban;
	private String piva;	//partita iva
	
	public CompanyData() {
		
	}
	
	public CompanyData(Properties prop) {
		setCompanyName(prop.getProperty("company_name"));
		setPhone(prop.getProperty("phone"));
		setHeadquarter(prop.getProperty("headquarter"));
		setMunicipality(prop.getProperty("municipality"));
		setCap(prop.getProperty("cap"));
		setProvince(prop.getProperty("province"));
		setIban(prop.getProperty("iban"));
		setPiva(prop.getProperty("partita_iva"));
		
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if (phone.length() > 15)
			phone = phone.substring(0,15);
		this.phone = phone;
	}

	public String getHeadquarter() {
		return headquarter;
	}

	public void setHeadquarter(String headquarter) {
		this.headquarter = headquarter;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		if (cap.length() > 5)
			cap = cap.substring(0,5);
		this.cap = cap;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if (province.length() > 2)
			province = province.substring(0,2);
		this.province = province;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		if (iban.length() > 27)
			iban = iban.substring(0,27);
		this.iban = iban;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		if (piva.length() > 11)
			piva = piva.substring(0,11);
		this.piva = piva;
	}
	
	public Properties exportInProperties() {
		Properties prop = new Properties();
		prop.put("company_name", companyName);
		prop.put("phone", phone);
		prop.put("headquarter", headquarter);
		prop.put("municipality", municipality);
		prop.put("cap", cap);
		prop.put("province", province);
		prop.put("iban", iban);
		prop.put("partita_iva", piva);
		
		return prop;
	}

}
