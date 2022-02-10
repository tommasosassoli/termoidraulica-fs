package tfs.business.dao.daoimplementation;

import tfs.business.data.RDBConnection;
import tfs.business.model.tax.TaxRate;
import tfs.business.dao.daointerface.TaxRateDao;
import tfs.service.LogService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RDBTaxRateDao implements TaxRateDao {
    @Override
    public List<TaxRate> getTaxRatesList() {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT taxRate, description FROM TaxRate";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<TaxRate> list = new ArrayList<>();
                while (rs.next()) {
                    TaxRate r = new TaxRate(rs.getInt(1), rs.getString(2));
                    list.add(r);
                }
                return list;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during tax rate list select", true, e);
            }
        }
        return null;
    }

    @Override
    public TaxRate getTaxRate(int taxRate) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT taxRate, description FROM TaxRate WHERE taxRate = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, taxRate);

                ResultSet rs = pstmt.executeQuery();
                if (!rs.next())
                    return null;
                TaxRate r = new TaxRate(rs.getInt(1), rs.getString(2));
                return r;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during tax rate select", true, e);
            }
        }
        return null;
    }
}
