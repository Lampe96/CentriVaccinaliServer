package org.project.server;

import org.project.models.Hub;
import org.project.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

@SuppressWarnings("unused")
public interface Server extends Remote {

    String NAME = "SERVER";
    int PORT = 8000;

    void insertDataUser(User user) throws RemoteException;

    void insertDataHub(Hub hub) throws RemoteException;

    boolean checkDuplicateNickname(String nick) throws RemoteException;

    boolean checkDuplicateEmail(String email) throws RemoteException;

    boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException;
}
