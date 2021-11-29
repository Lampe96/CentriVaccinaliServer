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

    /**
     * QUERY LATO USER
     */

    static PreparedStatement getInsertDataUser() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NICKNAME, EMAIL, NOME, COGNOME, CODICE_FISCALE, PASSWORD) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
    }

    static PreparedStatement changeDataUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO" +
                        " SET EMAIL = ?, NICKNAME = ?, PASSWORD = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    public static PreparedStatement getAvgAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT AVG(SEVERITA) " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    public static PreparedStatement checkBeforeAddEvent(String tableName) throws SQLException{
        return getConnection().prepareStatement(
                "SELECT CODICE_FISCALE " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE CODICE_FISCALE = ?"
        );
    }

    public static PreparedStatement addAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO EVENTO_AVVERSO " +
                        "(TIPO, NICKNAME, SEVERITA, TESTO, NOME_CENTRO) " +
                        "VALUES (?, ?, ?, ?, ?)"
        );
    }

    public static PreparedStatement checkIfAdverseEventExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE TIPO = ? AND NICKNAME = ? AND NOME_CENTRO = ?"
        );
    }

    public static PreparedStatement getHub() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }





    /**
     * QUERY LATO HUB
     */

    static PreparedStatement getInsertDataHub() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CENTRO_VACCINALE " +
                        "(NOME_CENTRO, TIPOLOGIA, PASSWORD, QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    static PreparedStatement getUpdateIdUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET ID_UNIVOCO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement checkIfUserExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT CODICE_FISCALE " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement getInsertNewVaccinated(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO VACCINATO_" + tableName +
                        "(ID_UNIVOCO, NOME, COGNOME, CODICE_FISCALE, NOME_CENTRO, DATA_VACCINO, TIPO_VACCINO, NUMERO_DOSE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    static PreparedStatement insertNewVaccinatedUserNotRegistered() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NOME, COGNOME, CODICE_FISCALE, ID_UNIVOCO, NUMERO_DOSE, NICKNAME) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
    }

    static PreparedStatement updateVaccinatedCitizen() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO" +
                        " SET ID_UNIVOCO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement updateVaccinatedUser(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE VACCINATO_" + tableName +
                        " SET ID_UNIVOCO = ?, NOME_CENTRO = ?, DATA_VACCINO = ?, TIPO_VACCINO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement checkIfUserIsVaccinated(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "SELECT ID_UNIVOCO, " +
                        " NOME, " +
                        "COGNOME " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement checkIfHubExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT NOME_CENTRO " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
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
                        "TIPO_VACCINO," +
                        "NUMERO_DOSE " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE ID_UNIVOCO = ?"
        );
    }

    public static PreparedStatement getUser() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }


    /**
     * QUERY CONDIVISE
     */

    static PreparedStatement getChangeImageHub() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET IMMAGINE = ? " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    static PreparedStatement getChangeImageUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET IMMAGINE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    static PreparedStatement getEmailAndPwdU() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }

    static PreparedStatement getEmailAndPwdH() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    static PreparedStatement getChangePwdHub() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET PASSWORD = ? " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    public static PreparedStatement getChangePwdCitizen() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET PASSWORD = ? " +
                        "WHERE EMAIL = ?"
        );
    }

    static PreparedStatement getDeleteHub() throws SQLException {
        return getConnection().prepareStatement(
                "DELETE FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    static PreparedStatement getDeleteUser() throws SQLException {
        return getConnection().prepareStatement(
                "DELETE FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }

    public static PreparedStatement fetchAllAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE NOME_CENTRO = ?"
        );
    }
}


























































