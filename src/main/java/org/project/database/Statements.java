package org.project.database;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Statements {

    @Language("POSTGRES-SQL")
    final static String CR_TB_CV =
            "CREATE TABLE Centro_Vaccinale(" +
                    "tipologia VARCHAR(15) NOT NULL," +
                    "password VARCHAR(500) NOT NULL," +
                    "citta VARCHAR(30) NOT NULL," +
                    "via VARCHAR(50) NOT NULL," +
                    "num VARCHAR(10) NOT NULL," +
                    "provincia CHAR(2) NOT NULL," +
                    "nome_centro VARCHAR(50) PRIMARY KEY" +
                    ");";

    @Language("POSTGRES-SQL")
    final static String CR_TB_VAC_NOMCV =
            "CREATE TABLE Vaccinato_NomeCentroVaccinale(" +
                    "nickname VARCHAR(25) NOT NULL," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro)," +
                    "data_vaccino date NOT NULL," +
                    "tipo_vaccino VARCHAR(20) NOT NULL," +
                    "id_univoco bigint PRIMARY KEY" +
                    ");";

    @Language("POSTGRES-SQL")
    final static String CR_TB_CITT_REG =
            "CREATE TABLE Cittadino_Registrato(" +
                    "email VARCHAR(50) NOT NULL," +
                    "nome VARCHAR(25) NOT NULL," +
                    "cognome VARCHAR(25) NOT NULL," +
                    "codice_fiscale CHAR(16) UNIQUE NOT NULL," +
                    "password VARCHAR(500) NOT NULL," +
                    "id_univoco bigint references Vaccinato_NomeCentroVaccinale (id_univoco)," +
                    "nickname VARCHAR(25) PRIMARY KEY" +
                    ");";

    @Language("POSTGRES-SQL")
    final static String CR_TB_EV_AVV =
            "CREATE TABLE Evento_Avverso(" +
                    "severita bigint NOT NULL," +
                    "testo VARCHAR(256) NOT NULL," +
                    "nome_centro VARCHAR(50) references Centro_Vaccinale (nome_centro)," +
                    "tipo VARCHAR(20)," +
                    "nickname VARCHAR(25) references Cittadino_Registrato (nickname)," +
                    "PRIMARY KEY(tipo, nickname)" +
                    ");";

    public static void initializeDb() throws SQLException {
        DbHelper.getStatement().executeUpdate(
                CR_TB_CV + "\n" + CR_TB_VAC_NOMCV + "\n" + CR_TB_CITT_REG + "\n" + CR_TB_EV_AVV
        );
    }

    public static ResultSet getBoh() throws SQLException {
        return DbHelper.getStatement().executeQuery(
                "SELECT * FROM cittadino_registrato"
        );
    }

    public static void insertDataUser(@NotNull User user) throws SQLException {
        PreparedStatement pStat = DbHelper.getPStmtInsertDataUser();
        pStat.setString(1, user.getEmail());
        pStat.setString(2, user.getName());
        pStat.setString(3, user.getSurname());
        pStat.setString(4, user.getCode());
        pStat.setString(5, user.getPassword());
        pStat.setString(6, user.getNickname());
        pStat.executeUpdate();
        pStat.closeOnCompletion();
    }
}
