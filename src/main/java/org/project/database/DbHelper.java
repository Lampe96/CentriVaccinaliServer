package org.project.database;

import java.sql.*;

public class DbHelper {
    private final static String protocol = "jdbc:postgresql://";
    private final static String host = "localhost/";
    private final static String resource = "ProgettoCentriVaccinali";
    private final static String url = protocol + host + resource;
    private final static String username = "postgres";
    private final static String password = "ProgettoLabB";

    private static Connection connection = null;
    private static Statement statement = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    public static Statement getStatement() throws SQLException {
        if (statement == null) {
            statement = getConnection().createStatement();
        }
        return statement;
    }

    public static void closeStatement() throws SQLException {
        statement.close();
        statement = null;
    }

    public static PreparedStatement getPStmtInsertDataUser() throws SQLException {
        return getConnection().prepareStatement("insert into cittadino_registrato " +
                "(nickname, email, nome, cognome, codice_fiscale, password) " +
                "values (?, ?, ?, ?, ?, ?);");
    }

    public static PreparedStatement getPStmtInsertDataHub() throws SQLException {
        return getConnection().prepareStatement("insert into centro_vaccinale " +
                "(nome_centro, tipologia, password, qualificatore, via, numero, citta, provincia) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?);");
    }
}
