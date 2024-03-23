package db.init;

import org.flywaydb.core.Flyway;

public class InitDb {

    public void initDb(String connectionUrl)  {

        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, null, null)
                .load();

        flyway.migrate();

    }
}
