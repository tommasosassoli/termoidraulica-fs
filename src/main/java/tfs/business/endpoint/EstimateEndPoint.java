package tfs.business.endpoint;

import tfs.business.dao.daofactory.EstimateDaoFactory;
import tfs.business.model.estimate.Estimate;

import java.util.List;

public class EstimateEndPoint {

    public boolean addEstimate(Estimate e) {
        return EstimateDaoFactory.getDao().addEstimate(e);
    }

    public List<Estimate> getEstimateList() {
        return EstimateDaoFactory.getDao().getEstimateList();
    }

    public Estimate getEstimate(String id) {
        return EstimateDaoFactory.getDao().getEstimate(id);
    }

    public boolean updateEstimate(Estimate e) {
        return EstimateDaoFactory.getDao().updateEstimate(e);
    }

    public Estimate deleteEstimate(String id) {
        return EstimateDaoFactory.getDao().deleteEstimate(id);
    }
}
