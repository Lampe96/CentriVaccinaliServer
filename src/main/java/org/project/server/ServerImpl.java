package org.project.server;

import org.project.UserType;
import org.project.database.Statements;
import org.project.models.Hub;
import org.project.models.User;
import org.project.utils.EmailUtil;

import javax.mail.MessagingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
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
    public boolean checkDuplicateTempEmail(String email) throws RemoteException {
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
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);

        try {
            EmailUtil.sendVerifyEmail(email, nickname, code);
            codeTracker.put(email, code);
        } catch (MessagingException e) {
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
}
