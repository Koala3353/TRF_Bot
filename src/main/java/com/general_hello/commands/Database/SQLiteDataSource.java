package com.general_hello.commands.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    public static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        // Gets the database connection and makes if it isn't there
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        }

        if (connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        }
        return connection;
    }

    // Removes all none letter text from the word
    public static String filter(String word) {
        return word.replaceAll("\\s+", "").replaceAll("'", "").toLowerCase();
    }
}
