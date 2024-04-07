package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = Util.getConnection();

    public static final Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("src/main/resources/logger_config.properties")){
            LogManager.getLogManager().readConfiguration(ins);
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
        LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName()); // I use it for debugging
        LOGGER.setLevel(Level.ALL);
    }


    public UserDaoJDBCImpl() {
        LOGGER.info("Create UserDaoJDBCImpl is finished;");
    }


    public void createUsersTable() {
        String sql1 = "CREATE SCHEMA IF NOT EXISTS `user_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;"; // создать базу
        String sql2 = "USE `user_schema`;"; // использовать
        String sql3 = "CREATE TABLE IF NOT EXISTS `user_schema`.`users` (\n" +
                "  `ID` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `NAME` VARCHAR(45) NULL,\n" +
                "  `LAST_NAME` VARCHAR(45) NULL,\n" +
                "  `AGE` TINYINT NULL,\n" + // Целое число от -128 до 127 (без знака от 0 до 255). Размер хранения 1 байт.
                "  PRIMARY KEY (`ID`));"; // создать таблицу
        executeSql(connection, sql1, sql2, sql3);
        LOGGER.info("Finished;");
    }


    public void dropUsersTable() {
        String sql1 = "DROP TABLE IF EXISTS `user_schema`.`users`;"; // delete a table
//        String sql2 = "DROP DATABASE IF EXISTS`user_schema`;"; // delete the database
        executeSql(connection, sql1);
        LOGGER.info("Finished;");
    }


    public void saveUser(String name, String lastName, byte age) {
        if (!Util.isExistsTable()) {
            LOGGER.info("An attempt to save the User to a non-existent table. Creating a table for the User;");
            createUsersTable();
        }

        String sql = "INSERT INTO USERS (NAME, LAST_NAME, AGE) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.warning("SQLException: \n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        LOGGER.info("Finished;");
    }


    public void removeUserById(long id) {
        String sql = "DELETE FROM `user_schema`.`users` WHERE ID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, (int) id);

            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.warning("SQLException: \n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        LOGGER.info("Finished;");
    }


    public List<User> getAllUsers() {
        String sql = "SELECT * FROM `user_schema`.`users`;";

        List<User> list = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setAge(resultSet.getByte("AGE"));

                list.add(user);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQLException: \n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        LOGGER.info("Finished;");
        return list;
    }


    public void cleanUsersTable() {
        String sql = "TRUNCATE `user_schema`.`users`;";
        executeSql(connection, sql);
        LOGGER.info("Finished;");
    }


    private static void executeSql(Connection connection, String... strings) {
        try (Statement statement = connection.createStatement()){
            for (String sql: strings) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQLException: \n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }
}
