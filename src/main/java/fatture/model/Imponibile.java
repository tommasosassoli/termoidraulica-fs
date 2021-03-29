package fatture.model;

public class Imponibile {
	private double imponibile = 0;
	private final AliquotaIva iva;
	
	public Imponibile(AliquotaIva iva) {
		this.iva = iva;
	}

	public double getImponibile() {
		return imponibile;
	}
	
	public void sumImponibile(double importo) {
		this.imponibile += importo;
	}

	public AliquotaIva getIva() {
		return iva;
	}
	
	public double getImposta() {
		return (imponibile * iva.getAliquota()) / 100;
	}

}
