package jm.task.core.jdbc.dao;

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
        String[] hqls = {
                "CREATE SCHEMA IF NOT EXISTS `user_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;",
                "USE `user_schema`;",
                "CREATE TABLE IF NOT EXISTS users (" +
                        "  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "  name VARCHAR(45) DEFAULT 'Unknown name'," +
                        "  last_name VARCHAR(45) DEFAULT 'Unknown lastName'," +
                        "  age TINYINT DEFAULT 0)" +
                        " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        };
        executeHql(hqls);
    }


    @Override
    public void dropUsersTable() {
        String hql = "DROP TABLE IF EXISTS `user_schema`.`users`;";
        executeHql(hql);
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
        String hql = "DELETE FROM Users";
        executeHql(hql);
    }



    // *** service ***
    private static void executeHql(String... hqls) {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();

            for (String hql: hqls) {
                session.createSQLQuery(hql).executeUpdate();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
