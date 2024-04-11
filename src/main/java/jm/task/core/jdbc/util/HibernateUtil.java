package jm.task.core.jdbc.util;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import jm.task.core.jdbc.model.User;


public class HibernateUtil {

    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                properties.put(Environment.URL, "jdbc:mysql://localhost:3306/user_schema?serverTimezone=Europe/Moscow&useSSL=false");
                properties.put(Environment.USER, "user");
                properties.put(Environment.PASS, "1234");
                properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

                properties.put(Environment.SHOW_SQL, "true");

                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                properties.put(Environment.HBM2DDL_AUTO, "update"); // "create-drop" -сначала создаст потом удалит /  "update" -обновлял существующую схему / "validate" -проверял ее целостность без изменений

                properties.put("hibernate.dialect.storage_engine", "innodb"); // Specify InnoDB storage engine

                configuration.setProperties(properties);

                configuration.addAnnotatedClass(User.class); // add JPA entity mapping class

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder() //  holds the services that Hibernate will need during bootstrapping and at runtime.
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.err.println("\n\n\nException:   SessionFactory getSessionFactory\n\n");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
