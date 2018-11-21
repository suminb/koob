package io.shortbread.koob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitConfig {

    @Autowired
    private DataSource dataSource;

    // TODO: Would it be possible to initialize the table from an entity class?
    @PostConstruct
    public void initialize() throws SQLException {
        // FIXME: Use the equivalant of Python's `with` statement
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS rooms");
        statement.execute(
                "CREATE TABLE rooms(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(255) NOT NULL," +
                        "capacity INTEGER NOT NULL" +
                        ")"
        );
        statement.execute("INSERT INTO rooms VALUES(1, 'Won', 8)");
        statement.execute("INSERT INTO rooms VALUES(2, 'Dollar', 12)");
        statement.execute("INSERT INTO rooms VALUES(3, 'Yen', 8)");
        statement.execute("INSERT INTO rooms VALUES(4, 'Baht', 6)");

        // FIXME: Foreign constraint keys
        statement.execute("DROP TABLE IF EXISTS reservations");
        statement.execute(
                "CREATE TABLE reservations(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "room_id INTEGER NOT NULL," +
                        "subject VARCHAR(255) NOT NULL," +
                        "description TEXT," +
                        "start DATETIME NOT NULL," +
                        "`end` DATETIME NOT NULL," +
                        "closing DATETIME NOT NULL," +
                        "recurring_frequency INTEGER NOT NULL," + // SQLite does not support enum type
                        "recurring_interval INTEGER NOT NULL," +
                        "recurring_count INTEGER NOT NULL," +
                        "FOREIGN KEY(room_id) REFERENCES rooms(id)" +
                        ")"
        );

        statement.execute("DROP TABLE IF EXISTS weekly_recurrences");
        statement.execute(
                "CREATE TABLE weekly_recurrences(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "weekday INTEGER NOT NULL," +
                        "reservation_id INTEGER NOT NULL," +
                        "start DATETIME NOT NULL," +
                        "`end` DATETIME NOT NULL," +
                        "FOREIGN KEY(reservation_id) REFERENCES reservations(id)" +
                        ")"
        );
        statement.execute("CREATE INDEX idx_weekly_recurrences_weekday ON weekly_recurrences (weekday)");

        statement.close();
        connection.close();
    }
}
