package org.project.database;

import java.sql.*;

/**
 * Classe utilizzata per stabilire la connessione con il DB
 * e per effettuare le query pre-compilate
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class DbHelper {

    /**
     * Protocollo utilizzato per accedere al DB
     */
    private final static String PROTOCOL = "jdbc:postgresql://";
    /**
     * Host del DB
     */
    private final static String HOST = "localhost/";
    /**
     * Nome del database
     */
    private final static String RESOURCE = "ProgettoCentriVaccinali";
    /**
     * Url per connessione composto da protocollo + host + risorsa
     */
    private final static String URL = PROTOCOL + HOST + RESOURCE;
    /**
     * Username del database
     */
    private final static String USERNAME = "postgres";
    /**
     * Password del database
     */
    private final static String PASSWORD = "ProgettoLabB";
    /**
     * Connessione con il database
     */
    private static Connection connection = null;
    /**
     * Statement aperto sul database
     */
    private static Statement statement = null;

    /**
     * Utilizzato per stabilire la connessione con il database
     * attraverso JDBC e driver necessari
     *
     * @return conessione con il database
     * @throws SQLException SQLException
     */
    private static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return connection;
    }

    /**
     * Utilizzato per chiudere la connessione al database
     *
     * @throws SQLException SQLException
     */
    public static void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    /**
     * Utilizzato per aprire uno statement sul database
     *
     * @return statement
     * @throws SQLException SQLException
     */
    static Statement getStatement() throws SQLException {
        if (statement == null) {
            statement = getConnection().createStatement();
        }
        return statement;
    }

    /**
     * Utilizzato per chiudere lo statement
     *
     * @throws SQLException SQLException
     */
    public static void closeStatement() throws SQLException {
        statement.close();
        statement = null;
    }


    //QUERY LATO USER

    /**
     * Viene utilizzata per inserire i cittadini
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getInsertDataUser() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NICKNAME, EMAIL, NOME, COGNOME, CODICE_FISCALE, PASSWORD) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
    }

    /**
     * Va a modificare la tupla corrispondente al codice fiscale inserito
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement changeDataUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO" +
                        " SET EMAIL = ?, NICKNAME = ?, PASSWORD = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Utilizzato per calcolare la media degli eventi avversi di un certo
     * evento
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getAvgAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT AVG(SEVERITA) " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Seleziona la tupla dove il codice fiscale e' uguale a
     * quello inserito
     *
     * @param tableName nome del centro
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement checkBeforeAddEvent(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "SELECT CODICE_FISCALE " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Inserisce un nuovo evento avverso creando la tupla con tutti i
     * campi passati
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement addAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO EVENTO_AVVERSO " +
                        "(TIPO, NICKNAME, SEVERITA, TESTO, NOME_CENTRO) " +
                        "VALUES (?, ?, ?, ?, ?)"
        );
    }

    /**
     * Seleziona le eventuali tuple corrispondenti alla ricerca effettuata
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement checkIfAdverseEventExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE TIPO = ? AND NICKNAME = ? AND NOME_CENTRO = ?"
        );
    }

    /**
     * Seleziona il centro vaccinale indicato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getHub() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }


    //QUERY LATO HUB

    /**
     * Inserisce nella tabella dei centri vaccinali il centro
     * passato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getInsertDataHub() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CENTRO_VACCINALE " +
                        "(NOME_CENTRO, TIPOLOGIA, PASSWORD, QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    /**
     * Aggiorna il cittadino con i dati passati
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getUpdateIdUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET ID_UNIVOCO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Seleziona la tupla con il cittadino identificato col codice fiscale
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement checkIfUserExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT CODICE_FISCALE " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Inserisce nella tabella dei vaccinati riferita al centro
     * specifico i dati passati
     *
     * @param tableName nome del centro
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getInsertNewVaccinated(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO VACCINATO_" + tableName +
                        "(ID_UNIVOCO, NOME, COGNOME, CODICE_FISCALE, NOME_CENTRO, DATA_VACCINO, TIPO_VACCINO, NUMERO_DOSE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    /**
     * Inserisce nella tabella dei cittadini un nuovo vaccinato non
     * ancora registrato nel DB
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement insertNewVaccinatedUserNotRegistered() throws SQLException {
        return getConnection().prepareStatement(
                "INSERT INTO CITTADINO_REGISTRATO " +
                        "(NOME, COGNOME, CODICE_FISCALE, ID_UNIVOCO, NUMERO_DOSE, NICKNAME) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
    }

    /**
     * Aggiorna il cittadino registrato con una nuova dose e id univoco
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement updateVaccinatedCitizen() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO" +
                        " SET ID_UNIVOCO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Aggiorna la tabella dei vaccinati riferita al centro in questione
     *
     * @param tableName nome del centro
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement updateVaccinatedUser(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE VACCINATO_" + tableName +
                        " SET ID_UNIVOCO = ?, NOME_CENTRO = ?, DATA_VACCINO = ?, TIPO_VACCINO = ?, NUMERO_DOSE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Seleziona l'eventuale tupla corrispondente al cittadino ricercato,
     * per verificare se egli ha effettuato la vaccinazione
     *
     * @param tableName nome del centro
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement checkIfUserIsVaccinated(String tableName) throws SQLException {
        return getConnection().prepareStatement(
                "SELECT ID_UNIVOCO, " +
                        " NOME, " +
                        "COGNOME " +
                        "FROM VACCINATO_" + tableName +
                        " WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Seleziona la tupla riferita al centro vaccinale
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement checkIfHubExist() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT NOME_CENTRO " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Restituisce la tupla con l'id univoco, per verificare se il cittadino
     * ha effettuato almeno una dose
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getCheckIfFirstDose() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT ID_UNIVOCO " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Restituisce la tupla con le info del cittadino vaccinato cercato
     *
     * @param tableName nome centro
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
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

    /**
     * Restituisce la tupla con tutti i dati del cittadino cercato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getUser() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }


    //QUERY CONDIVISE

    /**
     * Aggiorna l'immagine del centro vaccinale con la nuova passata
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getChangeImageHub() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET IMMAGINE = ? " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Aggiorna l'immagine del cittadino con la nuova passata
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getChangeImageUser() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET IMMAGINE = ? " +
                        "WHERE CODICE_FISCALE = ?"
        );
    }

    /**
     * Restituisce la tupla con la password del cittadino selezionato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getEmailAndPwdU() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }

    /**
     * Restituisce la tupla con la password del centro vaccinale selezionato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getEmailAndPwdH() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT PASSWORD " +
                        "FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Aggiorna la password del centro vaccinale selezionato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getChangePwdHub() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CENTRO_VACCINALE " +
                        "SET PASSWORD = ? " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Aggiorna la password del cittadino selezionato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getChangePwdCitizen() throws SQLException {
        return getConnection().prepareStatement(
                "UPDATE CITTADINO_REGISTRATO " +
                        "SET PASSWORD = ? " +
                        "WHERE EMAIL = ?"
        );
    }

    /**
     * Elimina il centro vaccinale indicato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getDeleteHub() throws SQLException {
        return getConnection().prepareStatement(
                "DELETE FROM CENTRO_VACCINALE " +
                        "WHERE NOME_CENTRO = ?"
        );
    }

    /**
     * Elimina il cittadino indicato
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement getDeleteUser() throws SQLException {
        return getConnection().prepareStatement(
                "DELETE FROM CITTADINO_REGISTRATO " +
                        "WHERE EMAIL = ?"
        );
    }

    /**
     * Restituisce la tabella con tutti gli eventi avversi riferiti a
     * quel centro
     *
     * @return restituisce una preparedStatement impostata come indicato
     * @throws SQLException SQLException
     */
    static PreparedStatement fetchAllAdverseEvent() throws SQLException {
        return getConnection().prepareStatement(
                "SELECT * " +
                        "FROM EVENTO_AVVERSO " +
                        "WHERE NOME_CENTRO = ?"
        );
    }
}