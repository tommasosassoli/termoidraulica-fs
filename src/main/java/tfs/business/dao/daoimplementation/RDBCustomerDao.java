package tfs.business.dao.daoimplementation;

import tfs.business.data.RDBConnection;
import tfs.business.model.customer.Customer;
import tfs.business.dao.daointerface.CustomerDao;
import tfs.service.LogService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RDBCustomerDao implements CustomerDao {
    @Override
    public boolean addCustomer(Customer c) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "INSERT INTO Customer " +
                    "(customerName, customerSurname, residence, municipality, province, cap, cf, notes) " +
                    "VALUES (?,?,?,?,?,?,?,?)";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                prepareCustomerStatement(pstmt, c);
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during customer insert", true, e);
            }
        }
        return false;
    }

    @Override
    public List<Customer> getCustomerList() {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT id, customerName, customerSurname FROM Customer " +
                    "ORDER BY customerSurname, customerName;";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<Customer> list = new ArrayList<>();
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setId(Integer.toString(rs.getInt(1)));
                    c.setName(rs.getString(2));
                    c.setSurname(rs.getString(3));
                    list.add(c);
                }
                return list;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during customer list select", true, e);
            }
        }
        return null;
    }

    @Override
    public Customer getCustomer(String id) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "SELECT id, customerName, customerSurname, residence, municipality, province, " +
                    "cap, cf, notes FROM Customer WHERE id = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(id));

                ResultSet rs = pstmt.executeQuery();
                if (!rs.next())
                    return null;

                Customer c = new Customer(
                        Integer.toString(rs.getInt(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9));
                return c;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during customer select", true, e);
            }
        }
        return null;
    }

    @Override
    public boolean updateCustomer(Customer c) {
        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "UPDATE Customer SET customerName = ?, customerSurname = ?, residence = ?, " +
                    "municipality = ?, province = ?, cap = ?, cf = ?, notes = ? WHERE id = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                prepareCustomerStatement(pstmt, c);
                pstmt.setInt(9, Integer.parseInt(c.getId()));
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during customer update", true, e);
            }
        }
        return false;
    }

    @Override
    public Customer deleteCustomer(String id) {
        Customer c = getCustomer(id);
        if (c == null)
            return null;

        Connection conn = RDBConnection.getInstance().getConnection();
        if (conn != null) {
            String sql = "DELETE FROM Customer WHERE id = ?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.executeUpdate();

                return c;
            } catch (SQLException e) {
                LogService.error(this.getClass(), "Error during customer delete", true, e);
            }
        }
        return null;
    }

    private void prepareCustomerStatement(PreparedStatement pstmt, Customer c) throws SQLException{
        pstmt.setString(1, c.getName());
        pstmt.setString(2, c.getSurname());
        pstmt.setString(3, c.getResidence());
        pstmt.setString(4, c.getMunicipality());
        pstmt.setString(5, c.getProvince());
        pstmt.setString(6, c.getCap());
        pstmt.setString(7, c.getCf());
        pstmt.setString(8, c.getNotes());
    }
}
