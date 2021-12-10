package org.project.models;

import java.io.Serializable;

/**
 * In questa classe viene creato l'oggetto indirizzo,
 * utilizzato per impostare il campo nell'oggetto
 * centro vaccinali, nella classe {@link Hub}.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class Address implements Serializable {

    /**
     * Qualificatore dell'indirizzo.
     */
    private String qualificator;

    /**
     * Nome della via.
     */
    private String address;

    /**
     * Numero civico dell'indirizzo.
     */
    private String number;

    /**
     * Citta' presso cui si trova.
     */
    private String city;

    /**
     * Cap del comune.
     */
    private String cap;

    /**
     * Provincia.
     */
    private String province;

    /**
     * Costruttore vuoto.
     */
    public Address() {
    }

    /**
     * Utilizzato per prendere il qualificatore dell'indirizzo.
     *
     * @return qualificatore
     */
    public String getQualificator() {
        return qualificator;
    }

    /**
     * Utilizzato per impostare il qualificatore dell'indirizzo.
     *
     * @param qualificator qualificatore da impostare
     */
    public void setQualificator(String qualificator) {
        this.qualificator = qualificator;
    }

    /**
     * Utilizzato per prendere il nome della via.
     *
     * @return nome della via
     */
    public String getAddress() {
        return address;
    }

    /**
     * Utilizzato per impostare il nome della via.
     *
     * @param address nome della via
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Utilizzato per prendere il numero civico dell'indirizzo.
     *
     * @return numero civico
     */
    public String getNumber() {
        return number;
    }

    /**
     * Utilizzato per impostare il numero civico.
     *
     * @param number numero civico dell'indirizzo
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Utilizzato per prendere la citta'.
     *
     * @return citta' dove si trova il centro
     */
    public String getCity() {
        return city;
    }

    /**
     * Utilizzato per impostare la citta' dove si trova il centro
     *
     * @param city nome della citta'.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Utilizzato per prendere il cap del comune.
     *
     * @return cap del comune
     */
    public String getCap() {
        return cap;
    }

    /**
     * Utilizzato per impostare il cap del comune.
     *
     * @param cap cap del comune
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * Utilizzato per prendere la provincia dove si trova il centro.
     *
     * @return provincia
     */
    public String getProvince() {
        return province;
    }

    /**
     * Utilizzato per impostare la provincia.
     *
     * @param province provincia dove si trova il centro
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Utilizzato per effettuare la concatenazione in string.
     *
     * @return concatenazione di tutti i parametri
     */
    public String toStringCustom() {
        return qualificator + " " + address + " " + number + ", " + city + " " + cap + " (" + province + ")";
    }

    /**
     * Utilizzato per effettuare la concatenazione in string.
     *
     * @return concatenazione di tutti i parametri
     */
    @Override
    public String toString() {
        return "Address{" +
                "qualificator='" + qualificator + '\'' +
                ", address='" + address + '\'' +
                ", number='" + number + '\'' +
                ", city='" + city + '\'' +
                ", cap='" + cap + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
