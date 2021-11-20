package org.project.models;

import java.io.Serializable;

public class AdverseEvent implements Serializable {

    private String eventType;
    private String nickname;
    private int gravity;
    private String text;
    private String hubName;

    public AdverseEvent() {

    }

    public AdverseEvent(String eventType, String nickname, int gravity, String text, String hubName) {
        this.eventType = eventType;
        this.nickname = nickname;
        this.gravity = gravity;
        this.text = text;
        this.hubName = hubName;
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

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
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

