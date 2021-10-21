package org.project.server;

import org.project.database.Statements;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ServerImpl server = new ServerImpl();
            Registry reg = LocateRegistry.createRegistry(Server.PORT);
            reg.rebind(Server.NAME, server);
            System.out.println("SERVER PRONTO");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            Statements.initializeDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
