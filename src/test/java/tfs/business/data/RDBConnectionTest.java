package tfs.business.data;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RDBConnectionTest {
    @Test
    void testConnection() {
        Connection c = RDBConnection.getInstance().getConnection();
        assertNotNull(c);
    }

}