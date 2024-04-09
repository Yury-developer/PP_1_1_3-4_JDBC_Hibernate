package jm.task.core.jdbc.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.lang.String.*;


public class Util {
    private static Connection connection;

    private static final Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("src/main/resources/logger_Util_config.properties")){
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
        LOGGER = Logger.getLogger(Util.class.getName());
        LOGGER.setLevel(Level.FINE);
    }


    public static Connection getConnection() {
        if (connection == null) {
            Properties properties = new Properties();

            try (Reader reader = new InputStreamReader(
                    new FileInputStream("src/main/resources/database.properties"), StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                LOGGER.warning("IOException: " + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }

            final String driver = properties.getProperty("driver");
            final String url = properties.getProperty("url");
            final String username = properties.getProperty("username");
            final String password = properties.getProperty("password");
            LOGGER.config(format("\tdriver:%s\n\turl:%s\n\tusername:%s\n\tpassword:%s", driver, url, username, password));

            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                LOGGER.warning("Connection ERROR (ClassNotFoundException or SQLException): "
                        + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
        }
        return connection;
    }


    // ***    Service methods    ***
    public static boolean isExistsTable() {
        boolean result = false;
        try (Statement statement = connection.createStatement()) {
            String tableSchema = "user_schema"; // имя DB
            String tableName = "users"; // имя табл.
            String query = "SELECT 1 FROM information_schema.tables "
                    + "WHERE table_schema = '" + tableSchema
                    + "' AND table_name = '" + tableName + "'";
            ResultSet resultSet = statement.executeQuery(query);
            result = resultSet.next();
        } catch (SQLException e) {
            LOGGER.warning("SQLException " + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return result;
    }
}
