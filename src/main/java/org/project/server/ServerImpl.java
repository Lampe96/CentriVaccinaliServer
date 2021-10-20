package org.project.server;

import org.project.database.Statements;
import org.project.models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class ServerImpl extends UnicastRemoteObject implements Server {

    protected ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void insertDataUser(User user) throws RemoteException {
        try {
            Statements.insertDataUser(user);
            System.out.println("INSERITO NUOVO UTENTE: " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
