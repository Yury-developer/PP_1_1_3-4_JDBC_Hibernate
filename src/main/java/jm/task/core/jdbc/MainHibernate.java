package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;


public class MainHibernate extends Script {
    public static void main(String[] args) {
        userDao = new UserDaoHibernateImpl();
        run();
    }
}
