package org.project.server;

import org.project.UserType;
import org.project.models.AdverseEvent;
import org.project.models.Hub;
import org.project.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Questa classe contiene tutti i metodi utilizzati dal client,
 * i quali vanno a lavorare direttamente sul DB remoto.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
@SuppressWarnings("unused")
public interface Server extends Remote {

    /**
     * Nome con cui viene caricato il server sul registro RMI.
     */
    String NAME = "SERVER";
    /**
     * Porta di utilizzata dal server.
     */
    int PORT = 8000;


    //METODI LATO USER

    /**
     * Utilizzato in fase di registrazione per inserire i dati dei cittadini nel DB.
     *
     * @param user contiene tutti i dati del cittadino da inserire nel DB
     * @throws RemoteException RemoteException
     */
    void insertDataUser(User user) throws RemoteException;

    /**
     * Utilizzato in fase di registrazione, nel caso il cittadino abbia gia' ricevuto
     * una dose o piu' presso un centro vaccinale.
     *
     * @param vaccinatedUser contiene tutti i dati del cittadino da inserire nel DB
     * @throws RemoteException RemoteException
     */
    void changeDataUser(User vaccinatedUser) throws RemoteException;

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questo nickname.
     *
     * @param nick nickname
     * @return true se il nickname &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateNickname(String nick) throws RemoteException;

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questa email.
     *
     * @param email email
     * @return true se la email &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateEmail(String email) throws RemoteException;

    /**
     * Utilizzato per controllare se un altro utente sta effettuando la verifica
     * della email, con la stessa email utilizzata dal secondo utente.
     *
     * @param email email
     * @return true se l'email non è disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateTempEmail(String email) throws RemoteException;

    /**
     * Utilizzato per controllare se esiste gia' un utente registrato
     * con questo codice fiscale.
     *
     * @param fiscalCode codice fiscale
     * @return true se il codice fiscale e' disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateFiscalCode(String fiscalCode) throws RemoteException;

    /**
     * Utilizzato per inviare una email al utente per verificare la validita' della stessa.
     *
     * @param email    email
     * @param nickname nickname
     * @throws RemoteException RemoteException
     */
    void sendVerifyEmail(String email, String nickname) throws RemoteException;

    /**
     * Utilizzato per verificare che il codice inviato via email e
     * inserito dal utente, corrisponde con quello salvato sul server.
     *
     * @param email email
     * @param code  codice di verifica
     * @return true se il codice inserito corrisponde, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean verifyCodeEmail(String email, int code) throws RemoteException;

    /**
     * Utilizzato per eliminare la propria referenza dalla tabella presente sul server
     * in caso di annullamento in fase di registrazione del utente.
     *
     * @param email email
     * @throws RemoteException RemoteException
     */
    void deleteReferenceVerifyEmail(String email) throws RemoteException;

    /**
     * Utilizzato per recuperare dal DB l'intera lista dei centri vaccinali.
     *
     * @return un array contenente tutti i centri vaccinali
     * @throws RemoteException RemoteException
     */
    ArrayList<Hub> fetchAllHub() throws RemoteException;

    /**
     * Utilizzato per calcolare la media.
     *
     * @param hubName nome centro vaccinale
     * @return La media degli eventi avversi se ne esiste almeno uno
     * @throws RemoteException RemoteException
     */
    float getAvgAdverseEvent(String hubName) throws RemoteException;

    /**
     * Utilizzato nella visualizzazione delle info del centro vaccinale.
     *
     * @param hubName    nome centro vaccinale
     * @param fiscalCode codice fiscale
     * @return true se il cittadino e' stato vaccinato presso quel centro
     * false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkBeforeAddEvent(String hubName, String fiscalCode) throws RemoteException;

    /**
     * Utilizzato per aggiungere un evento avverso.
     *
     * @param adverseEvent evento avverso
     * @return true se l'aggiunta e' avvenuta con successo, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean addAdverseEvent(AdverseEvent adverseEvent) throws RemoteException;

    /**
     * Utilizzato per prendere tutti i dati di un hub in fase
     * di apertura della riga presente nella home dei cittadini.
     *
     * @param hubName nome centro vaccinale
     * @return un centro vaccinale se esiste, null in caso contrario
     * @throws RemoteException RemoteException
     */
    Hub getHub(String hubName) throws RemoteException;


    //METODI LATO HUB

    /**
     * Utilizzato per controllare se l'indirizzo esiste gia'.
     *
     * @param address indirizzo da controllare
     * @return true se l'indirizzo &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateAddress(String address) throws RemoteException;

    /**
     * Utilizzato in fase di registrazione per inserire i dati dei centri
     * vaccinali nel DB.
     *
     * @param hub centro vaccinale
     * @throws RemoteException RemoteException
     */
    void insertDataHub(Hub hub) throws RemoteException;

    /**
     * Inserisce un nuovo vaccinato.
     *
     * @param vaccinatedUser contiene tutti i dati del cittadino da inserire nel DB
     *                       come cittadino vaccinato
     * @throws RemoteException RemoteException
     */
    void insertNewVaccinated(User vaccinatedUser) throws RemoteException;

    /**
     * Si occupa della registrazione dei cittadini
     * precedentemente vaccinati in un altro centro vaccinale.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws RemoteException RemoteException
     */
    void insertVaccinatedUserInNewHub(User vaccinatedUser) throws RemoteException;

    /**
     * Utilizzato per aggiornare la tabella dei cittadini registrati.
     *
     * @param vaccinatedUser oggetto contenente tutti i campi da inserire nel DB
     * @throws RemoteException RemoteException
     */
    void updateVaccinatedUser(User vaccinatedUser) throws RemoteException;

