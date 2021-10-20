package org.project.server;

import org.project.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

@SuppressWarnings("unused")
public interface Server extends Remote {

    String NAME = "SERVER";
    int PORT = 8000;

    void insertDataUser(User user) throws RemoteException;
}
