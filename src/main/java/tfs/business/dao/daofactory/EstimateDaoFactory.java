package tfs.business.dao.daofactory;

import tfs.business.dao.daoimplementation.RDBEstimateDao;
import tfs.business.dao.daointerface.EstimateDao;

public class EstimateDaoFactory {
    public static EstimateDao getDao() {
        return new RDBEstimateDao();
    }
}
