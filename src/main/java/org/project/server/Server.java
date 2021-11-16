package org.project.server;

import org.project.UserType;
import org.project.models.Address;
import org.project.models.Hub;
import org.project.models.User;
import org.project.models.VaccinatedUser;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public interface Server extends Remote {

    String NAME = "SERVER";
    int PORT = 8000;

    void insertDataUser(User user) throws RemoteException;

    void insertDataHub(Hub hub) throws RemoteException;

    void insertNewVaccinated(User user) throws RemoteException;

    Address getAddress(String hubName) throws RemoteException;

//    int getImage(String hubName) throws RemoteException;

    boolean checkDuplicateNickname(String nick) throws RemoteException;

    boolean checkDuplicateEmail(String email) throws RemoteException;

    boolean checkDuplicateTempEmail(String email) throws RemoteException;

    boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException;

    boolean checkDuplicateHubName(String name) throws RemoteException;

    boolean checkDuplicateAddress(String address) throws RemoteException;

    UserType checkCredential(String email, String pwd) throws RemoteException;

    void sendVerifyEmail(String email, String nickname) throws RemoteException;

    boolean verifyCodeEmail(String email, int code) throws RemoteException;

    void deleteReferenceVerifyEmail(String email) throws RemoteException;

    ArrayList<VaccinatedUser> fetchHubVaccinatedUser(String hubName) throws RemoteException;


}
