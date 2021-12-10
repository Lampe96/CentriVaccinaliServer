package org.project.models;

import java.io.Serializable;

/**
 * In questa classe viene creato l'oggetto evento avverso, utilizzato
 * per il passaggio dei dati e per la scrittura su DB.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */

public class AdverseEvent implements Serializable {

    /**
     * Tipologia dell'evento avverso.
     */
    private String eventType;

    /**
     * Nickname del cittadino che lo ha creato.
     */
    private String nickname;

    /**
     * Severita' dell'evento avverso.
     */
    private Short gravity;

    /**
     * Testo dell'evento avverso.
     */
    private String text;

    /**
     * Nome del centro vaccinale presso cui si e' verificato.
     */
    private String hubName;

    /**
     * Costruttore vuoto.
     */
    public AdverseEvent() {
    }

    /**
     * Utilizzato per prendere la tipologia dell'evento avverso.
     *
     * @return tipologia dell'evento avverso
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Utilizzato per impostare la tipologia dell'evento avverso.
     *
     * @param eventType tipologia evento avverso
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Utilizzato per prendere il nickname del cittadino che lo ha creato.
     *
     * @return nickname del cittadino
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Utilizzato per impostare il nickname del cittadino che ha creato l'evento.
     *
     * @param nickname nickname da impostare
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Utilizzato per prendere la severita'.
     *
     * @return severita' dell'evento avverso
     */
    public Short getGravity() {
        return gravity;
    }

    /**
     * Utilizzato per impostare la severita' dell'evento avverso.
     *
     * @param gravity severita' da impostare
     */
    public void setGravity(Short gravity) {
        this.gravity = gravity;
    }

    /**
     * Utilizzato per prendere il testo.
     *
     * @return testo dell'evento avverso
     */
    public String getText() {
        return text;
    }

    /**
     * Utilizzato per impostare il testo dell'evento avverso.
     *
     * @param text testo da impostare
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Utilizzato per prendere il nome del centro.
     *
     * @return nome del centro vaccinale presso cui si e' verificato
     */
    public String getHubName() {
        return hubName;
    }

    /**
     * Utilizzato per impostare il nome del centro vaccinale.
     *
     * @param hubName nome del centro da impostare
     */
    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    /**
     * Utilizzato per effettuare la concatenazione in string.
     *
     * @return concatenazione di tutti i parametri
     */
    @Override
    public String toString() {
        return "AdverseEvent{" +
                "eventType='" + eventType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gravity=" + gravity +
                ", text='" + text + '\'' +
                ", hubName='" + hubName + '\'' +
                '}';
    }
}
