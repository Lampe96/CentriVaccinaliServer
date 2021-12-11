package org.project.server;

import org.project.UserType;
import org.project.database.Statements;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;
import org.project.utils.EmailUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contiene l'implementazione dei metodi dichiarati nell'interfaccia
 * {@link Server}. Qui i vari metodi vanno ad utilizzare
 * i metodi di {@link Statements} per svolgere le loro funzioni.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class ServerImpl extends UnicastRemoteObject implements Server {

    /**
     * HashMap per controllare se l'email e' gia' stata verificata.
     */
    private final HashMap<String, Integer> codeTracker;

    /**
     * HashMap per tenere i codice salvati per un determinato lasso temporale.
     */
    private final HashMap<String, Timer> codeTimerTracker;

    /**
     * Array utilizzato per tener traccia dell'andamento vaccinale,
     * ogni 20 secondi viene riempita con gli ultimi dati
     * disponibili e passata ai client che richiedono i dati.
     */
    private int[] numberVaccinated;

    /**
     * Costruttore.
     *
     * @throws RemoteException RemoteException
     */
    protected ServerImpl() throws RemoteException {
        super();
        codeTracker = new HashMap<>();
        codeTimerTracker = new HashMap<>();
        numberVaccinated = new int[3];

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    numberVaccinated = Arrays.copyOf(Statements.getNumberVaccinated(), 3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000 * 20);
    }


    //METODI LATO USER

    /**
     * Utilizzato in fase di registrazione per inserire i dati dei cittadini nel DB.
     *
     * @param user contenente i dati del cittadino da inserire nel DB
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void insertDataUser(User user) throws RemoteException {
        try {
            Statements.insertDataUser(user);
            System.out.println("INSERITO NUOVO UTENTE: " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato in fase di registrazione, nel caso il cittadino abbia gia' ricevuto
     * una dose o piu' presso un centro vaccinale.
     *
     * @param vaccinatedUser parametro contenente tutti i dati del cittadino da inserire nel DB.
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void changeDataUser(User vaccinatedUser) throws RemoteException {
        try {
            Statements.changeDataUser(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per controllare se il nickname esiste gia'.
     *
     * @param nick nickname
     * @return true se il nickname &egrave; disponibile false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateNickname(String nick) throws RemoteException {
        try {
            return Statements.checkDuplicateNickname(nick);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per controllare se l'email esiste gia'.
     *
     * @param email email
     * @return true se la email &egrave; disponibile false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateEmail(String email) throws RemoteException {
        try {
            return Statements.checkDuplicateEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per controllare se un altro utente sta effettuando la verifica
     * della email, con la stessa email utilizzata dal secondo utente.
     *
     * @param email email
     * @return true se l'email e' contenuta nella hashmap delle email in attesa di verifica, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateTempEmail(String email) throws RemoteException {
        return codeTracker.containsKey(email);
    }

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questo codice fiscale.
     *
     * @param fiscalCode codice fiscale
     * @return true se il codice fiscale &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException {
        try {
            return Statements.checkDuplicateFiscalCode(fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per inviare un'email contenente un codice
     * all'indirizzo indicato per poterne verificare la validita'.
     * Dopo l'invio si attiva un timer di 10 minuti, al termine dei quali l'email
     * torna disponibile per gli altri utenti.
     *
     * @param email    email
     * @param nickname nickname
     * @throws RemoteException RemoteException
     */
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

    /**
     * Utilizzato per verificare il codice inserito dall'utente e confrontarlo
     * con il codice presente all'interno della tabella codeTracker.
     *
     * @param email email
     * @param code  codice di verifica
     * @return true se il codice corrisponde, false nel caso contrario
     * @throws RemoteException RemoteException
     */
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

    /**
     * Utilizzato per eliminare tutte le referenze presenti di
     * un utente, dopo l'annullamneto della verifica della email.
     *
     * @param email email
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void deleteReferenceVerifyEmail(String email) throws RemoteException {
        codeTracker.remove(email);
        if (codeTimerTracker.containsKey(email)) {
            codeTimerTracker.get(email).cancel();
            codeTimerTracker.remove(email);
        }
    }

    /**
     * Utilizzato per recuperare dal DB l'intera lista dei centri vaccinali.
     *
     * @return un array contenente tutti i centri vaccinali
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized ArrayList<Hub> fetchAllHub() throws RemoteException {
        try {
            return Statements.fetchAllHub();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utilizzato per calcolare la media degli eventi avversi.
     *
     * @param hubName nome centro vaccinale
     * @return La media degli eventi avversi se ne esiste almeno uno
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized float getAvgAdverseEvent(String hubName) throws RemoteException {
        try {
            return Statements.getAvgAdverseEvent(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Utilizzato nella visualizzazione delle info del centro vaccinale.
     *
     * @param hubName    nome centro vaccinale
     * @param fiscalCode codice fiscale
     * @return true se il cittadino e' stato vaccinato presso quel centro
     * false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkBeforeAddEvent(String hubName, String fiscalCode) throws RemoteException {
        try {
            return Statements.checkBeforeAddEvent(hubName, fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per aggiungere un'evento avverso.
     *
     * @param adverseEvent evento avverso
     * @return true se l'aggiunta e' avvenuta con successo, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean addAdverseEvent(AdverseEvent adverseEvent) throws RemoteException {
        try {
            return Statements.addAdverseEvent(adverseEvent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato per prendere tutti i dati di un hub in fase
     * di apertura della riga presente nella home dei cittadini.
     *
     * @param hubName nome centro vaccinale
     * @return un centro vaccinale se esiste, null in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized Hub getHub(String hubName) throws RemoteException {
        try {
            return Statements.getHub(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //METODI LATO HUB

    /**
     * Utilizzato per controllare se l'indirizzo esiste gia'.
     *
     * @param address indirizzo da controllare
     * @return true se l'indirizzo &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateAddress(String address) throws RemoteException {
        try {
            return Statements.checkDuplicateAddress(address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utilizzato in fase di registrazione per inserire i dati dei centri
     * vaccinali nel DB.
     *
     * @param hub centro vaccinale
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void insertDataHub(Hub hub) throws RemoteException {
        try {
            Statements.insertDataHub(hub);
            System.out.println("INSERITO NUOVO HUB: " + hub);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserisce un nuovo vaccinato.
     *
     * @param vaccinatedUser contiene tutti i dati del cittadino da inserire nel DB
     *                       come cittadino vaccinato
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void insertNewVaccinated(User vaccinatedUser) throws RemoteException {
        try {
            Statements.insertNewVaccinated(vaccinatedUser);
            System.out.println("INSERITO NUOVO VACCINATO: " + vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Si occupa della registrazione dei cittadini
     * precedentemente vaccinati in un altro centro vaccinale.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void insertVaccinatedUserInNewHub(User vaccinatedUser) throws RemoteException {
        try {
            Statements.insertVaccinatedUserInNewHub(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Per modificare la tabella dei cittadini registrati.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void updateVaccinatedUser(User vaccinatedUser) throws RemoteException {
        try {
            Statements.updateVaccinatedUser(vaccinatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per controllare se esiste gia' un centro vaccinale registrato
     * con questa nome.
     *
     * @param name nome del centro vaccinale
     * @return true se il nome &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkDuplicateHubName(String name) throws RemoteException {
        try {
            return Statements.checkDuplicateHubName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Controlla se il cittadino e' gia' stato vaccinato.
     *
     * @param hubName    nome del centro
     * @param fiscalCode codice fiscale del cittadino
     * @return un array con in prima posizione:
     * <ol>
     *    <li>2 se il centro non esiste</li>
     *    <li>0 se esiste il centro ma il cittadino non risulta vaccinato</li>
     *     <li>1 e in seconda posizione i dati del cittadino in caso di successo</li>
     * </ol>
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws RemoteException {
        try {
            return Statements.checkIfUserIsVaccinated(hubName, fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[]{-1};
    }

    /**
     * Controlla se il centro vaccinale passato come parametro esiste nel DB.
     *
     * @param hubName nome del centro
     * @return true se esiste, false in caso contrario
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkIfHubExist(String hubName) throws RemoteException {
        try {
            return Statements.checkIfHubExist(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Controlla se e' o meno la prima dose.
     *
     * @param fiscalCode codice fiscale del cittadino
     * @return restituisce:
     * <ol>
     *     <li>0 se il cittadino non e' stato vaccinato o non e' presente nel DB</li>
     *     <li>1 se ha effettuato la prima o la seconda dose</li>
     *     <li>2 se i dati non sono corretti</li>
     *     <li>-1 in caso di errori</li>
     * </ol>
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized int checkIfFirstDose(String fiscalCode) throws RemoteException {
        try {
            return Statements.checkIfFirstDose(fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Utilizzato dai centri vaccinali per riempire le righe della home.
     *
     * @param hubName nome del centro vaccinale
     * @return restituisce un array di cittadini che hanno ricevuto almeno una
     * dose presso il centro che chiama il metodo
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized ArrayList<User> fetchHubVaccinatedUser(String hubName) throws RemoteException {
        try {
            return Statements.fetchHubVaccinatedUser(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utilizzato per visualizzare le info del cittadino selezionato dalla riga della
     * home del centro.
     *
     * @param UId     id univoco del cittadino vaccinato da cercare
     * @param hubName nome del centro vaccinale
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized User fetchHubVaccinatedInfo(short UId, String hubName) throws RemoteException {
        try {
            return Statements.fetchHubVaccinatedInfo(UId, hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utilizzato per recuperare tutti i dati dello user lato centro vaccinale.
     *
     * @param email email del cittadino da cercare
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized User getUser(String email) throws RemoteException {
        try {
            return Statements.getUser(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //METODI CONDIVISI

    /**
     * Utilizzato nelle impostazioni, per cambiare immagine all'utente, sia cittadini
     * sia centri vaccinali.
     *
     * @param selectedImage nuovo riferimento dell'immagine da caricare sul DB
     * @param hubName       nome del centro (se chiamato dal lato cittadino viene impostato a "")
     * @param fiscalCode    codice fiscale del cittadino (se chiamato dal lato centro viene impostato a "")
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void changeImage(int selectedImage, String hubName, String fiscalCode) throws RemoteException {
        try {
            Statements.changeImage(selectedImage, hubName, fiscalCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Controlla se le credenziali inserite in fase di login sono corrette,
     * andando a confrontarle con quelle presenti nel DB.
     *
     * @param key chiave per accedere alla tabella, puo' essere o il nome del
     *            centro vaccinale o il codice fiscale del cittadino
     * @param pwd password da confrontare
     * @return restituisce il tipo dell'utente, in modo da caricare la home corretta,
     * se non trova riscontro restituisce null
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized UserType checkCredential(String key, String pwd, UserType userType) throws RemoteException {
        try {
            return Statements.checkCredential(key, pwd, userType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Va a modificare la password precedente, controllando prima su che tabella deve andare
     * modificare.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene impostato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene impostato a "")
     * @param newPwd  nuova password da inserire nel DB
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void changePwd(String hubName, String email, String newPwd) throws RemoteException {
        try {
            Statements.changePwd(hubName, email, newPwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Controlla se la password inserita coincide con quella inserita dall'utente.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene impostato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene impostato a "")
     * @param pwd     password da controllare sul DB
     * @return restiuisce true se coincide con quelle presenti nel DB, sia lato centro
     * che lato cittadino, false se non coincidono o si verifica un errore
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized boolean checkPassword(String hubName, String email, String pwd) throws RemoteException {
        try {
            return Statements.checkPassword(hubName, email, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Si occupa dell'eliminazione di tutti i riferimenti lato cittadino e centro.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene impostato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene impostato a "")
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized void deleteAccount(String hubName, String email) throws RemoteException {
        try {
            Statements.deleteAccount(hubName, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Effettua la ricerca, tramite il parametro passato, di tutti gli
     * eventi avversi riferiti a quello specifico centro vaccinale.
     *
     * @param hubName nome del centro
     * @return restituisce un array di tutti gli eventi avversi riferiti ad
     * un determinato centro vaccinale
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized ArrayList<AdverseEvent> fetchAllAdverseEvent(String hubName) throws RemoteException {
        try {
            return Statements.fetchAllAdverseEvent(hubName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Il metodo viene chiamato ogni 20 secondi, se equivale a cittadino
     * restituisce un array di 3 posizioni, altrimenti restituisce un array
     * di 4 posizioni.
     *
     * @param hubName nome centro
     * @return restuisce un array di 3 o 4 posizioni con:
     * <ol>
     *   <li>in prima posizione il numero totale di vaccinati</li>
     *   <li>in seconda il numero di vaccinati con una sola dose</li>
     *   <li>in terza il numero di vaccinati con due dosi</li>
     *   <li>in quarta i vaccinati presso il centro</li>
     *  </ol>
     * @throws RemoteException RemoteException
     */
    @Override
    public synchronized int[] getNumberVaccinated(String hubName) throws RemoteException {
        try {
            if (hubName.equals("CITTADINO")) {
                return numberVaccinated;
            } else {
                int[] nv = Arrays.copyOf(numberVaccinated, 4);
                nv[3] = Statements.getHubVaccinated(hubName);
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}