package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

class Main {
    protected static UserDao userDao = new UserDaoHibernateImpl();
//    protected static UserDao userDao = new UserDaoJDBCImpl();


    public static void main(String[] args) {

        System.out.println("\n\t" + userDao.getClass().getSimpleName() + ": Создание таблицы");
        userDao.createUsersTable();

        System.out.println("\n\tСохранение пользователей в базу");
        userDao.saveUser("Имя_for_delete", "Фамилия_for_delete", (byte) 1);
        userDao.saveUser("Аня", "Анечкина", (byte) 11);
        userDao.saveUser("Борис", "Бодич", (byte) 22);
        userDao.saveUser("Вася", "Васечкин", (byte) 33);

        System.out.println("\n\tПолучение всех пользователей из базы и вывод на экран");
        userDao.getAllUsers().forEach(System.out::println);

        System.out.println("\n\tУдаление пользователя с идентификатором id=1 и вывод на экран");
        userDao.removeUserById(1);
        userDao.getAllUsers().forEach(System.out::println);

        System.out.println("\n\tОчистка таблицы пользователей и вывод на экран");
        userDao.cleanUsersTable();
        userDao.getAllUsers().forEach(System.out::println);

        System.out.println("\n\tУдаление таблицы");
        userDao.dropUsersTable();
    }
}
