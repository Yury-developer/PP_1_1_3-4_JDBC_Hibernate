package jm.task.core.jdbc.util;

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

    public static final Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("src/main/resources/logger_config.properties")){
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
        LOGGER = Logger.getLogger(Util.class.getName()); // I use it for debugging
        LOGGER.setLevel(Level.ALL);
    }

//    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // "org.h2.Driver"
//    private static final String DB_URL = "jdbc:mysql://localhost:3306?serverTimezone=Europe/Moscow&useSSL=false"; // указываю, что SSL не будет использоваться и что часовым поясом будет московский часовой пояс
////    private static final String DB_URL = "jdbc:mysql://localhost:3306"; // "jdbc:h2:~/test"
//    private static final String DB_USER_NAME = "user";
//    private static final String DB_USER_PASSWORD = "1234";

    public static Connection getConnection() {
        if (connection == null) {
            Properties properties = new Properties();

            try (Reader reader = new InputStreamReader(
                    new FileInputStream("src/main/resources/database.properties"), StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                LOGGER.warning("IOException: \n" + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
//            try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("database.properties")) {
//                properties.load(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

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
                LOGGER.warning("Connection ERROR (ClassNotFoundException or SQLException): \n"
                        + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
            LOGGER.info("Connection is created!");
        }
        LOGGER.info("Finished;");
        return connection;
    }


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
            LOGGER.warning("SQLException \n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return result;
    }
}
