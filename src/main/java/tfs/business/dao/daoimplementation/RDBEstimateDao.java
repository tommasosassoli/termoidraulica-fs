package tfs.business.dao.daoimplementation;

import tfs.business.dao.daofactory.CustomerDaoFactory;
import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.data.RDBConnection;
import tfs.business.model.customer.Customer;
import tfs.business.model.estimate.Estimate;
import tfs.business.dao.daointerface.EstimateDao;
import tfs.business.model.estimate.Item;
import tfs.business.model.estimate.ItemGroup;
import tfs.business.model.tax.TaxRate;
import tfs.service.LogService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RDBEstimateDao implements EstimateDao {
    @Override
    public boolean addEstimate(Estimate e) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            try {
                return insertEstimateWithItemGroupsAndItem(conn, e);
            } catch (SQLException g) {
                LogService.error(this.getClass(), "Error during estimate insertion", true, g);
            } finally {
                try {
                    conn.close();
                } catch (SQLException f) {
                    LogService.error(this.getClass(), "Error during closing connection", false, f);
                }
            }
        }
        return false;
    }

    @Override
    public List<Estimate> getEstimateList() {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT e.estimateId, c.customerName, c.customerSurname, e.expirationDate, e.insertDate " +
                    "FROM Estimate e JOIN Customer c ON c.id = e.customerId ORDER BY e.insertDate DESC;";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<Estimate> list = new ArrayList<>();
                while (rs.next()) {
                    // customer
                    Customer c = new Customer();
                    c.setName(rs.getString(2));
                    c.setSurname(rs.getString(3));

                    // estimate
                    Estimate e = createEstimate(rs.getInt(1), c,
                            rs.getTimestamp(4), rs.getTimestamp(5));
                    list.add(e);
                }
                return list;

            } catch (SQLException g) {
                LogService.error(this.getClass(), "Error during estimate list select", true, g);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LogService.error(this.getClass(), "Error during closing connection", false, e);
                }
            }
        }
        return null;
    }

    @Override
    public Estimate getEstimate(String id) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT e.estimateId, e.customerId, e.expirationDate, e.insertDate, e.deposit " +
                    "FROM Estimate e WHERE estimateId = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(id));

                ResultSet rs = pstmt.executeQuery();
                if (!rs.next())
                    return null;

                Estimate e = createEstimate(rs.getInt(1),
                        rs.getInt(2),
                        rs.getTimestamp(3),
                        rs.getTimestamp(4));
                e.setDeposit(rs.getFloat(5));

                e.overrideItemGroups(resolveItemGroupsAndItem(conn, id));
                return e;
            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during estimate select", true, e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LogService.error(this.getClass(), "Error during closing connection", false, e);
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateEstimate(Estimate e) {
        if (e.getId() != null && !e.getId().isEmpty()) {
            Connection conn = RDBConnection.getInstance().getConnection();
            if (conn != null) {
                try {
                    conn.setAutoCommit(false);
                    int estimateId = Integer.parseInt(e.getId());
                    boolean ok = delete(conn, estimateId);
                    if (ok) {
                        return insertEstimateWithItemGroupsAndItem(conn, e);
                    } else
                        conn.rollback();
                } catch (SQLException ex) {
                    LogService.error(this.getClass(), "Error during estimate update", true, ex);
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException f) {
                        LogService.error(this.getClass(), "Error during closing connection", false, f);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Estimate deleteEstimate(String id) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            Estimate e = getEstimate(id);
            if (e != null) {
                try {
                    return delete(conn, Integer.parseInt(id)) ? e : null;
                } catch (SQLException ex) {
                    LogService.error(this.getClass(), "Error during estimate delete", true, ex);
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException f) {
                        LogService.error(this.getClass(), "Error during closing connection", false, f);
                    }
                }
            }
        }
        return null;
    }

    // HElPER FUNCTION

    private boolean delete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Estimate WHERE estimateId = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int ok = pstmt.executeUpdate();

        return ok != 0;
    }

    private boolean insertEstimateWithItemGroupsAndItem(Connection conn, Estimate e) throws SQLException {
        // Transaction Begin
        conn.setAutoCommit(false);

        // Estimate
        String sqlE;
        PreparedStatement stmE;
        if (e.getId() == null || e.getId().isEmpty()) {
            sqlE = "INSERT INTO Estimate " +
                    "(customerId, expirationDate, insertDate, deposit) " +
                    "VALUES (?,?,?,?) RETURNING estimateId";

            stmE = conn.prepareStatement(sqlE);

            stmE.setInt(1, Integer.parseInt(e.getCustomerId()));
            stmE.setTimestamp(2, Timestamp.valueOf(e.getExpirationDate()));
            stmE.setTimestamp(3, Timestamp.valueOf(e.getInsertDate()));
            stmE.setFloat(4, (float) e.getDeposit());

        } else {
            sqlE = "INSERT INTO Estimate " +
                    "(estimateId, customerId, expirationDate, insertDate, deposit) " +
                    "VALUES (?,?,?,?,?) RETURNING estimateId";

            stmE = conn.prepareStatement(sqlE);

            stmE.setInt(1, Integer.parseInt(e.getId()));
            stmE.setInt(2, Integer.parseInt(e.getCustomerId()));
            stmE.setTimestamp(3, Timestamp.valueOf(e.getExpirationDate()));
            stmE.setTimestamp(4, Timestamp.valueOf(e.getInsertDate()));
            stmE.setFloat(5, (float) e.getDeposit());
        }

        stmE.execute();
        ResultSet rsE = stmE.getResultSet();
        if (!rsE.next()) {
            conn.rollback();
            throw new SQLException("Rollback: Cannot insert Estimate in Estimate table correctly");
        }
        int estimateId = rsE.getInt(1);

        String sqlIg = "INSERT INTO ItemGroup (itemGroupNum, estimateId, description) VALUES ";
        String sqlI = "INSERT INTO Item (itemNum, itemGroupNum, estimateId, description, um, qt, price, discount, taxRate) VALUES ";

        boolean firstIg = true;
        boolean firstI = true;

        List<ItemGroup> listIg = e.getItemGroups();
        for (int i = 0; i < listIg.size(); i++) {
            sqlIg += (firstIg ? "(" : ", (");
            firstIg = false;
            sqlIg += i + "," + estimateId + ",'" + escape(listIg.get(i).getDescription()) + "') ";

            List<Item> listI = listIg.get(i).getItems();
            for (int j = 0; j < listI.size(); j++) {
                Item item = listI.get(j);

                sqlI += (firstI ? "(" : ", (");
                firstI = false;
                sqlI += j + "," + i + "," + estimateId + ",'" + escape(item.getDescription()) + "','" + escape(item.getUm()) + "'," + item.getQt() +
                        "," + item.getPrice() + "," + item.getDiscount() + "," + item.getTaxRate().getTaxRateValue() + ") ";
            }
        }

        if (!firstIg) {
            PreparedStatement stmIg = conn.prepareStatement(sqlIg);
            int ok = stmIg.executeUpdate();

            if (ok == 0) {
                conn.rollback();
                throw new SQLException("Rollback: Cannot insert ItemGroup in ItemGroup table correctly");
            }
        }

        if (!firstI) {
            PreparedStatement stmI = conn.prepareStatement(sqlI);
            int ok = stmI.executeUpdate();

            if (ok == 0) {
                conn.rollback();
                throw new SQLException("Rollback: Cannot insert Item in Item table correctly");
            }
        }

        // Transaction End
        conn.commit();
        return true;
    }

    private List<ItemGroup> resolveItemGroupsAndItem(Connection conn, String estimateId) throws SQLException {
        String sql = "SELECT ig.itemGroupNum, ig.description, " +
                "i.description, i.um, i.qt, i.price, i.discount, i.taxRate " +
                "FROM ItemGroup ig " +
                "LEFT JOIN Item i ON (i.itemGroupNum = ig.itemGroupNum AND i.estimateId = ig.estimateId) " +
                "WHERE ig.estimateId = ? " +
                "ORDER BY ig.itemGroupNum, i.itemNum";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(estimateId));

        ResultSet rs = pstmt.executeQuery();
        HashMap<Integer, ItemGroup> igList = new HashMap<>();

        while (rs.next()) {
            int igId = rs.getInt(1);

            if (!igList.containsKey(igId)) {
                ItemGroup ig = new ItemGroup();
                ig.setDescription(rs.getString(2));
                igList.put(igId, ig);
            }
            // build item
            Item i = new Item();
            i.setDescription(rs.getString(3));
            i.setUm(rs.getString(4));
            i.setQt(rs.getFloat(5));
            i.setPrice(rs.getFloat(6));
            i.setDiscount(rs.getFloat(7));

            TaxRate tax = TaxRateDaoFactory.getDao().getTaxRate(rs.getInt(8));
            i.setTaxRate(tax);

            if (!i.isEmpty())
                igList.get(igId).addItem(i);
        }
        return new ArrayList<>(igList.values());
    }

    private Estimate createEstimate(int id, int cId, Timestamp tsExp, Timestamp tsIn) {
        return createEstimate(id, CustomerDaoFactory.getDao().getCustomer(String.valueOf(cId)),
                tsExp, tsIn);
    }

    private Estimate createEstimate(int id, Customer c, Timestamp tsExp, Timestamp tsIn) {
        LocalDateTime ldtExp = (tsExp != null) ? tsExp.toLocalDateTime() : null;
        LocalDateTime ldtIn = (tsIn != null) ? tsIn.toLocalDateTime() : null;

        return new Estimate(Integer.toString(id), c, ldtExp, ldtIn);
    }

    private String escape(String s) {
        return s.replace("\'", "''");
    }
}
