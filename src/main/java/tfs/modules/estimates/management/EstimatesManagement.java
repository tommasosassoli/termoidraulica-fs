package tfs.modules.estimates.management;

import tfs.storage.DataManagement;
import tfs.modules.estimates.model.Client;
import tfs.modules.estimates.model.Estimate;
import tfs.modules.estimates.model.serialization.EstimatesSerialization;
import tfs.service.LogService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class EstimatesManagement {
	private static EstimatesManagement instance;
	private final HashMap<String, Estimate> estimates = new HashMap<>();

	private EstimatesManagement() {
		loadData();
	}

	public void loadData() {
		LogService.trace(EstimatesManagement.class, "Loading quotes data");

		try {
			estimates.clear();
			List<Estimate> lf = DataManagement.instance().getEstimates();

			if (lf != null)
				for (Estimate f : lf)
					estimates.put(f.getId(), f);

		} catch (IOException e) {
			LogService.info(this.getClass(), "File fatture is empty");
		}
	}

	public void commitData() {
		LogService.trace(EstimatesManagement.class, "Committing quotes data");

		try {
			DataManagement.instance().commitEstimates(new EstimatesSerialization(estimates.values()));

		} catch (IOException e) {
			LogService.error(this.getClass(), "Error during saving the fatture file", true, e);
		}
	}

	public void refreshData() {
		LogService.trace(EstimatesManagement.class, "Refreshing quotes data");

		commitData();
		loadData();
	}

	public String calcID() {
		return DataManagement.instance().getNextEstimateID();
	}

	public static EstimatesManagement instance() {
		if (instance == null)
			instance = new EstimatesManagement();
		return instance;
	}

	// ******************** functions

	public String addEstimate(Client c, LocalDateTime data) {
		String id = calcID();
		Estimate e = new Estimate(id, c, data);

		estimates.put(e.getId(), e);
		return id;
	}

	public Estimate getEstimate(String id) {
		return estimates.get(id);
	}

	public Collection<Estimate> getEstimates() {
		return estimates.values();
	}

	public Estimate deleteEstimate(String id) {
		return estimates.remove(id);
	}
}
