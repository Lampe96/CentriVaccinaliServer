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

    private static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    static Statement getStatement() throws SQLException {
        if (statement == null) {
            statement = getConnection().createStatement();
        }
        return statement;
    }

    public static void closeStatement() throws SQLException {
        statement.close();
        statement = null;
    }

    static PreparedStatement getInsertDataUser() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NICKNAME, EMAIL, NOME, COGNOME, CODICE_FISCALE, PASSWORD) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
    }

    static PreparedStatement getInsertDataHub() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CENTRO_VACCINALE " +
                        "(NOME_CENTRO, TIPOLOGIA, PASSWORD, QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

    static PreparedStatement getInsertNewVaccinated(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO VACCINATO_" + tableName +
                        "(ID_UNIVOCO, NOME, COGNOME, CODICE_FISCALE, NOME_CENTRO, DATA_VACCINO, TIPO_VACCINO) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)");
    }

    static PreparedStatement getUpdateIdUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET ID_UNIVOCO = ? " +
                        "WHERE CODICE_FISCALE = ?");
    }

    static PreparedStatement getEmailAndPwdU() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?");
    }

    static PreparedStatement getEmailAndPwdH() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getAddress() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getChangeImageHub() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET IMMAGINE = ? " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getImage() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT IMMAGINE " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getChangePwd() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET PASSWORD = ? " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getDeleteHub() throws SQLException {
        return getConnection().prepareStatement(
                "DELETE FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?");
    }

    static PreparedStatement getCheckIfFirstDose() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT ID_UNIVOCO " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement getFetchHubVaccinatedInfo(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "SELECT NOME_CENTRO," +
                        "DATA_VACCINO," +
                        "CODICE_FISCALE," +
                        "TIPO_VACCINO " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE ID_UNIVOCO = ?"
        );
    }

    static PreparedStatement updateVaccinatedUser(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE VACCINATO_" + tableName +
                        " SET ID_UNIVOCO = ?, NOME_CENTRO = ?, DATA_VACCINO = ?, TIPO_VACCINO = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }
}
