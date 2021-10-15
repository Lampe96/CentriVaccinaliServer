package org.project.database;

import java.sql.SQLException;

public class Statements {

    public static void initializeDb() throws SQLException {

        DbHelper.getStatement().executeUpdate(
                "CREATE TABLE Centro_Vaccinale(" +
                        "tipologia VARCHAR(10) NOT NULL," +
                        "password VARCHAR(25) NOT NULL," +
                        "citta VARCHAR(20) NOT NULL," +
                        "via VARCHAR(40) NOT NULL," +
                        "num VARCHAR(5) NOT NULL," +
                        "provincia VARCHAR(2) NOT NULL," +
                        "nome_centro VARCHAR(30) PRIMARY KEY" +
                        ");" +
                        "\n" +
                        "CREATE TABLE Vaccinato_NomeCentroVaccinale(" +
                        "nickname VARCHAR(20) NOT NULL," +
                        "nome_centro VARCHAR(30) references Centro_Vaccinale (nome_centro)," +
                        "data_vaccino date NOT NULL," +
                        "tipo_vaccino VARCHAR(15) NOT NULL," +
                        "id_univoco bigint PRIMARY KEY" +
                        ");" +
                        "\n" +
                        "CREATE TABLE Cittadino_Registrato(" +
                        "email VARCHAR(50) NOT NULL," +
                        "nome VARCHAR(20) NOT NULL," +
                        "cognome VARCHAR(25) NOT NULL," +
                        "codice_fiscale CHAR(16) UNIQUE NOT NULL," +
                        "password VARCHAR(25) NOT NULL," +
                        "id_univoco bigint references Vaccinato_NomeCentroVaccinale (id_univoco)," +
                        "nickname VARCHAR(20) PRIMARY KEY" +
                        ");" +
                        "\n" +
                        "CREATE TABLE Evento_Avverso(" +
                        "severita bigint NOT NULL," +
                        "testo VARCHAR(256) NOT NULL," +
                        "nome_centro VARCHAR(30) references Centro_Vaccinale (nome_centro)," +
                        "tipo VARCHAR(15)," +
                        "nickname VARCHAR(20) references Cittadino_Registrato (nickname)," +
                        "PRIMARY KEY(tipo, nickname)" +
                        ");");
       DbHelper.closeStatement();
    }
}
