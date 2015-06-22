package database.flyway;

import javax.ejb.Stateless;
import org.hibernate.cfg.Configuration;
import org.flywaydb.core.Flyway;

@Stateless
public class DatabaseMigrator {

    public void run(Configuration config) {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(
                config.getProperty("connection.url"),
                config.getProperty("connection.username"),
                config.getProperty("connection.password")
        );
        flyway.migrate();
    }

}
