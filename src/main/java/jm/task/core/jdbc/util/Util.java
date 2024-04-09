package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Util {
    private static Connection connection;

    private static final Logger LOGGER;
    static {
//        System.setProperty("java.util.logging.config.file","src/main/resources/logger_Util_config.properties");
        try(FileInputStream ins = new FileInputStream("src/main/resources/logger_Util_config.properties")){
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
        LOGGER = Logger.getLogger(Util.class.getName()); // I use it for debugging
        LOGGER.setLevel(Level.FINE);

        LOGGER.fine("test LOGGER, Level.FINE");
        LOGGER.info("test LOGGER, Level.INFO");
        LOGGER.severe("test LOGGER, Level.SEVERE");
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
            LOGGER.config("driver: " + driver);
            final String url = properties.getProperty("url");
            LOGGER.config("url: " + url);
            final String username = properties.getProperty("username");
            LOGGER.config("username: " + username);
            final String password = properties.getProperty("password");
            LOGGER.config("password: " + password);

            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                LOGGER.warning("Connection ERROR (ClassNotFoundException or SQLException): "
                        + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
            LOGGER.fine("Connection is created!");
        }
        LOGGER.info("Finished;");
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
