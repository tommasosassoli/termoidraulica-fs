package tfs.business.dao.daoimplementation;

import tfs.business.dao.daofactory.TaxRateDaoFactory;
import tfs.business.data.RDBConnection;
import tfs.business.model.customer.Customer;
import tfs.business.model.receipt.Receipt;
import tfs.business.dao.daointerface.ReceiptDao;
import tfs.business.model.receipt.Riba;
import tfs.business.model.tax.TaxRate;
import tfs.service.LogService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RDBReceiptDao implements ReceiptDao {
    @Override
    public boolean addReceipt(Receipt r) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            try {
                return insertReceiptWithRiba(conn, r);
            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during receipt insert", true, e);
            }
        }
        return false;
    }

    @Override
    public List<Receipt> getReceiptList() {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT id, foreignId, description, date FROM Receipt ORDER BY date DESC;";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<Receipt> list = new ArrayList<>();
                while (rs.next()) {
                    Receipt r = createReceipt(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getTimestamp(4), null);
                    list.add(r);
                }
                return list;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during receipt list select", true, e);
            }
        }
        return null;
    }

    @Override
    public Receipt getReceipt(String id) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT id, foreignId, description, date, taxRate FROM Receipt WHERE id = ?";
            int receiptId = Integer.parseInt(id);

            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, receiptId);
                ResultSet rs = stmt.executeQuery();

                if (!rs.next())
                    return null;

                Receipt r = createReceipt(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getTimestamp(4),
                        TaxRateDaoFactory.getDao().getTaxRate(rs.getInt(5)));

                List<Riba> listRiba = resolveRiba(conn, receiptId);
                for (Riba k : listRiba)
                    r.addRiba(k);

                return r;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during receipt select", true, e);
            }
        }
        return null;
    }

    @Override
    public boolean updateReceipt(Receipt r) {
        if (r.getId() != null && !r.getId().isEmpty()) {
            Connection conn = RDBConnection.getInstance().getConnection();
            if (conn != null) {
                try {
                    int receiptId = Integer.parseInt(r.getId());
                    boolean ok = delete(conn, receiptId);
                    if (ok) {
                        return insertReceiptWithRiba(conn, r);
                    }
                } catch (SQLException ex) {
                    LogService.error(this.getClass(), "Error during receipt update", true, ex);
                }
            }
        }
        return false;
    }

    @Override
    public Receipt deleteReceipt(String id) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            Receipt r = getReceipt(id);
            if (r != null) {
                try {
                    return delete(conn, Integer.parseInt(id)) ? r : null;
                } catch (SQLException ex) {
                    LogService.error(this.getClass(), "Error during receipt delete", true, ex);
                }
            }
        }
        return null;
    }

    // HELPER

    private boolean delete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Receipt WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int ok = pstmt.executeUpdate();

        return ok != 0;
    }

    private boolean insertReceiptWithRiba(Connection conn, Receipt r) throws SQLException {
        // Transaction Begin
        conn.setAutoCommit(false);

        // get receipt next id
        String sqlR;
        PreparedStatement stmR;
        if (r.getId() == null || r.getId().isEmpty()) {
            sqlR = "INSERT INTO Receipt (foreignId, description, date, taxRate) " +
                    "VALUES (?,?,?,?) RETURNING id;";

            stmR = conn.prepareStatement(sqlR);
            stmR.setString(1, r.getForeignId());
            stmR.setString(2, r.getDescription());
            Timestamp ts = (r.getDate() != null) ? Timestamp.valueOf(r.getDate()) : null;
            stmR.setTimestamp(3, ts);
            stmR.setInt(4, (int) r.getTaxRate().getTaxRateValue());

        } else {
            sqlR = "INSERT INTO Receipt (id, foreignId, description, date, taxRate) " +
                    "VALUES (?,?,?,?,?)  RETURNING id;";

            stmR = conn.prepareStatement(sqlR);
            stmR.setInt(1, Integer.parseInt(r.getId()));
            stmR.setString(2, r.getForeignId());
            stmR.setString(3, r.getDescription());
            Timestamp ts = (r.getDate() != null) ? Timestamp.valueOf(r.getDate()) : null;
            stmR.setTimestamp(4, ts);
            stmR.setInt(5, (int) r.getTaxRate().getTaxRateValue());
        }



        stmR.execute();
        ResultSet rsR = stmR.getResultSet();
        if (!rsR.next()) {
            conn.rollback();
            throw new SQLException("Rollback: Cannot insert Receipt in Receipt table correctly");
        }
        int receiptId = rsR.getInt(1);

        // Riba
        if (!r.getRibaList().isEmpty()) {
            boolean first = true;
            int i = 0;
            String sqlRi = "INSERT INTO Riba (num, receipt, amount, expireDate, paid) VALUES ";
            for (Riba ri : r.getRibaList()) {
                sqlRi += (first ? "(" : ", (");
                first = false;
                sqlRi += i + "," + receiptId + "," + ri.getAmount() + ",'" + ri.getExpireDate() + "'," + ri.isPaid() + ") ";
                i++;
            }
            PreparedStatement stmRi = conn.prepareStatement(sqlRi);
            int okRi = stmRi.executeUpdate();

            if (okRi == 0) {
                conn.rollback();
                throw new SQLException("Rollback: Cannot insert Riba in Riba table correctly");
            }
        }
        // Transaction End
        conn.commit();
        return true;
    }

    private List<Riba> resolveRiba(Connection conn, int id) throws SQLException{
        String sql = "SELECT amount, expireDate, paid FROM Riba WHERE receipt = ? ORDER BY num;";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();

        List<Riba> list = new ArrayList<>();
        while (rs.next()) {
            Riba r = new Riba();
            r.setAmount(rs.getFloat(1));
            Timestamp ts = rs.getTimestamp(2);
            LocalDateTime ldt = (ts != null) ? ts.toLocalDateTime() : null;
            r.setExpireDate(ldt);
            r.setPaid(rs.getBoolean(3));

            list.add(r);
        }
        return list;
    }

    private Receipt createReceipt(int id, String foreignId, String description, Timestamp date, TaxRate tax) {
        LocalDateTime ldt = (date != null) ? date.toLocalDateTime() : null;
        Receipt r = new Receipt(
                foreignId,
                description,
                ldt,
                tax
        );
        r.setId(Integer.toString(id));
        return r;
    }
}
