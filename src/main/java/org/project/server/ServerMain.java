package org.project.server;

import org.project.database.DbHelper;
import org.project.database.Statements;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

/**
 * In questa classe e' contenuto il main del server
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class ServerMain {

    /**
     * Nel main del server viene creato l'oggetto server per poi, essere
     * messo a disposizione dei client attraverso il registro RMI.
     * Altre funzioni sono quelle di: inizializzare il DB creando le tabelle
     * vuote necessarie e inserendo un Thread che rileva quando il programma viene
     * fermato chiudendo tutte le connessioni aperte.
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            ServerImpl server = new ServerImpl();

            try {
                LocateRegistry.createRegistry(Server.PORT);
                Naming.rebind("rmi://localhost:" + Server.PORT + "/" + Server.NAME, server);
                System.out.println("SERVER PRONTO");

                try {
                    Statements.initializeDb();

                    Runtime.getRuntime().addShutdownHook(
                            new Thread(() -> {
                                try {
                                    DbHelper.closeStatement();
                                    DbHelper.closeConnection();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    System.exit(-1);
                                }
                            })
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}