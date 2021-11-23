package org.project.server;

import org.project.UserType;
import org.project.models.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

@SuppressWarnings("unused")
public interface Server extends Remote {

    String NAME = "SERVER";
    int PORT = 8000;

    void insertDataUser(User user) throws RemoteException;

    void insertDataHub(Hub hub) throws RemoteException;

    void insertNewVaccinated(VaccinatedUser vaccinatedUser) throws RemoteException;

    Address getAddress(String hubName) throws RemoteException;

    void changeImageHub(int selectedImage, String hubName) throws RemoteException;

    int getImage(String hubName) throws RemoteException;

    boolean checkDuplicateNickname(String nick) throws RemoteException;

    boolean checkDuplicateEmail(String email) throws RemoteException;

    boolean checkDuplicateTempEmail(String email) throws RemoteException;

    boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException;

    boolean checkDuplicateHubName(String name) throws RemoteException;

    boolean checkDuplicateAddress(String address) throws RemoteException;

    UserType checkCredential(String email, String pwd) throws RemoteException;

    boolean checkPasswordHub(String hubName, String pwd) throws RemoteException;

    boolean checkIfUserExist(String name, String surname, String fiscalCode) throws RemoteException;

    boolean checkIfFirstDose(String fiscalCode) throws RemoteException;

    boolean checkIfHubExist(String hubName) throws RemoteException;

    VaccinatedUser fetchHubVaccinatedInfo(short idUnivoco, String hubName) throws RemoteException;

    void changePwd(String hubName, String newPwd) throws RemoteException;

    void deleteHub(String hubName) throws RemoteException;

    void sendVerifyEmail(String email, String nickname) throws RemoteException;

    boolean verifyCodeEmail(String email, int code) throws RemoteException;

    void deleteReferenceVerifyEmail(String email) throws RemoteException;

    ArrayList<VaccinatedUser> fetchHubVaccinatedUser(String hubName) throws RemoteException;

    ArrayList<Hub> fetchAllHub() throws RemoteException;

    ArrayList<AdverseEvent> fetchAllAdverseEvent() throws RemoteException;

    void updateVaccinatedUser(short idUnivoco, String hubName, String vaccineType, Date newDate, String fiscalCode, short newDose) throws RemoteException;


}