    /**
     * Utilizzato per controllare se esiste gia' un centro vaccinale registrato
     * con questa nome.
     *
     * @param name nome del centro vaccinale
     * @return true se il nome &egrave; disponibile, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkDuplicateHubName(String name) throws RemoteException;

    /**
     * Controlla se il cittadino e' gia' vaccinato.
     *
     * @param hubName    nome del centro presso il quale effettuare la vaccinazione
     * @param fiscalCode codice fiscale del cittadino
     * @return un array con in prima posizione 2, se il centro non esiste, 0 se esiste il centro
     * ma il cittadino non risulta vaccinato, 1 e i dati del cittadino in caso contrario
     * @throws RemoteException RemoteException
     */
    Object[] checkIfUserIsVaccinated(String hubName, String fiscalCode) throws RemoteException;

    /**
     * Controlla se il centro
     * vaccinale passato come parametro esiste nel DB.
     *
     * @param hubName nome del centro
     * @return true se esiste, false in caso contrario
     * @throws RemoteException RemoteException
     */
    boolean checkIfHubExist(String hubName) throws RemoteException;

    /**
     * Controlla se e' o meno la prima dose.
     *
     * @param fiscalCode codice fiscale del cittadino
     * @return restituisce 0 se il cittadino non e' stato vaccinato o non e' presente
     * nel DB, 1 se ha effettuato la prima o la seconda dose, 2 se i dati non sono corretti,
     * -1 in caso di errori
     * @throws RemoteException RemoteException
     */
    int checkIfFirstDose(String fiscalCode) throws RemoteException;

    /**
     * Utilizzato dai centri vaccinali per riempire le righe della home.
     *
     * @param hubName nome del centro vaccinale
     * @return restituisce un array di cittadini che hanno ricevuto almeno una
     * dose presso il centro che chiama il metodo
     * @throws RemoteException RemoteException
     */
    ArrayList<User> fetchHubVaccinatedUser(String hubName) throws RemoteException;

    /**
     * Utilizzato per visualizzare le info del cittadino selezionato dalla riga della
     * home del centro.
     *
     * @param UId     id univoco del cittadino vaccinato da cercare
     * @param hubName nome del centro vaccinale
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws RemoteException RemoteException
     */
    User fetchHubVaccinatedInfo(short UId, String hubName) throws RemoteException;

    /**
     * Utilizzato per recuperare tutti i dati dello user lato centro vaccinale.
     *
     * @param email email del cittadino da cercare
     * @return restituisce i dati del cittadino richiesto, in caso di errore restituisce null
     * @throws RemoteException RemoteException
     */
    User getUser(String email) throws RemoteException;


    // METODI CONDIVISI

    /**
     * Utilizzato nelle impostazioni, per cambiare immagine all'utente, sia cittadini
     * sia centri vaccinali.
     *
     * @param selectedImage nuovo riferimento dell'immagine da caricare sul DB
     * @param hubName       nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param fiscalCode    codice fiscale del cittadino(se chiamato dal lato centro viene settato a "")
     * @throws RemoteException RemoteException
     */
    void changeImage(int selectedImage, String hubName, String fiscalCode) throws RemoteException;

    /**
     * Controlla se le credenziali inserite in fase di login sono corrette,
     * andando a confrontarle con quelle presenti nel DB.
     *
     * @param key chiave per accedere alla tabella, puo' essere o il nome del
     *            centro vaccinale o il codice fiscale del cittadino
     * @param pwd password da confrontare
     * @return restituisce il tipo dell'utente, in modo da caricare la home corretta.
     * se non trova riscontro restituisce null
     * @throws RemoteException RemoteException
     */
    UserType checkCredential(String key, String pwd, UserType userType) throws RemoteException;

    /**
     * Va a modificare la password precedente, controllando prima su che tabella deve andare
     * a modificare.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino(se chiamato dal lato centro vaccinale viene settato a "")
     * @param newPwd  newPwd nuova password da inserire nel DB
     * @throws RemoteException RemoteException
     */
    void changePwd(String hubName, String email, String newPwd) throws RemoteException;

    /**
     * Controlla se la password inserita coincide con quella inserita dall'utente.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene settato a "")
     * @param pwd     password da controllare sul DB
     * @return restiuisce true se coincide con quelle presenti nel DB, sia lato centro
     * che lato cittadino, false se non coincidono o si verifica un errore
     * @throws RemoteException RemoteException
     */
    boolean checkPassword(String hubName, String email, String pwd) throws RemoteException;

    /**
     * Si occupa dell'eliminazione di tutti i riferimenti lato cittadino e centro.
     *
     * @param hubName nome del centro (se chiamato dal lato cittadino viene settato a "")
     * @param email   email del cittadino (se chiamato dal lato centro vaccinale viene settato a "")
     * @throws RemoteException RemoteException
     */
    void deleteAccount(String hubName, String email) throws RemoteException;

    /**
     * Effettua la ricerca, tramite il parametro passato, di tutti gli
     * eventi avversi riferiti a quello specifico centro vaccinale.
     *
     * @param hubName nome del centro
     * @return restituisce un array di tutti gli eventi avversi riferiti ad
     * un determinato centro vaccinale
     * @throws RemoteException RemoteException
     */
    ArrayList<AdverseEvent> fetchAllAdverseEvent(String hubName) throws RemoteException;

    /**
     * Utilizzato per prendere tutte le informazioni per quanto riguarda
     * l'andamento vaccinale.
     *
     * @param hubName nome centro
     * @return restuisce un array di 3 posizioni con, in prima posizione
     * il numero totale di vaccinati; in seconda il numero di vaccinati con una sola
     * dose; in terza il numero di vaccinati con due dosi
     * @throws RemoteException RemoteException
     */
    int[] getNumberVaccinated(String hubName) throws RemoteException;
}