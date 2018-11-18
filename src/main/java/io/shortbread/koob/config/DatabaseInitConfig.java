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
        statement.executeUpdate(
                "CREATE TABLE rooms(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(255) NOT NULL," +
                        "capacity INTEGER NOT NULL" +
                        ")"
        );
        statement.execute("INSERT INTO rooms VALUES(1, 'Won', 8)");
        statement.execute("INSERT INTO rooms VALUES(2, 'Dollar', 12)");
        statement.execute("INSERT INTO rooms VALUES(3, 'Baht', 6)");

        // FIXME: Foreign constraint keys
        statement.execute("DROP TABLE IF EXISTS reservations");
        statement.executeUpdate(
                "CREATE TABLE reservations(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "room_id INTEGER NOT NULL," +
                        "subject VARCHAR(255) NOT NULL," +
                        "description TEXT," +
                        "start_datetime DATETIME NOT NULL," +
                        "end_datetime DATETIME NOT NULL," +
                        "FOREIGN KEY(room_id) REFERENCES rooms(id)" +
                        ")"
        );
        statement.close();
        connection.close();
    }
}
