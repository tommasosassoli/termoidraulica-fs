package fatture.model;

import java.util.Properties;

public class DatiAzienda {
	private String nomeAzienda;
	private String telefono;
	private String sede;
	private String comune;
	private String cap;
	private String provincia;
	private String iban;
	private String piva;
	
	public DatiAzienda() {
		
	}
	
	public DatiAzienda(Properties prop) {
		this.nomeAzienda = prop.getProperty("nome_azienda");
		this.telefono = prop.getProperty("telefono");
		this.sede = prop.getProperty("sede");
		this.comune = prop.getProperty("comune");
		this.cap = prop.getProperty("cap");
		this.provincia = prop.getProperty("provincia");
		this.iban = prop.getProperty("iban");
		this.piva = prop.getProperty("partita_iva");
		
	}
	
	public String getNomeAzienda() {
		return nomeAzienda;
	}

	public void setNomeAzienda(String nomeAzienda) {
		this.nomeAzienda = nomeAzienda;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getsede() {
		return sede;
	}

	public void setsede(String sede) {
		this.sede = sede;
	}

	public String getCitta() {
		return comune;
	}

	public void setCitta(String citta) {
		this.comune = citta;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}
	
	public Properties exportInProperties() {
		Properties prop = new Properties();
		prop.put("nome_azienda", nomeAzienda);
		prop.put("iban", iban);
		prop.put("sede", sede);
		prop.put("comune", comune);
		prop.put("provincia", provincia);
		prop.put("cap", cap);
		prop.put("telefono", telefono);
		prop.put("partita_iva", piva);
		
		return prop;
	}

}
