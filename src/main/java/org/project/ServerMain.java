package org.project;

import org.project.database.DbHelper;
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
            reg.rebind("SERVER", server);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            DbHelper.getConnection();
            Statements.initializeDb();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
