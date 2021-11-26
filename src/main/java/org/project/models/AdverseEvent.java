package org.project.models;

import java.io.Serializable;

public class AdverseEvent implements Serializable {

    private String eventType;
    private String nickname;
    private Short gravity;
    private String text;
    private String hubName;

    public AdverseEvent() {

    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Short getGravity() {
        return gravity;
    }

    public void setGravity(Short gravity) {
        this.gravity = gravity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

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

