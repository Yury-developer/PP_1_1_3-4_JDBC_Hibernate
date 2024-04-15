package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.*;

import static java.lang.String.*;


public class Util {
    private static Connection connection; // for JDBC

    private static final Logger LOGGER;
    static {
        try (FileInputStream ins = new FileInputStream("src/main/resources/logger_Util_config.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        LOGGER = Logger.getLogger(Util.class.getName());
        LOGGER.setLevel(Level.FINE);
    }

    private static SessionFactory sessionFactory; // for Hibernate


    public static SessionFactory getSessionFactory() { // for Hibernate
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                properties.put(Environment.URL, "jdbc:mysql://localhost:3306/user_schema?serverTimezone=Europe/Moscow&useSSL=false");
                properties.put(Environment.USER, "user");
                properties.put(Environment.PASS, "1234");
                properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                properties.put(Environment.SHOW_SQL, "true");
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                properties.put(Environment.HBM2DDL_AUTO, "update"); // "create-drop" -сначала создаст потом удалит /  "update" -обновлял существующую схему / "validate" -проверял ее целостность без изменений
                properties.put("hibernate.dialect.storage_engine", "innodb"); // Specify InnoDB storage engine
                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class); // add JPA entity mapping class

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder() //  holds the services that Hibernate will need during bootstrapping and at runtime.
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.err.println("\n\n\nException:   SessionFactory getSessionFactory\n\n");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static Connection getConnection() { // for JDBC
        if (connection == null) {

            Properties properties = new Properties();
            try (Reader reader = new InputStreamReader(
                    new FileInputStream("src/main/resources/database.properties"), StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                LOGGER.warning("IOException: " + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }

//            Properties properties = getProperties(); // you can put it in a separate method

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

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream in = Files
                .newInputStream(
                        Paths.get(Util.class.getResource("/database.properties").toURI())
                )
        ) {
            properties.load(in);
        } catch (IOException | URISyntaxException e) {
            LOGGER.warning("Database config file not found (IOException or URISyntaxException) "
                    + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return properties;
    }
}
