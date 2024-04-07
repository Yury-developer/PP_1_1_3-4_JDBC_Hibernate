package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserDaoJDBCImpl userDaoJDBC = new UserDaoJDBCImpl();

        userDaoJDBC.createUsersTable();

        userDaoJDBC.saveUser("Имя delete", "Фамилия delete", (byte) 1);
        userDaoJDBC.saveUser("Аня", "Анечкина", (byte) 11);
        userDaoJDBC.saveUser("Борис", "Бодич", (byte) 22);
        userDaoJDBC.saveUser("Вася", "Васечкин", (byte) 33);
        userDaoJDBC.getAllUsers().forEach(System.out::println);

        System.out.println();
        userDaoJDBC.removeUserById(1);
        userDaoJDBC.getAllUsers().forEach(System.out::println);

        System.out.println();
        userDaoJDBC.cleanUsersTable();
        userDaoJDBC.getAllUsers().forEach(System.out::println);

        userDaoJDBC.dropUsersTable();
    }
}
