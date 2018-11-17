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
        statement.execute("DROP TABLE IF EXISTS reservations");
        statement.executeUpdate(
            "CREATE TABLE reservations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room INTEGER NOT NULL," +
                "start_datetime DATETIME NOT NULL," +
                "end_datetime DATETIME NOT NULL" +
            ")"
        );
        statement.close();
        connection.close();
    }
}
