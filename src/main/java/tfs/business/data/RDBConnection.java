package tfs.business.data;

import tfs.business.dao.daoimplementation.PropertiesCompanyDataDao;
import tfs.business.resolvers.FileResolver;
import tfs.service.LogService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class RDBConnection {
    private static RDBConnection rdbConnection;
    private Properties connectionProp;

    private RDBConnection() {
    }

    public static RDBConnection getInstance() {
        if (rdbConnection == null)
            rdbConnection = new RDBConnection();
        return rdbConnection;
    }

    public Connection getConnection() {
        try {
            if (connectionProp == null)
                connectionProp = loadPropertiesFile(FileResolver.DATABASE);

            String url = buildConnectionString(connectionProp);

            Connection conn = DriverManager.getConnection(url);
            LogService.info(RDBConnection.class, "Connection to database established");
            return conn;

        } catch (IOException | IllegalArgumentException e) {
            LogService.error(PropertiesCompanyDataDao.class, "Cannot load the connection file", true, e);
        } catch (SQLException e) {
            LogService.error(RDBConnection.class, "Couldn't connect to database: ", true, e);
        }
        return null;
    }

    private String buildConnectionString(Properties p) {
        return  "jdbc:postgresql://" + p.getProperty("host") +
                ":" + p.getProperty("port") +
                "/" + p.getProperty("db_name") +
                "?user=" + p.getProperty("user") +
                "&password=" + p.getProperty("password");
    }

    private Properties loadPropertiesFile(FileResolver file) throws IllegalArgumentException, IOException {
        if (file == null || file.type() != 3)
            throw new IllegalArgumentException("The file passed must is a connection file");

        PropertiesFileHandler pfile = new PropertiesFileHandler(file);
        return pfile.loadProperties();
    }
}
