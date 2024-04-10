package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;

public class MainHibernate {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoHibernateImpl();

        // Создание таблицы пользователей
        userDao.createUsersTable();


        userDao.saveUser("Аня", "Анечкина", (byte) 11);
        userDao.saveUser("Борис", "Бодич", (byte) 22);
        userDao.saveUser("Вася", "Васечкин", (byte) 33);

        userDao.getAllUsers().forEach(s -> System.out.println(s.toString()));


        // Сохранение пользователя
        userDao.saveUser("John", "Doe", (byte) 30);

        // Получение всех пользователей и вывод на экран
        userDao.getAllUsers().forEach(System.out::println);

        // Удаление пользователя с идентификатором 1
        userDao.removeUserById(1);

        // Очистка таблицы пользователей
        userDao.cleanUsersTable();
    }
}
