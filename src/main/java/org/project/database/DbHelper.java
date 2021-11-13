package org.project.database;

import java.sql.*;

public class DbHelper {

    private final static String PROTOCOL = "jdbc:postgresql://";
    private final static String HOST = "localhost/";
    private final static String RESOURCE = "ProgettoCentriVaccinali";
    private final static String URL = PROTOCOL + HOST + RESOURCE;
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "ProgettoLabB";

    private static Connection connection = null;
    private static Statement statement = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NICKNAME, EMAIL, NOME, COGNOME, CODICE_FISCALE, PASSWORD) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
    }

    public static PreparedStatement getPStmtInsertDataHub() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CENTRO_VACCINALE " +
                        "(NOME_CENTRO, TIPOLOGIA, PASSWORD, QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

    public static PreparedStatement getEmailAndPwdU() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?");
    }

    public static PreparedStatement getEmailAndPwdH() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }

    public static PreparedStatement getAddress() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }
}
