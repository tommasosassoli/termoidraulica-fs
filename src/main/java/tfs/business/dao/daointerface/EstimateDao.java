package tfs.business.dao.daointerface;

import tfs.business.model.estimate.Estimate;

import java.util.List;

public interface EstimateDao extends DaoInterface {
    boolean addEstimate(Estimate e);
    List<Estimate> getEstimateList();
    Estimate getEstimate(String id);
    boolean updateEstimate(Estimate e);
    Estimate deleteEstimate(String id);
}
