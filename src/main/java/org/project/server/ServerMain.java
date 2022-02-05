package org.project.server;

import org.project.database.DbHelper;
import org.project.database.Statements;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * In questa classe e' contenuto il main del server.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class ServerMain {

    /**
     * Utilizzata per resettare il colore delle stringhe in console.
     */
    private final static String ANSI_RESET = "\u001B[0m";
    /**
     * Utilizzata per colorare le stringhe di verde in console.
     */
    private final static String ANSI_GREEN = "\u001B[32m";
    /**
     * Utilizzata per colorare le stringhe di blu in console.
     */
    private final static String ANSI_BLUE = "\u001B[34m";

    /**
     * Nel main del server vengono richieste le credenziali per accedere al DB in seguito,
     * viene creato l'oggetto server, il quale verra poi messo a
     * disposizione dei client attraverso il registro RMI.
     * Altre funzioni sono:
     * <ol>
     *   <li>inizializzare il DB creando le tabelle vuote necessarie.</li>
     *   <li>inserisce un Thread che rileva quando il programma viene fermato, chiudendo tutte le connessioni aperte.</li>
     * </ol>
     *
     * @param args args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ANSI_GREEN + "Inserire le credenziali per accedere al DB" + ANSI_RESET);
        System.out.print(ANSI_BLUE + "Username: " + ANSI_RESET);
        DbHelper.setUsernameDB(scanner.nextLine());
        System.out.print(ANSI_BLUE + "Password: " + ANSI_RESET);
        DbHelper.setPasswordDB(scanner.nextLine());

        try {
            Statements.initializeDb();
            System.out.println("ACCESSO AL DB ESEGUITO\n");

            ServerImpl server = null;
            try {
                server = new ServerImpl();
                LocateRegistry.createRegistry(Server.PORT);
                Naming.rebind("rmi://localhost:" + Server.PORT + "/" + Server.NAME, server);
                System.out.println("REBIND SERVER ESEGUITA");
            } catch (RemoteException | MalformedURLException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            ServerImpl finalServer = server;
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        try {
                            DbHelper.closeStatement();
                            DbHelper.closeConnection();
                            UnicastRemoteObject.unexportObject(finalServer, true);
                        } catch (SQLException | NoSuchObjectException e) {
                            e.printStackTrace();
                            System.exit(-1);
                        }
                    })
            );

        } catch (SQLException e) {
            System.err.println("CREDENZIALI NON CORRETTE");
            System.exit(-1);
        }
    }
}