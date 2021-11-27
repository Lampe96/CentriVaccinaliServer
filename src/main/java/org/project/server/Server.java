package org.project.server;

import org.project.UserType;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public interface Server extends Remote {

    String NAME = "SERVER";
    int PORT = 8000;

    //METODI LATO USER

    void insertDataUser(User user) throws RemoteException;

    void changeDataUser(User vaccinatedUser) throws RemoteException;

    boolean checkDuplicateNickname(String nick) throws RemoteException;

    boolean checkDuplicateEmail(String email) throws RemoteException;

    boolean checkDuplicateTempEmail(String email) throws RemoteException;

    boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException;

    void sendVerifyEmail(String email, String nickname) throws RemoteException;

    boolean verifyCodeEmail(String email, int code) throws RemoteException;

    void deleteReferenceVerifyEmail(String email) throws RemoteException;

    ArrayList<Hub> fetchAllHub() throws RemoteException;

    float getAvgAdverseEvent(String hubName) throws RemoteException;

    boolean addAdverseEvent(AdverseEvent adverseEvent) throws RemoteException;

    Hub getHub(String hubName) throws RemoteException;


    //METODI LATO HUB

    void insertDataHub(Hub hub) throws RemoteException;

    void insertNewVaccinated(User vaccinatedUser) throws RemoteException;

    void insertVaccinatedUserInNewHub(User vaccinatedUser) throws RemoteException;

    boolean checkDuplicateHubName(String name) throws RemoteException;

    boolean checkDuplicateAddress(String address) throws RemoteException;

    Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws RemoteException;

    int checkIfFirstDose(String fiscalCode) throws RemoteException;

    boolean checkIfHubExist(String hubName) throws RemoteException;

    User fetchHubVaccinatedInfo(short UId, String hubName) throws RemoteException;

    ArrayList<User> fetchHubVaccinatedUser(String hubName) throws RemoteException;

    void updateVaccinatedUser(User vaccinatedUser) throws RemoteException;

    User getUser(String email) throws RemoteException;


    //METODI CONDIVISI

    void changeImage(int selectedImage, String hubName, String fiscalCode) throws RemoteException;

    UserType checkCredential(String key, String pwd) throws RemoteException;

    void changePwd(String hubName, String email, String newPwd) throws RemoteException;

    boolean checkPassword(String hubName, String email, String pwd) throws RemoteException;

    void deleteAccount(String hubName, String email) throws RemoteException;

    ArrayList<AdverseEvent> fetchAllAdverseEvent(String hubName) throws RemoteException;

    int[] getNumberVaccinated(String hubName) throws RemoteException;
}