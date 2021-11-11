package org.project.server;

import org.project.UserType;
import org.project.database.Statements;
import org.project.models.Hub;
import org.project.models.User;
import org.project.models.VaccinatedUser;
import org.project.utils.EmailUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ServerImpl extends UnicastRemoteObject implements Server {

    private final HashMap<String, Integer> codeTracker;

    protected ServerImpl() throws RemoteException {
        super();
        codeTracker = new HashMap<>();
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

    @Override
    public synchronized void insertDataHub(Hub hub) throws RemoteException {
        try {
            Statements.insertDataHub(hub);
            System.out.println("INSERITO NUOVO HUB: " + hub);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void insertNewVaccinated(User user) throws RemoteException {

    }

    @Override
    public synchronized boolean checkDuplicateNickname(String nick) throws RemoteException {
        try {
            return Statements.checkDuplicateNickname(nick);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized boolean checkDuplicateEmail(String email) throws RemoteException {
        try {
            return Statements.checkDuplicateEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized boolean checkDuplicateTempEmail(String email) throws RemoteException {
        return codeTracker.containsKey(email);
    }

    @Override
    public synchronized boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException {
        try {
            return Statements.checkDuplicateFiscalCode(fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized boolean checkDuplicateHubName(String name) throws RemoteException {
        try {
            return Statements.checkDuplicateHubName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized boolean checkDuplicateAddress(String address) throws RemoteException {
        try {
            return Statements.checkDuplicateAddress(address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized UserType checkCredential(String email, String pwd) throws RemoteException {
        try {
            return Statements.checkCredential(email, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void sendVerifyEmail(String email, String nickname) throws RemoteException {
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        /*Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        System.out.println(String.format("%06d", number));*/

        try {
            EmailUtil.sendVerifyEmail(email, nickname, code);
            codeTracker.put(email, code);

            //todo mettere timer per rimuovere codice scaduto dopo 10 min
            //problema che se fa nuovo codice dopo es 5 min quello nuovo verrà tolto dopo 5 min non dopo 10 perché questo timer continua ad andare
            //possibile soluzione hash map di email e timer
            /*new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    codeTracker.remove(email);
                    //rimandane un altro?
                }
            }, 1000*60*10);*/


        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean verifyCodeEmail(String email, int code) throws RemoteException {
        boolean codeOk = codeTracker.get(email) == code;
        if (codeOk) {
            codeTracker.remove(email);
        }
        return codeOk;
    }

    @Override
    public synchronized void deleteReferenceVerifyEmail(String email) throws RemoteException {
        codeTracker.remove(email);
    }

    @Override
    public synchronized ArrayList<VaccinatedUser> fetchHubVaccinatedUser(String hubName) throws RemoteException {
        try {
            return Statements.fetchAllVaccinatedUser(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
