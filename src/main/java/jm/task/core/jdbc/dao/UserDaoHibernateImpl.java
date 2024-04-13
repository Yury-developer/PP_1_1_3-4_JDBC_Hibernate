package jm.task.core.jdbc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;


public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory SESSION_FACTORY = HibernateUtil.getSessionFactory();


    public UserDaoHibernateImpl() {
        // NOP
    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(
                    "CREATE SCHEMA IF NOT EXISTS `user_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;"
            ).executeUpdate();

            session.createSQLQuery(
                    "USE `user_schema`;"
            ).executeUpdate();

            session.createSQLQuery(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                            "  name VARCHAR(45) DEFAULT 'Unknown name'," +
                            "  last_name VARCHAR(45) DEFAULT 'Unknown lastName'," +
                            "  age TINYINT DEFAULT 0)" +
                            " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
            ).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS `user_schema`.`users`;").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
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
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            userList = session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }


    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


}
