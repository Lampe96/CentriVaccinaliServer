package org.project.database;

import com.password4j.Password;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.Address;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Questa classe contiene tutti i metodi utilizzati dal client,
 * i quali vanno a lavorare sul DB. In diversi
 * metodi verranno utilizzati metodi presenti nella classe {@link DbHelper}
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico  739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class Statements {

    /**
     * Creazione della tabella riferita ai centri vaccinali
     * La creazione avviene solo se la tabella
     * non esiste gia'
     */
    @Language("POSTGRES-SQL")
    private final static String CR_TB_CV =
            "CREATE TABLE IF NOT EXISTS Centro_Vaccinale(" +
                    "nome_centro VARCHAR(50) PRIMARY KEY," +
                    "tipologia VARCHAR(15) NOT NULL," +
                    "password VARCHAR(500) NOT NULL," +
                    "qualificatore VARCHAR(10) NOT NULL," +
                    "via VARCHAR(50) NOT NULL," +
                    "numero VARCHAR(10) NOT NULL," +
                    "citta VARCHAR(30) NOT NULL," +
                    "cap CHAR(5) NOT NULL," +
                    "provincia CHAR(2) NOT NULL," +
                    "immagine SMALLINT DEFAULT 1" +
                    ");";

    /**
     * Creazione della tabella riferita alle tabelle dei vaccinati
     * per i singoli centri. La creazione avviene solo se la tabella
     * non esiste gia'
     */
    @Language("POSTGRES-SQL")
    private static final String CR_TB_VAC_NOMCV =
            "CREATE TABLE IF NOT EXISTS Vaccinato_NomeCentroVaccinale(" +
                    "id_univoco SMALLINT PRIMARY KEY," +
                    "nome VARCHAR(25) NOT NULL," +
                    "cognome VARCHAR(25) NOT NULL," +
                    "codice_fiscale CHAR(16) references Cittadino_Registrato (codice_fiscale) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro) ON DELETE SET NULL ON UPDATE CASCADE," +
                    "data_vaccino DATE NOT NULL," +
                    "tipo_vaccino VARCHAR(20) NOT NULL," +
                    "numero_dose SMALLINT DEFAULT 1" +
                    ");";

    /**
     * Creazione della tabella riferita alla tabella dei cittadini
     * registrati. La creazione avviene solo se la tabella non
     * esiste gia'
     */
    @Language("POSTGRES-SQL")
    private final static String CR_TB_CITT_REG =
            "CREATE TABLE IF NOT EXISTS Cittadino_Registrato(" +
                    "nickname VARCHAR(25) UNIQUE," +
                    "email VARCHAR(50) DEFAULT NULL," +
                    "nome VARCHAR(25) NOT NULL," +
                    "cognome VARCHAR(25) NOT NULL," +
                    "codice_fiscale CHAR(16) UNIQUE," +
                    "password VARCHAR(500)  DEFAULT NULL," +
                    "id_univoco SMALLINT DEFAULT 0," +
                    "numero_dose SMALLINT DEFAULT 0," +
                    "immagine SMALLINT DEFAULT 1," +
                    "PRIMARY KEY(codice_fiscale, nickname)" +
                    ");";

    /**
     * Creazione della tabella riferita alla tabella contenente
     * tutti gli eventi avversi. La creazione avviene solo se la tabella
     * non esiste gia'
     */
    @Language("POSTGRES-SQL")
    private final static String CR_TB_EV_AVV =
            "CREATE TABLE IF NOT EXISTS Evento_Avverso(" +
                    "tipo VARCHAR(20)," +
                    "nickname VARCHAR(25) references Cittadino_Registrato (nickname) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "severita SMALLINT NOT NULL," +
                    "testo VARCHAR(256)," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "PRIMARY KEY(tipo, nickname, nome_centro)" +
                    ");";


    /**
     * Utilizzato per inizializzare il DB creando le tabelle vuote
     * necessarie
     *
     * @throws SQLException SQLException
     */
    public static void initializeDb() throws SQLException {
        DbHelper.getStatement().executeUpdate(
                CR_TB_CV + "\n" + CR_TB_CITT_REG + "\n" + CR_TB_EV_AVV
        );
    }


    //METODI LATO USER

    /**
     * Utilizzato in fase di registrazione, va a richiamare un metodo presente in DbHelper
     * il quale verra' poi riempito dai parametri presi dal parametro
     *
     * @param user tutti i dati del cittadino da inserire nel DB
     * @throws SQLException SQLException
     */
    public static void insertDataUser(@NotNull User user) throws SQLException {
        PreparedStatement pStat = DbHelper.getInsertDataUser();
        pStat.setString(1, user.getNickname());
        pStat.setString(2, user.getEmail());
        pStat.setString(3, user.getName());
        pStat.setString(4, user.getSurname());
        pStat.setString(5, user.getFiscalCode());
        pStat.setString(6, user.getPassword());
        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }

    /**
     * Utilizzato in fase di registrazione, va a richiamare un metodo presente in DbHelper
     * il quale verra' poi riempito dai parametri presi dal parametro
     *
     * @param vaccinatedUser tutti i dati del cittadino da inserire nel DB
     * @throws SQLException SQLException
     */
    public static void changeDataUser(User vaccinatedUser) throws SQLException {
        System.out.println(vaccinatedUser);
        PreparedStatement pStat = DbHelper.changeDataUser();
        pStat.setString(1, vaccinatedUser.getEmail());
        pStat.setString(2, vaccinatedUser.getNickname());
        pStat.setString(3, vaccinatedUser.getPassword());
        pStat.setString(4, vaccinatedUser.getFiscalCode());

        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questo nickname
     *
     * @param nick nickname
     * @return true se il nickname &egrave; disponibile false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkDuplicateNickname(String nick) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT NICKNAME " +
                        "FROM CITTADINO_REGISTRATO"
        );

        while (rs.next()) {
            if (rs.getString(1) != null) {
                if (rs.getString(1).equals(nick)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questa email
     *
     * @param email email
     * @return true se la email &egrave; disponibile false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkDuplicateEmail(String email) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT EMAIL " +
                        "FROM CITTADINO_REGISTRATO"
        );

        while (rs.next()) {
            if (rs.getString(1) != null) {
                if (rs.getString(1).equals(email)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questo codice fiscale
     *
     * @param fiscalCode codice fiscale del cittadino
     * @return true se il codice fiscale &egrave; disponibile false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkDuplicateFiscalCode(String fiscalCode) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT CODICE_FISCALE " +
                        "FROM CITTADINO_REGISTRATO"
        );

        while (rs.next()) {
            if (rs.getString(1).equals(fiscalCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vengono scaricati da DB tutti i centri vaccinali presenti e,
     * tramite un while, vengono inseriti in una lista di centri uno per uno.
     * La creazione avviene tramite il metodo {@link #createHub}
     *
     * @return un array contenente tutti i centri vaccinali
     * presenti nel DB
     * @throws SQLException SQLException
     */
    public static ArrayList<Hub> fetchAllHub() throws SQLException {
        ResultSet rsAll = DbHelper.getStatement().executeQuery(
                "SELECT * " +
                        "FROM CENTRO_VACCINALE " +
                        "ORDER BY NOME_CENTRO ASC"
        );

        ArrayList<Hub> allHub = new ArrayList<>();
        Hub hub;
        Address address;

        while (rsAll.next()) {
            hub = new Hub();
            address = new Address();
            hub.setNameHub(rsAll.getString(1));
            hub.setType(rsAll.getString(2));
            address.setQualificator(rsAll.getString(4));
            address.setAddress(rsAll.getString(5));
            address.setNumber(rsAll.getString(6));
            address.setCity(rsAll.getString(7));
            address.setCap(rsAll.getString(8));
            address.setProvince(rsAll.getString(9));
            hub.setAddress(address);
            hub.setImage(rsAll.getShort(10));
            allHub.add(hub);
        }

        return allHub;
    }

    /**
     * Utilizzato per riempire il campo presente nella riga della home del
     * cittadino
     *
     * @param hubName nome del centro vaccinale
     * @return La media degli eventi avversi se ne esiste almeno uno,
     * zero in caso contrario
     * @throws SQLException SQLException
     */
    public static float getAvgAdverseEvent(String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.getAvgAdverseEvent();
        pStats.setString(1, hubName);
        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        if (rs.next()) {
            return rs.getFloat(1);
        }
        return 0;
    }

    /**
     * Utilizzato nella visualizzazione delle info del centro vaccinale
     *
     * @param hubName    nome del centro vaccinale preso in considerazione
     * @param fiscalCode codice fiscale del cittadino che vuole rilasciare un
     *                   evento avverso
     * @return true se il cittadino e' stato vaccinato presso quel centro
     * false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkBeforeAddEvent(String hubName, String fiscalCode) throws SQLException {
        String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        PreparedStatement pStats = DbHelper.checkBeforeAddEvent(tableName);
        pStats.setString(1, fiscalCode);
        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        return rs.next();
    }

    /**
     * Utilizzato per aggiungere un evento avverso alla tabella corrispondente.
     * Prima di procedere con l'aggiunta, viene effettuato un controllo
     * per verificare che il cittadino non abbia gia' inserito un evento avverso
     * dello stesso tipo presso quel centro.
     *
     * @param adverseEvent model con tutti i campi necessari per l'aggiunta
     *                     dell'evento alla tabella
     * @return true se l'aggiunta è avvenuta con successo, false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean addAdverseEvent(AdverseEvent adverseEvent) throws SQLException {
        PreparedStatement pStats = DbHelper.checkIfAdverseEventExist();
        pStats.setString(1, adverseEvent.getEventType());
        pStats.setString(2, adverseEvent.getNickname());
        pStats.setString(3, adverseEvent.getHubName());

        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        if (!rs.next()) {
            PreparedStatement pStats1 = DbHelper.addAdverseEvent();
            pStats1.setString(1, adverseEvent.getEventType());
            pStats1.setString(2, adverseEvent.getNickname());
            pStats1.setShort(3, adverseEvent.getGravity());
            pStats1.setString(4, adverseEvent.getText());
            pStats1.setString(5, adverseEvent.getHubName());

            pStats1.executeUpdate();
            pStats1.closeOnCompletion();

            return true;
        }

        return false;
    }

    /**
     * Utilizzato per prendere tutti i dati di un hub in fase
     * di apertura della riga presente nella home dei cittadini.
     * Utilizza il metodo {@link #createHub} per la creazione del centro vaccinale
     *
     * @param hubName nome del centro vaccinale
     * @return un centro vaccinale se esiste, null in caso contrario
     * @throws SQLException SQLException
     */
    public static Hub getHub(String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.getHub();
        pStats.setString(1, hubName);

        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        if (rs.next()) {
            Hub hub = new Hub();
            Address address = new Address();
            createHub(rs, hub, address);
            return hub;
        }

        return null;
    }

    /**
     * Viene chiamato da {@link #getHub} e {@link #fetchAllHub},
     * utilizzato per creare l'oggetto che verra' poi restituito
     *
     * @param rs      contiene tutti i campi del centro prelevati dal DB
     * @param hub     oggetto da riempire con i campi presenti nel rs
     * @param address oggetto da riempire con i campi presenti nel rs
     * @throws SQLException SQLException
     */
    private static void createHub(ResultSet rs, Hub hub, Address address) throws SQLException {
        hub.setNameHub(rs.getString(1));
        hub.setType(rs.getString(2));
        address.setQualificator(rs.getString(4));
        address.setAddress(rs.getString(5));
        address.setNumber(rs.getString(6));
        address.setCity(rs.getString(7));
        address.setCap(rs.getString(8));
        address.setProvince(rs.getString(9));
        hub.setAddress(address);
        hub.setImage(rs.getShort(10));
    }


    //METODI LATO HUB

    /**
     * Utilizzato per controllare se l'indirizzo esiste gia'
     *
     * @param address indirizzo da controllare
     * @return true se l'indirizzo &egrave; disponibile, false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkDuplicateAddress(String address) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT QUALIFICATORE, VIA, NUMERO, CITTA, CAP, PROVINCIA " +
                        "FROM CENTRO_VACCINALE"
        );

        String dbAddress;
        while (rs.next()) {
            dbAddress = rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4) + rs.getString(5) + rs.getString(6);
            if (dbAddress.equals(address)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utilizzato in fase di registrazione per inserire i dati dei centri
     * vaccinali nel DB
     *
     * @param hub centro vaccinale da inserire
     * @throws SQLException SQLException
     */
    public static void insertDataHub(Hub hub) throws SQLException {
        PreparedStatement pStat = DbHelper.getInsertDataHub();
        pStat.setString(1, hub.getNameHub());
        pStat.setString(2, hub.getType());
        pStat.setString(3, hub.getPassword());
        pStat.setString(4, hub.getAddress().getQualificator());
        pStat.setString(5, hub.getAddress().getAddress());
        pStat.setString(6, hub.getAddress().getNumber());
        pStat.setString(7, hub.getAddress().getCity());
        pStat.setString(8, hub.getAddress().getCap());
        pStat.setString(9, hub.getAddress().getProvince());
        pStat.executeUpdate();
        pStat.closeOnCompletion();

        DbHelper.getStatement().executeUpdate(CR_TB_VAC_NOMCV.replace("NomeCentroVaccinale", hub.getNameHub().replaceAll("\\s+", "_")));
    }

    /**
     * Tramite il metodo {@link #checkIfUserExist} viene controllato se il
     * cittadino è gia' registrato. Nel caso in cui il cittadino sia gia' registrato
     * viene inserito nella tabella di riferimento del centro tramite
     * {@link #insertVaccinatedTableVaccinatedHospital}. In caso contrario viene prima inserito
     * nella tabella dei cittadini registrati, tramite {@link #insertCitizenNotRegistered} e
     * successivamente nella tabella di riferimento del centro tramite
     * {@link #insertVaccinatedTableVaccinatedHospital}. Questo avviene per rispettare i vincoli
     * delle tabelle
     *
     * @param vaccinatedUser contiene tutti i dati del cittadino da inserire nel DB
     *                       come cittadino vaccinato
     * @throws SQLException SQLException
     */
    public static void insertNewVaccinated(User vaccinatedUser) throws SQLException {
        System.out.println(vaccinatedUser);
        if (checkIfUserExist(vaccinatedUser.getFiscalCode())) {

            insertVaccinatedTableVaccinatedHospital(vaccinatedUser);

            PreparedStatement pStat2 = DbHelper.getUpdateIdUser();
            pStat2.setShort(1, vaccinatedUser.getId());
            pStat2.setShort(2, vaccinatedUser.getDose());
            pStat2.setString(3, vaccinatedUser.getFiscalCode());

            pStat2.executeUpdate();
            pStat2.closeOnCompletion();

        } else {
            insertCitizenNotRegistered(vaccinatedUser);

            insertVaccinatedTableVaccinatedHospital(vaccinatedUser);
        }
    }

    /**
     * Utilizzato in {@link #checkIfFirstDose} e {@link #insertNewVaccinated},
     * verifica se il cittadino e già registrato nel DB o meno.
     *
     * @param fiscalCode codice fiscale da controllare
     * @return true se il cittadino e' registrato, false in caso contrario
     * @throws SQLException SQLException
     */
    private static boolean checkIfUserExist(String fiscalCode) throws SQLException {
        PreparedStatement pStat = DbHelper.checkIfUserExist();
        pStat.setString(1, fiscalCode);

        ResultSet rs = pStat.executeQuery();

        pStat.closeOnCompletion();
        return rs.next();
    }

    /**
     * Utilizzato in {@link #insertNewVaccinated} e {@link #insertVaccinatedUserInNewHub},
     * effettua l'operazione di inserimento del vaccinato nella corretta tabella.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws SQLException SQLException
     */
    private static void insertVaccinatedTableVaccinatedHospital(User vaccinatedUser) throws SQLException {
        PreparedStatement pStat = DbHelper.getInsertNewVaccinated(vaccinatedUser.getHubName().replaceAll("\\s+", "_"));
        pStat.setShort(1, vaccinatedUser.getId());
        pStat.setString(2, vaccinatedUser.getName());
        pStat.setString(3, vaccinatedUser.getSurname());
        pStat.setString(4, vaccinatedUser.getFiscalCode());
        pStat.setString(5, vaccinatedUser.getHubName());
        pStat.setDate(6, vaccinatedUser.getVaccineDate());
        pStat.setString(7, vaccinatedUser.getVaccineType());
        pStat.setInt(8, vaccinatedUser.getDose());
        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }

    /**
     * Utilizzato in {@link #insertNewVaccinated}, inserisce nella tabella dei
     * cittadini i dati inseriti dagli operatori sanitari in fase di vaccinazione.
     * Nel caso in cui il cittadino in questione voglia registrarsi al programma, potra'
     * accedere ai dati precedentemente inseriti dall'operatore sanitario.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws SQLException SQLException
     */
    private static void insertCitizenNotRegistered(User vaccinatedUser) throws SQLException {
        PreparedStatement pStat = DbHelper.insertNewVaccinatedUserNotRegistered();
        pStat.setString(1, vaccinatedUser.getName());
        pStat.setString(2, vaccinatedUser.getSurname());
        pStat.setString(3, vaccinatedUser.getFiscalCode());
        pStat.setShort(4, vaccinatedUser.getId());
        pStat.setShort(5, vaccinatedUser.getDose());
        pStat.setString(6, "Guest " + vaccinatedUser.getId());
        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }

    /**
     * Si avvale dei metodi {@link #insertVaccinatedTableVaccinatedHospital} e
     * {@link #updateCitizen}. Si occupa della registrazione dei cittadini
     * precedentemente vaccinati in un altro centro vaccinale
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws SQLException SQLException
     */
    public static void insertVaccinatedUserInNewHub(User vaccinatedUser) throws SQLException {
        insertVaccinatedTableVaccinatedHospital(vaccinatedUser);
        updateCitizen(vaccinatedUser);
    }

    /**
     * Utilizzato in {@link #insertVaccinatedUserInNewHub} e {@link #updateVaccinatedUser},
     * viene utilizzato per aggiornare il numero della dose e l'id univoco nel DB
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws SQLException SQLException
     */
    private static void updateCitizen(User vaccinatedUser) throws SQLException {
        PreparedStatement pStats = DbHelper.updateVaccinatedCitizen();
        pStats.setShort(1, vaccinatedUser.getId());
        pStats.setInt(2, vaccinatedUser.getDose());
        pStats.setString(3, vaccinatedUser.getFiscalCode());

        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    /**
     * Utilizza {@link #updateCitizen} per aggiornare la tabella dei cittadini registrati,
     * dopo aver aggiornato la tabella dei vaccinati
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws SQLException SQLException
     */
    public static void updateVaccinatedUser(User vaccinatedUser) throws SQLException {
        String tableName = vaccinatedUser.getHubName().toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        PreparedStatement pStats = DbHelper.updateVaccinatedUser(tableName);
        pStats.setShort(1, vaccinatedUser.getId());
        pStats.setString(2, vaccinatedUser.getHubName());
        pStats.setDate(3, vaccinatedUser.getVaccineDate());
        pStats.setString(4, vaccinatedUser.getVaccineType());
        pStats.setShort(5, vaccinatedUser.getDose());
        pStats.setString(6, vaccinatedUser.getFiscalCode());
        pStats.executeUpdate();
        pStats.closeOnCompletion();

        updateCitizen(vaccinatedUser);
    }

    /**
     * Utilizzato per controllare se esiste gia' un centro vaccinale registrato
     * con questo nome
     *
     * @param name nome del centro vaccinale
     * @return true se il nome &egrave; disponibile, false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkDuplicateHubName(String name) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT NOME_CENTRO " +
                        "FROM CENTRO_VACCINALE"
        );

        while (rs.next()) {
            if (rs.getString(1).equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utilizza il metodo {@link #checkIfHubExist} per verificare se il centro vaccinale
     * esiste nel DB, successivamente si effettua il controllo per verificare se l'utente e' gia'
     * vaccinato. In caso positivo, verra' restituito un array di Object con il cittadino all'interno
     *
     * @param hubName    nome del centro presso il quale effettuare la vaccinazione
     * @param fiscalCode codice fiscale del cittadino
     * @return un array con in prima posizione 2, se il centro non esiste, 0 se esiste il centro
     * ma il cittadino non risulta vaccinato, 1 e i dati del cittadino in caso contrario
     * @throws SQLException SQLException
     */
    public static Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws SQLException {
        if (checkIfHubExist(hubName)) {
            String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
            PreparedStatement pStats = DbHelper.checkIfUserIsVaccinated(tableName);
            pStats.setString(1, fiscalCode);
            ResultSet rs = pStats.executeQuery();
            pStats.closeOnCompletion();

            if (rs.next()) {
                User vu = new User();
                vu.setId(rs.getShort(1));
                vu.setName(rs.getString(2));
                vu.setSurname(rs.getString(3));

                return new Object[]{1, vu};
            } else {
                return new Object[]{0};
            }
        }

        return new Object[]{2};
    }

    /**
     * Viene utilizzato in {@link #checkIfUserIsVaccinated}, controlla se il centro
     * vaccinale passato come parametro esiste nel DB
     *
     * @param hubName nome del centro
     * @return true se esiste, false in caso contrario
     * @throws SQLException SQLException
     */
    public static boolean checkIfHubExist(String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.checkIfHubExist();
        pStats.setString(1, hubName);

        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        return rs.next();
    }

    /**
     * Si avvale del metodo {@link #checkIfUserExist} per verificar se il cittadino
     * e' gia' registrato. In caso affermativo si verifica che sia effettivamente la prima volta
     * che viene vaccinato, in caso contrario deve esser utilizzato un altro metodo.
     *
     * @param fiscalCode codice fiscale del cittadino
     * @return restituisce 0 se il cittadino non è stato vaccinato o non e' presente
     * nel DB, 1 se ha effettuato la prima o la seconda dose, 2 se i dati non sono corretti,
     * -1 in caso di errori
     * @throws SQLException SQLException
     */
    public static int checkIfFirstDose(String fiscalCode) throws SQLException {
        if (checkIfUserExist(fiscalCode)) {
            PreparedStatement pStat = DbHelper.getCheckIfFirstDose();
            pStat.setString(1, fiscalCode);
            pStat.closeOnCompletion();

            ResultSet rs = pStat.executeQuery();

            if (rs.next()) {
                if (rs.getShort(1) == 0) {
                    return 0;
                } else if (rs.getShort(1) == 1 || rs.getShort(1) == 2) {
                    return 1;
                }
            } else {
                return 2;
            }
        } else {
            return 0;
        }
        return -1;
    }

    /**
     * Utilizzato dai centri vaccinali per riempire le righe della home. Vengono effettuate
     * due query, la prima per trovare tutti i cittadini vaccinati presso il centro, la seconda
     * per controllare se ogni singolo cittadino ha rilasciato o meno un evento presso il centro
     *
     * @param hubName nome del centro vaccinale
     * @return restituisce un array di cittadini che hanno ricevuto almeno una
     * dose presso il centro che chiama il metodo
     * @throws SQLException SQLException
     */
    public static ArrayList<User> fetchHubVaccinatedUser(String hubName) throws SQLException {
        String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        ResultSet rsAll = DbHelper.getStatement().executeQuery(
                "SELECT NOME," +
                        "COGNOME," +
                        "NICKNAME," +
                        "ID_UNIVOCO," +
                        "NUMERO_DOSE " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE CODICE_FISCALE IN (SELECT CODICE_FISCALE " +
                        "FROM VACCINATO_" + tableName + ")" +
                        "ORDER BY COGNOME, NOME, NICKNAME ASC"
        );

        ArrayList<User> avu = new ArrayList<>();
        User vu;

        while (rsAll.next()) {
            vu = new User();
            vu.setName(rsAll.getString(1));
            vu.setSurname(rsAll.getString(2));
            vu.setNickname(rsAll.getString(3));
            vu.setId(rsAll.getShort(4));
            vu.setDose(rsAll.getShort(5));
            avu.add(vu);
        }

        ResultSet rsEvent = DbHelper.getStatement().executeQuery(
                " SELECT NICKNAME " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE NICKNAME IN (SELECT NICKNAME " +
                        "FROM EVENTO_AVVERSO) AND CODICE_FISCALE IN (SELECT CODICE_FISCALE " +
                        "FROM VACCINATO_" + tableName + ")"
        );

        while (rsEvent.next()) {
            for (User vue : avu) {
                if (vue.getNickname().equals(rsEvent.getString(1))) {
                    vue.setEvent(rsEvent.getString(1));
                }
            }
        }

        return avu;
    }

    /**
     * Utilizzato per visualizzare le info del cittadino selezionato dalla riga della
     * home del centro
     *
     * @param UId     id univoco del cittadino vaccinato da cercare
     * @param hubName nome del centro vaccinale
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws SQLException SQLException
     */
    public static User fetchHubVaccinatedInfo(short UId, String hubName) throws SQLException {
        String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
        PreparedStatement pStat = DbHelper.getFetchHubVaccinatedInfo(tableName);
        pStat.setShort(1, UId);

        ResultSet rs = pStat.executeQuery();
        pStat.closeOnCompletion();

        if (rs.next()) {
            User vu = new User();
            vu.setHubName(rs.getString(1));
            vu.setVaccineDate(rs.getDate(2));
            vu.setFiscalCode(rs.getString(3));
            vu.setVaccineType(rs.getString(4));
            vu.setDose(rs.getShort(5));
            vu.setId(UId);
            return vu;
        }
        return null;
    }

    /**
     * Utilizzato per recuperare tutti i dati dello user lato centro vaccinale
     *
     * @param email email del cittadino da cercare
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws SQLException SQLException
     */
    public static User getUser(String email) throws SQLException {
        PreparedStatement pStats = DbHelper.getUser();
        pStats.setString(1, email);

        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        if (rs.next()) {
            User us = new User();
            us.setNickname(rs.getString(1));
            us.setEmail(rs.getString(2));
            us.setName(rs.getString(3));
            us.setSurname(rs.getString(4));
            us.setFiscalCode(rs.getString(5));
            us.setId(rs.getShort(7));
            us.setDose(rs.getShort(8));
            us.setImage(rs.getShort(9));
            return us;
        }
        return null;
    }


    // METODI CONDIVISI

    /**
     * Utilizzato nelle impostazioni, per cambiare immagine all'utente, sia cittadini
     * sia centri vaccinali
     *
     * @param selectedImage nuovo riferimento dell'immagine da caricare sul DB
     * @param hubName       nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param fiscalCode    codice fiscale del cittadino(se chiamato dal lato centro viene settato a "")
     * @throws SQLException SQLException
     */
    public static void changeImage(int selectedImage, String hubName, String fiscalCode) throws SQLException {
        PreparedStatement pStats;
        if (!hubName.equals("")) {
            pStats = DbHelper.getChangeImageHub();
            pStats.setInt(1, selectedImage);
            pStats.setString(2, hubName);
        } else {
            pStats = DbHelper.getChangeImageUser();
            pStats.setInt(1, selectedImage);
            pStats.setString(2, fiscalCode);
        }
        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    /**
     * Controlla se le credenziali inserite in fase di login sono corrette,
     * andando a confrontarle con quelle presenti nel DB
     *
     * @param key chiave per accedere alla tabella, puo' essere o il nome del
     *            centro vaccinale o il codice fiscale del cittadino
     * @param pwd password da confrontare
     * @return restituisce il tipo dell'utente, in modo da caricare la home corretta.
     * se non trova riscontro restituisce null
     * @throws SQLException SQLException
     */
    public static UserType checkCredential(String key, String pwd) throws SQLException {
        PreparedStatement psU = DbHelper.getEmailAndPwdU();
        psU.setString(1, key);
        ResultSet rsU = psU.executeQuery();
        psU.closeOnCompletion();

        if (rsU.next()) {
            if (Password.check(pwd, rsU.getString(1)).withArgon2()) {
                return UserType.USER;
            }
        }

        PreparedStatement psH = DbHelper.getEmailAndPwdH();
        psH.setString(1, key);
        ResultSet rsH = psH.executeQuery();
        psH.closeOnCompletion();

        if (rsH.next()) {
            if (Password.check(pwd, rsH.getString(1)).withArgon2()) {
                return UserType.HUB;
            }
        }

        return null;
    }

    /**
     * Va a modificare la password precedente, controllando prima su che tabella deve andare
     * modificare
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino(se chiamato dal lato centro vaccinale viene settato a "")
     * @param newPwd  nuova password da inserire nel DB
     * @throws SQLException SQLException
     */
    public static void changePwd(String hubName, String email, String newPwd) throws SQLException {
        PreparedStatement pStats;
        if (!hubName.equals("")) {
            pStats = DbHelper.getChangePwdHub();
            pStats.setString(1, newPwd);
            pStats.setString(2, hubName);
        } else {
            pStats = DbHelper.getChangePwdCitizen();
            pStats.setString(1, newPwd);
            pStats.setString(2, email);
        }
        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    /**
     * Controlla se la password inserita coincide con quella inserita dall'utente
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene settato a "")
     * @param pwd     password da controllare sul DB
     * @return restiuisce true se coincide con quelle presenti nel DB, sia lato centro
     * che lato cittadino, false se non coincidono o si verifica un errore
     * @throws SQLException SQLException
     */
    public static boolean checkPassword(String hubName, String email, String pwd) throws SQLException {
        if (!hubName.equals("")) {
            PreparedStatement psH = DbHelper.getEmailAndPwdH();
            psH.setString(1, hubName);
            ResultSet rsH = psH.executeQuery();
            psH.closeOnCompletion();

            if (rsH.next()) {
                return Password.check(pwd, rsH.getString(1)).withArgon2();
            }

        } else {
            PreparedStatement psU = DbHelper.getEmailAndPwdU();
            psU.setString(1, email);
            ResultSet rsU = psU.executeQuery();
            psU.closeOnCompletion();

            if (rsU.next()) {
                return Password.check(pwd, rsU.getString(1)).withArgon2();
            }
        }
        return false;
    }

    /**
     * Si occupa dell'eliminazione di tutti i riferimenti lato cittadino e centro
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene settato a "")
     * @throws SQLException SQLException
     */
    public static void deleteAccount(String hubName, String email) throws SQLException {
        PreparedStatement pStats;
        if (!hubName.equals("")) {
            pStats = DbHelper.getDeleteHub();
            pStats.setString(1, hubName);
        } else {
            pStats = DbHelper.getDeleteUser();
            pStats.setString(1, email);
        }

        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    /**
     * Effettua la ricerca, tramite il parametro passato, di tutti gli
     * eventi avversi riferiti a quello specifico centro vaccinale.
     *
     * @param hubName nome del centro
     * @return restituisce un array di tutti gli eventi avversi riferiti ad
     * un determinato centro vaccinale
     * @throws SQLException SQLException
     */
    public static ArrayList<AdverseEvent> fetchAllAdverseEvent(String hubName) throws SQLException {
        PreparedStatement pStat = DbHelper.fetchAllAdverseEvent();
        pStat.setString(1, hubName);
        ResultSet rsAll = pStat.executeQuery();
        pStat.closeOnCompletion();

        ArrayList<AdverseEvent> allAdverseEvent = new ArrayList<>();
        AdverseEvent adverseEvent;

        while (rsAll.next()) {
            adverseEvent = new AdverseEvent();
            adverseEvent.setEventType(rsAll.getString(1));
            adverseEvent.setNickname(rsAll.getString(2));
            adverseEvent.setGravity(rsAll.getShort(3));
            adverseEvent.setText(rsAll.getString(4));
            adverseEvent.setHubName(rsAll.getString(5));
            allAdverseEvent.add(adverseEvent);
        }

        return allAdverseEvent;
    }

    /**
     * Effettua una serie query per recuperare una serie dati con cui
     * vengono riempite le label presenti nelle home e il grafico
     * sull'andamento delle vaccinazioni
     *
     * @return restuisce un array di 3 posizioni con, in prima posizione
     * il numero totale di vaccinati; in seconda il numero di vaccinati con una sola
     * dose; in terza il numero di vaccinati con due dosi
     * @throws SQLException SQLException
     */
    public static int[] getNumberVaccinated() throws SQLException {
        int[] vcn = new int[3];

        ResultSet rsAll = DbHelper.getStatement().executeQuery(
                "SELECT COUNT(ID_UNIVOCO) " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE ID_UNIVOCO != 0"
        );

        if (rsAll.next()) {
            vcn[0] = rsAll.getInt(1);
        }

        ResultSet rsDose1 = DbHelper.getStatement().executeQuery(
                "SELECT COUNT(NUMERO_DOSE) " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE NUMERO_DOSE = 1"
        );

        if (rsDose1.next()) {
            vcn[1] = rsDose1.getInt(1);
        }

        ResultSet rsDose2 = DbHelper.getStatement().executeQuery(
                "SELECT COUNT(NUMERO_DOSE) " +
                        "FROM CITTADINO_REGISTRATO " +
                        "WHERE NUMERO_DOSE = 2"
        );

        if (rsDose2.next()) {
            vcn[2] = rsDose2.getInt(1);
        }

        return vcn;
    }

    /**
     * Utilizzato in serverImpl per riempire l'array per l'andamento vaccinazioni
     *
     * @param hubName nome del centro vaccinale
     * @return restituisce il numero di vaccinati presso quel centro, 0 se non ce
     * ne sono
     * @throws SQLException SQLException
     */
    public static int getHubVaccinated(String hubName) throws SQLException {

        String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");

        ResultSet rsAllHub = DbHelper.getStatement().executeQuery(
                "SELECT COUNT(*) " +
                        "FROM VACCINATO_" + tableName
        );

        if (rsAllHub.next()) {
            return rsAllHub.getInt(1);
        }
        return 0;
    }
}