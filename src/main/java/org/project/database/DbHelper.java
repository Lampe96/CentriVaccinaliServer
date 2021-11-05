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
                "insert into cittadino_registrato " +
                        "(nickname, email, nome, cognome, codice_fiscale, password) " +
                        "values (?, ?, ?, ?, ?, ?);");
    }

    public static PreparedStatement getPStmtInsertDataHub() throws SQLException {
        return getConnection().prepareStatement(
                "insert into centro_vaccinale " +
                        "(nome_centro, tipologia, password, qualificatore, via, numero, citta, cap, provincia) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
    }

    public static PreparedStatement getPStmtCreateHub() throws SQLException {
        return getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS Vaccinato_?(" +
                                    "id_univoco bigint PRIMARY KEY," +
                                    "nome VARCHAR(25)," +
                                    "cognome VARCHAR(25)," +
                                    "codice_fiscale CHAR(16)," +
                                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro) ON UPDATE CASCADE ON DELETE SET NULL," +
                                    "data_vaccino date," +
                                    "tipo_vaccino VARCHAR(20)" +
                                    ");");
    }

    public static PreparedStatement getEmailAndPwdU() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT password " +
                        "FROM cittadino_registrato " +
                        "WHERE email = ?;");
    }

    public static PreparedStatement getEmailAndPwdH() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT password " +
                        "FROM centro_vaccinale " +
                        "WHERE nome_centro = ?;");
    }
}
