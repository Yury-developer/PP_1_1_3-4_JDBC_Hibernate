package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;


public class MainJDBC extends Script {
    public static void main(String[] args) {
        userDao = new UserDaoJDBCImpl();
        run();
    }
}
