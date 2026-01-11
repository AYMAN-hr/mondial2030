package com.mondial.ticket.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestion de la connexion à la base de données MariaDB pour le Mondial 2030.
 */
public class DatabaseConnection {
    private static final String HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String URL = "jdbc:mariadb://" + HOST + ":3306/mondial2030";
    private static final String USER = "root";
    private static final String PASSWORD = "mondial2030";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Pilote MariaDB non trouvé", e);
            }
        }
        return connection;
    }
}

