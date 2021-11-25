package org.project.database;

import com.password4j.Password;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.Address;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Statements {

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
                    "PRIMARY KEY(codice_fiscale, nickname)" +
                    ");";

    @Language("POSTGRES-SQL")
    private final static String CR_TB_EV_AVV =
            "CREATE TABLE IF NOT EXISTS Evento_Avverso(" +
                    "tipo VARCHAR(20)," +
                    "nickname VARCHAR(25) references Cittadino_Registrato (nickname) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "severita SMALLINT NOT NULL," +
                    "testo VARCHAR(256)," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "PRIMARY KEY(tipo, nickname)" +
                    ");";

    public static void initializeDb() throws SQLException {
        DbHelper.getStatement().executeUpdate(
                CR_TB_CV + "\n" + CR_TB_CITT_REG + "\n" + CR_TB_EV_AVV
        );
    }

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

    private static boolean checkIfUserExist(String fiscalCode) throws SQLException {
        PreparedStatement pStat = DbHelper.checkIfUserExist();
        pStat.setString(1, fiscalCode);

        ResultSet rs = pStat.executeQuery();

        pStat.closeOnCompletion();
        return rs.next();
        /*try {
            ResultSet rsAll = DbHelper.getStatement().executeQuery(
                    "SELECT CODICE_FISCALE " +
                            "FROM CITTADINO_REGISTRATO " +
                            "WHERE CODICE_FISCALE = " + fiscalCode
            );

            return rsAll.next();
        } catch (SQLException e) {

            return false;
        }*/
    }

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

    public static Address getAddress(String hubName) throws SQLException {
        PreparedStatement pStat = DbHelper.getAddress();
        pStat.setString(1, hubName);

        ResultSet rs = pStat.executeQuery();

        pStat.closeOnCompletion();

        rs.next();

        return new Address(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
    }

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

    public static boolean checkDuplicateEmail(String email) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT EMAIL " +
                        "FROM CITTADINO_REGISTRATO"
        );

        while (rs.next()) {
            if (rs.getString(1) != null){
                if (rs.getString(1).equals(email)) {
                    return false;
                }
            }
        }
        return true;
    }

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

    public static UserType checkCredential(String email, String pwd) throws SQLException {
        PreparedStatement psU = DbHelper.getEmailAndPwdU();
        psU.setString(1, email);
        ResultSet rsU = psU.executeQuery();
        psU.closeOnCompletion();

        if (rsU.next()) {
            if (Password.check(pwd, rsU.getString(1)).withArgon2()) {
                return UserType.USER;
            }
        }

        PreparedStatement psH = DbHelper.getEmailAndPwdH();
        psH.setString(1, email);
        ResultSet rsH = psH.executeQuery();
        psH.closeOnCompletion();

        if (rsH.next()) {
            if (Password.check(pwd, rsH.getString(1)).withArgon2()) {
                return UserType.HUB;
            }
        }

        return null;
    }

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
                        "FROM VACCINATO_" + tableName + ")"
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

    public static int getImage(String hubName) throws SQLException {
        PreparedStatement pStat = DbHelper.getImage();
        pStat.setString(1, hubName);

        ResultSet rs = pStat.executeQuery();
        pStat.closeOnCompletion();
        rs.next();

        return rs.getInt(1);
    }

    public static void changeImageHub(int selectedImage, String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.getChangeImageHub();
        pStats.setInt(1, selectedImage);
        pStats.setString(2, hubName);
        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    public static boolean checkPasswordHub(String hubName, String pwd) throws SQLException {
        PreparedStatement psU = DbHelper.getEmailAndPwdH();
        psU.setString(1, hubName);
        ResultSet rsU = psU.executeQuery();
        psU.closeOnCompletion();

        if (rsU.next()) {
            return Password.check(pwd, rsU.getString(1)).withArgon2();
        }

        return false;
    }

    public static Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws SQLException {
        if (checkIfHubExist(hubName)) {
            String tableName = hubName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
            PreparedStatement pStats = DbHelper.checkIfUserIsVaccinated(tableName);
            pStats.setString(1, fiscalCode);
            pStats.executeQuery();
            pStats.closeOnCompletion();

            ResultSet rs = pStats.executeQuery();
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

    public static void changePwd(String hubName, String newPwd) throws SQLException {
        PreparedStatement pStats = DbHelper.getChangePwd();
        pStats.setString(1, newPwd);
        pStats.setString(2, hubName);
        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    public static void deleteHub(String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.getDeleteHub();
        pStats.setString(1, hubName);
        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }


    public static int checkIfFirstDose(String fiscalCode) throws SQLException {
        if(checkIfUserExist(fiscalCode)){
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
        }else{
            return 0;
        }
        return -1;
    }

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

    public static ArrayList<Hub> fetchAllHub() throws SQLException {
        ResultSet rsAll = DbHelper.getStatement().executeQuery(
                "SELECT * " +
                        "FROM CENTRO_VACCINALE"
        );

        ArrayList<Hub> allHub = new ArrayList<>();
        Hub hub;
        Address address;

        if (rsAll.next()) {
            while (rsAll.next()) {
                hub = new Hub();
                address = new Address();
                hub.setNameHub(rsAll.getString(1));
                hub.setType(rsAll.getString(3));
                address.setQualificator(rsAll.getString(4));
                address.setAddress(rsAll.getString(5));
                address.setNumber(rsAll.getString(6));
                address.setCity(rsAll.getString(7));
                address.setCap(rsAll.getString(8));
                address.setProvince(rsAll.getString(9));
                hub.setImage(rsAll.getInt(10));
                hub.setAddress(address);
                allHub.add(hub);
            }
            return allHub;
        }

        return null;
    }


    public static ArrayList<AdverseEvent> fetchAllAdverseEvent() throws SQLException {
        ResultSet rsAll = DbHelper.getStatement().executeQuery(
                "SELECT * " +
                        "FROM EVENTO_AVVERSO"
        );

        ArrayList<AdverseEvent> allAdverseEvent = new ArrayList<>();
        AdverseEvent adverseEvent;
        if (rsAll.next()) {
            while (rsAll.next()) {
                adverseEvent = new AdverseEvent();
                adverseEvent.setEventType(rsAll.getString(1));
                adverseEvent.setNickname(rsAll.getString(2));
                adverseEvent.setGravity(rsAll.getInt(3));
                adverseEvent.setText(rsAll.getString(4));
                adverseEvent.setHubName(rsAll.getString(5));
                allAdverseEvent.add(adverseEvent);

            }
            return allAdverseEvent;
        }

        return null;
    }

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

    public static void insertVaccinatedUserInNewHub(User vaccinatedUser) throws SQLException {
        insertVaccinatedTableVaccinatedHospital(vaccinatedUser);
        updateCitizen(vaccinatedUser);
    }

    private static void updateCitizen(User vaccinatedUser) throws SQLException{
        PreparedStatement pStats = DbHelper.updateVaccinatedCitizen();
        pStats.setShort(1, vaccinatedUser.getId());
        pStats.setInt(2, vaccinatedUser.getDose());
        pStats.setString(3, vaccinatedUser.getFiscalCode());

        pStats.executeUpdate();
        pStats.closeOnCompletion();
    }

    public static boolean checkIfHubExist(String hubName) throws SQLException {
        PreparedStatement pStats = DbHelper.checkIfHubExist();
        pStats.setString(1, hubName);

        ResultSet rs = pStats.executeQuery();
        pStats.closeOnCompletion();

        return rs.next();
    }


}
