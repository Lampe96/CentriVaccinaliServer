package org.project.server;

import org.project.UserType;
import org.project.database.Statements;
import org.project.models.*;
import org.project.utils.EmailUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ServerImpl extends UnicastRemoteObject implements Server {

    private final HashMap<String, Integer> codeTracker;
    private final HashMap<String, Timer> codeTimerTracker;

    protected ServerImpl() throws RemoteException {
        super();
        codeTracker = new HashMap<>();
        codeTimerTracker = new HashMap<>();
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
    public synchronized void insertNewVaccinated(User vaccinatedUser) throws RemoteException {
        try {
            Statements.insertNewVaccinated(vaccinatedUser);
            System.out.println("INSERITO NUOVO VACCINATO: " + vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeDataUser(User vaccinatedUser) throws RemoteException {
        try {
            Statements.changeDataUser(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void insertVaccinatedUserInNewHub(User vaccinatedUser) throws RemoteException {
        try {
            Statements.insertVaccinatedUserInNewHub(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Address getAddress(String hubName) throws RemoteException {
        try {
            return Statements.getAddress(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void changeImageHub(int selectedImage, String hubName) throws RemoteException {
        try {
            Statements.changeImageHub(selectedImage, hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized int getImage(String hubName) throws RemoteException {
        try {
            return Statements.getImage(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
    public synchronized boolean checkPasswordHub(String hubName, String pwd) throws RemoteException {
        try {
            return Statements.checkPasswordHub(hubName, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws RemoteException {
        try {
            return Statements.checkIfUserIsVaccinated(hubName, fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return new Object[] {-1};
    }

    @Override
    public synchronized int checkIfFirstDose(String fiscalCode) throws RemoteException {
        try {
            return Statements.checkIfFirstDose(fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean checkIfHubExist(String hubName) throws RemoteException {
        try {
            return Statements.checkIfHubExist(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized User fetchHubVaccinatedInfo(short idUnivoco, String hubName) throws RemoteException {
        try {
            return Statements.fetchHubVaccinatedInfo(idUnivoco, hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void changePwd(String hubName, String newPwd) throws RemoteException {
        try {
            Statements.changePwd(hubName, newPwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteHub(String hubName) throws RemoteException {
        try {
            Statements.deleteHub(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendVerifyEmail(String email, String nickname) throws RemoteException {
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        try {
            EmailUtil.sendVerifyEmail(email, nickname, code);
            codeTracker.put(email, code);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    codeTracker.remove(email);
                    codeTimerTracker.remove(email);
                }
            }, 1000 * 60 * 10);
            codeTimerTracker.put(email, timer);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean verifyCodeEmail(String email, int code) throws RemoteException {
        if (codeTracker.containsKey(email)) {
            boolean codeOk = codeTracker.get(email) == code;
            if (codeOk) {
                codeTracker.remove(email);
                if (codeTimerTracker.containsKey(email)) {
                    codeTimerTracker.get(email).cancel();
                    codeTimerTracker.remove(email);
                }
            }
            return codeOk;
        }

        return false;
    }

    @Override
    public synchronized void deleteReferenceVerifyEmail(String email) throws RemoteException {
        codeTracker.remove(email);
        if (codeTimerTracker.containsKey(email)) {
            codeTimerTracker.get(email).cancel();
            codeTimerTracker.remove(email);
        }
    }

    @Override
    public synchronized ArrayList<User> fetchHubVaccinatedUser(String hubName) throws RemoteException {
        try {
            return (ArrayList<User>) Statements.fetchHubVaccinatedUser(hubName).stream().sorted(Comparator.comparing(User::getSurname, String.CASE_INSENSITIVE_ORDER).thenComparing(User::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(User::getNickname, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

       @Override
    public synchronized ArrayList<Hub> fetchAllHub() throws RemoteException {
         try {
            return (ArrayList<Hub>) Objects.requireNonNull(Statements.fetchAllHub()).stream().sorted(Comparator.comparing(Hub::getNameHub, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized ArrayList<AdverseEvent> fetchAllAdverseEvent() throws RemoteException {
        try {
            return Statements.fetchAllAdverseEvent();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public synchronized void updateVaccinatedUser(User vaccinatedUser) throws RemoteException {
        try {
            Statements.updateVaccinatedUser(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
