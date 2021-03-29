package fatture.model;

public class SAL {
	/**
	 * stato avanzamento lavori
	 */
	private String riferimentoFase;

	public SAL() {

	}

	public SAL(String riferimentoFase) {
		this.riferimentoFase = riferimentoFase;
	}

	public String getRiferimentoFase() {
		return riferimentoFase;
	}

	public void setRiferimentoFase(String riferimentoFase) {
		this.riferimentoFase = riferimentoFase;
	}

	public String toString() {
		return riferimentoFase;
	}

}
