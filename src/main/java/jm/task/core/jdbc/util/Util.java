package jm.task.core.jdbc.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;


public class Util {

    private static Connection connection;
    public static final Logger LOGGER = Logger.getLogger(Util.class.getName()); // I use it for debugging

//    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // "org.h2.Driver"
//    private static final String DB_URL = "jdbc:mysql://localhost:3306?serverTimezone=Europe/Moscow&useSSL=false"; // указываю, что SSL не будет использоваться и что часовым поясом будет московский часовой пояс
////    private static final String DB_URL = "jdbc:mysql://localhost:3306"; // "jdbc:h2:~/test"
//    private static final String DB_USER_NAME = "user";
//    private static final String DB_USER_PASSWORD = "1234";

    public static Connection getConnection() {
        if (connection == null) {
            Properties properties = new Properties();

            try (Reader reader = new InputStreamReader(new FileInputStream("src/main/resources/database.properties"), StandardCharsets.UTF_8)) {
                properties.load(reader);  // Загружаем свойства из файла
            } catch (IOException e) {
                e.printStackTrace();
            }
//        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("database.properties")) {
//            properties.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            LOGGER.warning("driver: " + driver);
            LOGGER.warning("url: " + url);
            LOGGER.warning("username: " + username);
            LOGGER.warning("password: " + password);

            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connection is 'ok'");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection ERROR");
                e.printStackTrace();
            }
        }
        return connection;
    }


    public boolean tableIsExists(String sql) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String tableName = "имя_таблицы";
            String query = "SELECT 1 FROM information_schema.tables "
                    + "WHERE table_schema = 'имя_базы_данных' AND table_name = '" + tableName + "'";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                // Таблица существует
                System.out.println("Таблица " + tableName + " существует.");
                // Теперь можно добавить строку в таблицу
            } else {
                // Таблица не существует
                System.out.println("Таблица " + tableName + " не существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
