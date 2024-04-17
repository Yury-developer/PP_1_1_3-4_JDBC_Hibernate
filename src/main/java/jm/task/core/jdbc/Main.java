package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

class Main {
    private static UserService userService = new UserServiceImpl();

    public static void main(String[] args) {

        try {
            System.out.println("\n\t" + userService.getClass().getSimpleName() + ": Создание таблицы");
            userService.createUsersTable();

            System.out.println("\n\tСохранение пользователей в базу");
            userService.saveUser("Имя_for_delete", "Фамилия_for_delete", (byte) 1);
            userService.saveUser("Аня", "Анечкина", (byte) 11);
            userService.saveUser("Борис", "Бодич", (byte) 22);
            userService.saveUser("Вася", "Васечкин", (byte) 33);

            System.out.println("\n\tПолучение всех пользователей из базы и вывод на экран");
            userService.getAllUsers().forEach(System.out::println);

            System.out.println("\n\tУдаление пользователя с идентификатором id=1 и вывод на экран");
            userService.removeUserById(1);
            userService.getAllUsers().forEach(System.out::println);

            System.out.println("\n\tОчистка таблицы пользователей и вывод на экран");
            userService.cleanUsersTable();
            userService.getAllUsers().forEach(System.out::println);

            System.out.println("\n\tУдаление таблицы");
            userService.dropUsersTable();
        } finally {
            Util.closeSessionFactory();
        }
    }
}
