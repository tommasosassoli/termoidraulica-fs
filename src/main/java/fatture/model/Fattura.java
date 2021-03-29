package fatture.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import fatture.management.GestioneAliquoteIva;
import fatture.management.GestioneClienti;

public class Fattura {
	@JsonProperty("id_fattura")
	private String id;
	
	@JsonProperty("id_cliente")
	private String IDcliente;
	
	@JsonIgnore
	private Cliente cliente;
	
	@JsonProperty("data_scadenza")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss") 
	private LocalDateTime dataScadenza;
	
	@JsonProperty("data_inserimento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy-HH:mm:ss") 
	private LocalDateTime dataInserimento;
	
	@JacksonXmlElementWrapper(localName = "lista_materiali")
	@JsonProperty("materiale")
	private final List<Materiale> materiali = new ArrayList<>();
	
	@JacksonXmlElementWrapper(localName = "lista_SAL")
	@JsonProperty("SAL")
	private final List<SAL> sal = new ArrayList<>();
	
	@JsonProperty("sconto_maggiorazione")
	private double scontoMaggiorazione;
	
	@JsonProperty("acconto")
	private double acconto;
	
	public Fattura() {
		
	}

	public Fattura(String id, Cliente cliente, LocalDateTime dataScadenza) {
		this.id = id;
		this.cliente = cliente;
		this.dataScadenza = dataScadenza;
		this.dataInserimento = LocalDateTime.now();
	}

	@JsonIgnore
	public String getId() {
		return this.id;
	}

	@JsonIgnore
	public LocalDateTime getDataScadenza() {
		return dataScadenza;
	}

	@JsonIgnore
	public void setDataScadenza(LocalDateTime dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	@JsonIgnore
	public LocalDateTime getDataInserimento() {
		return this.dataInserimento;
	}

	@JsonIgnore
	public String getFormatDataScadenza() {
		LocalDateTime scadenza = this.dataScadenza;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		return scadenza.format(formatter);
	}
	
	@JsonProperty("data_scadenza")
	private void setFormatDataScadenza(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
		this.dataScadenza = LocalDateTime.parse(data, formatter);
	}

	@JsonIgnore
	public String getFormatDataInserimento() {
		LocalDateTime scadenza = this.dataInserimento;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy - H : m : s");
		return scadenza.format(formatter);
	}
	
	@JsonProperty("data_inserimento")
	private void setFormatDatainserimento(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
		this.dataInserimento = LocalDateTime.parse(data, formatter);
	}

	@JsonIgnore
	public Cliente getCliente() {
		if (cliente == null)
			cliente = GestioneClienti.instance().getCliente(IDcliente);
		return cliente;
	}

	public void setCliente(String IDcliente) {
		this.IDcliente = IDcliente;
		cliente = null;
	}

	@JsonIgnore
	public List<Materiale> getMateriali() {
		return materiali;
	}

	public void overrideMateriali(List<Materiale> list) {
		materiali.clear();
		materiali.addAll(list);
	}

	@JsonIgnore
	public List<SAL> getListSal() {
		return sal;
	}

	public boolean overrideSal(List<SAL> sal) {
		this.sal.clear();
		return this.sal.addAll(sal);
	}

	@JsonIgnore
	public double getScontoMaggiorazione() {
		return scontoMaggiorazione;
	}

	@JsonIgnore
	public void setScontoMaggiorazione(double scontoMaggiorazione) {
		this.scontoMaggiorazione = scontoMaggiorazione;
	}

	@JsonIgnore
	public double getAcconto() {
		return acconto;
	}

	/**
	 * @param acconto must be positive*/
	public void setAcconto(double acconto) {
		this.acconto = acconto;
	}

	@JsonIgnore
	public List<Imponibile> getImponibili() {

		ArrayList<Imponibile> list = new ArrayList<Imponibile>();

		list.add(new Imponibile(GestioneAliquoteIva.instance().getFromValue(22)));
		list.add(new Imponibile(GestioneAliquoteIva.instance().getFromValue(10)));
		list.add(new Imponibile(GestioneAliquoteIva.instance().getFromValue(4)));

		for (Materiale m : materiali) {
			for (Imponibile i : list) {
				if (m.getAliquotaIva().getAliquota() == i.getIva().getAliquota()) {
					i.sumImponibile(m.getSubtotale());
				}
			}
		}

		ArrayList<Imponibile> imponibili = new ArrayList<Imponibile>();

		for (Imponibile i : list) {
			if (i.getImponibile() != 0)
				imponibili.add(i);
		}

		return imponibili;
	}

	@JsonIgnore
	public double getSubTotaleFattura() {
		double res = 0;
		for (Materiale m : materiali)
			res += m.getSubtotale();

		return res;
	}
	
	@JsonIgnore
	public double getTotaleImponibile() {
		double res = getSubTotaleFattura() - Math.abs(getAcconto());
		return (res > 0) ? res : 0;
	}

	@JsonIgnore
	public double getTotaleImposta() {
		double totImposta = 0;
		for (Imponibile i : getImponibili()) {
			totImposta += i.getImposta();
		}

		return totImposta;
	}

	@JsonIgnore
	public double getTotaleFattura() {
		return getTotaleImponibile() + getTotaleImposta();
	}

}
