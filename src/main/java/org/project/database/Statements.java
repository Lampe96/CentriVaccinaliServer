package org.project.database;

import com.password4j.Password;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.models.Hub;
import org.project.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    "provincia CHAR(2) NOT NULL" +
                    ");";

    @Language("POSTGRES-SQL")
    private final static String CR_TB_CITT_REG =
            "CREATE TABLE IF NOT EXISTS Cittadino_Registrato(" +
                    "nickname VARCHAR(25) PRIMARY KEY," +
                    "email VARCHAR(50) NOT NULL," +
                    "nome VARCHAR(25) NOT NULL," +
                    "cognome VARCHAR(25) NOT NULL," +
                    "codice_fiscale CHAR(16) UNIQUE NOT NULL," +
                    "password VARCHAR(500) NOT NULL," +
                    "id_univoco bigint references Vaccinato_NomeCentroVaccinale (id_univoco) ON UPDATE CASCADE ON DELETE SET NULL" +
                    ");";

    @Language("POSTGRES-SQL")
    private final static String CR_TB_EV_AVV =
            "CREATE TABLE IF NOT EXISTS Evento_Avverso(" +
                    "tipo VARCHAR(20)," +
                    "nickname VARCHAR(25) references Cittadino_Registrato (nickname) ON UPDATE CASCADE ON DELETE SET NULL," +
                    "severita SMALLINT NOT NULL," +
                    "testo VARCHAR(256) NOT NULL," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro) ON UPDATE CASCADE ON DELETE SET NULL," +
                    "PRIMARY KEY(tipo, nickname)" +
                    ");";

    public static void initializeDb() throws SQLException {
        DbHelper.getStatement().executeUpdate(
                CR_TB_CV + "\n" + CR_TB_CITT_REG + "\n" + CR_TB_EV_AVV
        );
    }

    public static void insertDataUser(@NotNull User user) throws SQLException {
        PreparedStatement pStat = DbHelper.getPStmtInsertDataUser();
        pStat.setString(1, user.getNickname());
        pStat.setString(2, user.getEmail());
        pStat.setString(3, user.getName());
        pStat.setString(4, user.getSurname());
        pStat.setString(5, user.getCode());
        pStat.setString(6, user.getPassword());
        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }

    public static void insertDataHub(Hub hub) throws SQLException {
        PreparedStatement pStat = DbHelper.getPStmtInsertDataHub();
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

        PreparedStatement pStatCr = DbHelper.getPStmtCreateHub();
        pStatCr.setString(1, hub.getNameHub());
        System.out.println(pStatCr);
        pStatCr.executeUpdate();
        pStatCr.closeOnCompletion();
    }

    public static boolean checkDuplicateNickname(String nick) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT nickname " +
                        "FROM cittadino_registrato"
        );

        while (rs.next()) {
            if (rs.getString(1).equals(nick)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDuplicateEmail(String email) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT email " +
                        "FROM cittadino_registrato"
        );

        while (rs.next()) {
            if (rs.getString(1).equals(email)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDuplicateFiscalCode(String fiscalCode) throws SQLException {
        ResultSet rs = DbHelper.getStatement().executeQuery(
                "SELECT codice_fiscale " +
                        "FROM cittadino_registrato"
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
                "SELECT nome_centro " +
                        "FROM centro_vaccinale"
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
                "SELECT qualificatore, via, numero, citta, cap, provincia " +
                        "FROM centro_vaccinale"
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

        if (rsU.next()) {
            if (Password.check(pwd, rsU.getString(1)).withArgon2()) {
                return UserType.USER;
            }
        }

        PreparedStatement psH = DbHelper.getEmailAndPwdH();
        psH.setString(1, email);
        ResultSet rsH = psH.executeQuery();

        if (rsH.next()) {
            if (Password.check(pwd, rsH.getString(1)).withArgon2()) {
                return UserType.HUB;
            }
        }

        return null;
    }
}
