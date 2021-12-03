package org.project.models;

import java.io.Serializable;

/**
 * In questa classe viene creato l'oggetto centro vaccinale, utilizzato
 * per i passaggio dei dati e per la scrittura su DB
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class Hub implements Serializable {

    /**
     * Nome del centro vaccinale
     */
    private String nameHub;

    /**
     * password del centro vaccinale
     */
    private String password;

    /**
     * indirizzo del centro vaccinale
     */
    private Address address;

    /**
     * Tipologia di centro vaccinale
     */
    private String type;

    /**
     * RIferimento dell'immagine scelta dal centro vaccinale
     */
    private int image;

    /**
     * Costruttore vuoto
     */
    public Hub() {
    }

    /**
     * Utilizzato per prendere il numero dell'immagine
     *
     * @return numero di riferimento dell'immagine
     */
    public int getImage() {
        return image;
    }

    /**
     * Utilizzato per impostare il numero dell'immagine
     *
     * @param image numero di riferimento dell'immagine
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * Utilizzato per prendere il nome
     *
     * @return nome del centro vaccinale
     */
    public String getNameHub() {
        return nameHub;
    }

    /**
     * Utilizzato per impostare il nome del centro vaccinale
     *
     * @param nameHub nome da impostare
     */
    public void setNameHub(String nameHub) {
        this.nameHub = nameHub;
    }

    /**
     * Utilizzato per prendere la password
     *
     * @return password del centro vaccinale
     */
    public String getPassword() {
        return password;
    }

    /**
     * Utilizzato per impostare la password del centro vaccinale
     *
     * @param password password da impostare
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Utilizzato per prendere l'indirizzo
     *
     * @return indirizzo del centro vaccinale
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Utilizzato per impostare l'indirizzo del centro vaccinale
     *
     * @param address indirizzo da impostare
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Utilizzato per prendere la tipologia del centro vaccinale
     *
     * @return tipologia del centro vaccinale
     */
    public String getType() {
        return type;
    }

    /**
     * Utilizzato per impostare la tipologia del centro vaccinale
     *
     * @param type tipologia centro vaccinale
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Utilizzato per effettuare la concatezione in string
     *
     * @return concatezione di tutti i parametri
     */
    @Override
    public String toString() {
        return "Hub{" +
                "nameHub='" + nameHub + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                ", type='" + type + '\'' +
                ", image=" + image +
                '}';
    }
}
