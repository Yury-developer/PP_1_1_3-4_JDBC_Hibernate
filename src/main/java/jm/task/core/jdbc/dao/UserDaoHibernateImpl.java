package jm.task.core.jdbc.dao;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
        executeHql("CREATE SCHEMA IF NOT EXISTS `user_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;",
                "USE `user_schema`;",
                "CREATE TABLE IF NOT EXISTS users (" +
                        "  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "  name VARCHAR(45) DEFAULT 'Unknown name'," +
                        "  last_name VARCHAR(45) DEFAULT 'Unknown lastName'," +
                        "  age TINYINT DEFAULT 0)" +
                        " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
    }

    @Override
    public void dropUsersTable() {
        executeHql("DROP TABLE IF EXISTS `user_schema`.`users`;");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInTransaction(session -> session.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        return executeAndReturn(session -> session.createQuery("FROM User", User.class).list());
    }

    @Override
    public void cleanUsersTable() {
        executeHql("DELETE FROM Users");
    }



    // *** services ***
    private static void executeHql(String... hqls) {
        executeInTransaction(session -> {
            for (String hql : hqls) {
                session.createSQLQuery(hql).executeUpdate();
            }
        });
    }


    private static void executeInTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    private static <T> T executeAndReturn(Function<Session, T> action) {
        Transaction transaction = null;
        T result = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            result = action.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }
}
