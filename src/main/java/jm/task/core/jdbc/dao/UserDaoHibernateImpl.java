package jm.task.core.jdbc.dao;

import java.util.List;

import org.hibernate.*;
import org.hibernate.query.Query;
import jm.task.core.jdbc.util.Util;
import jm.task.core.jdbc.model.User;


public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory SESSION_FACTORY = Util.getSessionFactory();


    public UserDaoHibernateImpl() {
        // NOP
    }


    // Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl должны быть реализованы с помощью SQL.
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


    // Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl должны быть реализованы с помощью SQL.
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
        Criteria criteria = SESSION_FACTORY.openSession().createCriteria(User.class);
        List<User> users = criteria.list();
        return users;
    }


    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            String sql = "TRUNCATE TABLE users";
            Query query = session.createNativeQuery(sql);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
