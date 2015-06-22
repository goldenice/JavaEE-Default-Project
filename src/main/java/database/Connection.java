package database;

import database.flyway.DatabaseMigrator;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

//@Singleton
//@Startup
public class Connection {

    @Inject private DatabaseMigrator migrator;
    @Getter private SessionFactory sessionFactory;
    private Configuration config;

    @PostConstruct
    public void startUp() {
        config = new Configuration().configure();
        migrator.run(config);
        this.sessionFactory = setupSessionFactory(config);
    }

    private void applyMigrations(Configuration cfg) {
        migrator.run(cfg);
    }

    private static SessionFactory setupSessionFactory(Configuration cfg) {
        return cfg.buildSessionFactory(
                new StandardServiceRegistryBuilder()
                        .applySettings(cfg.getProperties())
                        .build()
        );
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

}
