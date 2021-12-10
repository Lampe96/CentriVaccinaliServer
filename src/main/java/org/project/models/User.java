package org.project.models;

import java.io.Serializable;
import java.sql.Date;

/**
 * In questa classe viene creato l'oggetto user, utilizzato
 * per i passaggio dei dati e per la scrittura su DB.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class User implements Serializable {

    /**
     * Nome del cittadino.
     */
    private String name;

    /**
     * Cognome del cittadino.
     */
    private String surname;

    /**
     * Codice fiscale del cittadino.
     */
    private String fiscalCode;

    /**
     * Email del cittadino.
     */
    private String email;

    /**
     * Nickname del cittadino.
     */
    private String nickname;

    /**
     * Password del cittadino.
     */
    private String password;

    /**
     * Parametro utilizzato per controllare se il cittadino
     * ha o meno segnalato un evento
     */
    private String event;

    /**
     * Id univo del cittadino, assegnato dai centri vaccinali.
     */
    private short id;

    /**
     * Nome hub presso cui e' stato vaccinato il cittadino.
     */
    private String hubName;

    /**
     * Data del vaccino effettuato dal cittadino.
     */
    private Date vaccineDate;

    /**
     * Tipologia di vaccino ricevuto dal cittadino.
     */
    private String vaccineType;

    /**
     * Numero dosi ricevute dal cittadino.
     */
    private short dose;

    /**
     * Riferimento dell'immagine scelta dal cittadino.
     */
    private short image;

    /**
     * Costruttore vuoto.
     */
    public User() {
    }

    /**
     * Utilizzato per prendere il nome.
     *
     * @return nome del cittadino
     */
    public String getName() {
        return name;
    }

    /**
     * Utilizzato per impostare il nome del cittadino.
     *
     * @param name nome da impostare
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Utilizzato per prendere il cognome.
     *
     * @return cognome del cittadino
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Utilazzato per impostare il cognome del cittadino.
     *
     * @param surname cognome da impostare
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Utilizzato per prendere il codice fiscale.
     *
     * @return codice fiscale del cittadino
     */
    public String getFiscalCode() {
        return fiscalCode;
    }

    /**
     * Utilizzato per impostare il codice fiscale del cittadino.
     *
     * @param fiscalCode codice fiscale da impostare
     */
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    /**
     * Utilizzato per prendere l'email.
     *
     * @return email del cittadino
     */
    public String getEmail() {
        return email;
    }

    /**
     * Utilizzato per impostare l'email del cittadino.
     *
     * @param email email da impostare
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Utilizzato per prendere il nickname.
     *
     * @return nickname del cittadino
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Utilizzato per impostare il nickname del cittadino.
     *
     * @param nickname nickname da impostare
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Utilizzato per prendere la password.
     *
     * @return password del cittadino
     */
    public String getPassword() {
        return password;
    }

    /**
     * Utilizzato per impostare la password del cittadino.
     *
     * @param password password da impostare
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Utilizzato per verificare se il cittadino ha o meno segnalato
     * un evento.
     *
     * @return una stringa
     */
    public String getEvent() {
        return event;
    }

    /**
     * Utilizzato per verificare se il cittadino ha o meno segnalato
     * un evento.
     *
     * @param event eventuale evento segnalato
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Utilizzato per prendere l'id univoco.
     *
     * @return id univoco del cittadino
     */
    public short getId() {
        return id;
    }

    /**
     * Utilizzato per impostare l'id univoco del cittadino.
     *
     * @param id id univoco da impostare
     */
    public void setId(short id) {
        this.id = id;
    }

    /**
     * Utilizzato per prendere il nome del centro vaccinale.
     *
     * @return nome del centro presso cui si e' vaccinato
     */
    public String getHubName() {
        return hubName;
    }

    /**
     * Utilizzato per impostare il nome del centro.
     *
     * @param hubName nome del centro vaccinale presso cui si e' vaccinato
     */
    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per prendere la data del vaccino.
     *
     * @return data in cui e' stato effettuato il vaccino
     */
    public Date getVaccineDate() {
        return vaccineDate;
    }

    /**
     * Utilizzato per impostare la data del vaccino.
     *
     * @param vaccineDate data del vaccino
     */
    public void setVaccineDate(Date vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    /**
     * Utilizzato per prendere la tipologia del vaccino effettuato.
     *
     * @return tipo del vaccino
     */
    public String getVaccineType() {
        return vaccineType;
    }

    /**
     * Utilizzato per impostare la tipologia del vaccino.
     *
     * @param vaccineType tipologia del vaccino
     */
    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    /**
     * Utilizzato per prendere il numero di dose effettuato.
     *
     * @return numero della dose
     */
    public short getDose() {
        return dose;
    }

    /**
     * Utilizzato per impostare il numero della dose.
     *
     * @param dose numero della dose
     */
    public void setDose(short dose) {
        this.dose = dose;
    }

    /**
     * Utilizzato per prendere il numero dell'immagine.
     *
     * @return numero di riferimento dell'immagine
     */
    public short getImage() {
        return image;
    }

    /**
     * Utilizzato per impostare il numero dell'immagine.
     *
     * @param image numero di riferimento dell'immagine
     */
    public void setImage(short image) {
        this.image = image;
    }

    /**
     * Utilizzato per effettuare la concatenazione in string.
     *
     * @return concatenazione di tutti i parametri
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", event='" + event + '\'' +
                ", id=" + id +
                ", hubName='" + hubName + '\'' +
                ", vaccineDate=" + vaccineDate +
                ", vaccineType='" + vaccineType + '\'' +
                ", dose=" + dose +
                ", image=" + image +
                '}';
    }
}
