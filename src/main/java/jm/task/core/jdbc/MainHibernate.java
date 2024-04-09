package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;

public class MainHibernate {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            UserDao userDao = new UserDaoHibernateImpl(sessionFactory);

            // Создание таблицы пользователей
            userDao.createUsersTable();

            // Сохранение пользователя
            userDao.saveUser("John", "Doe", (byte) 30);

            // Получение всех пользователей и вывод на экран
            userDao.getAllUsers().forEach(System.out::println);

            // Удаление пользователя с идентификатором 1
            userDao.removeUserById(1);

            // Очистка таблицы пользователей
            userDao.cleanUsersTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
