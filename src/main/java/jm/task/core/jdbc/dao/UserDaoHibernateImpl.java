package jm.task.core.jdbc.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import java.sql.Connection;
import java.util.Properties;


public class UserDaoHibernateImpl implements UserDao {

//    private Connection connection = HibernateUtil.getConnection();


    public UserDaoHibernateImpl() {
//        this.sessionFactory = sessionFactory;
    }


    @Override
    public void createUsersTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery(
                    "CREATE SCHEMA IF NOT EXISTS `user_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;"
            ).executeUpdate();

            session.createSQLQuery(
                    "USE `user_schema`;"
            ).executeUpdate();

            session.createSQLQuery(
//                    "CREATE TABLE IF NOT EXISTS `user_schema`.`users` (\n" +
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "  name VARCHAR(45) DEFAULT 'Unknown name'," +
                            "  last_name VARCHAR(45) DEFAULT 'Unknown lastName'," +
                            "  age TINYINT DEFAULT -1)" +
                            " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
            ).executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS `user_schema`.`users`;").executeUpdate();
            transaction.commit();
        }
    }


    // ok
    @Override
    public void saveUser(String name, String lastName, byte age) { // проверено   https://dzone.com/articles/hibernate-5-java-configuration-example
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction(); // start a transaction

            session.save(new User(name, lastName, age)); // save the User object

            transaction.commit(); // commit transaction
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    @Override
    public void removeUserById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        }
    }

    // ok
    @Override
    public List<User> getAllUsers() { // проверено   https://dzone.com/articles/hibernate-5-java-configuration-example
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }
}
