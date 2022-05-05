package com.illumo.chat;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id;
    private String createdAt;
    private String title;
    private String userId;
    private List<String> participants = new ArrayList();
    private boolean isPrivate;
    private boolean isDialogue;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isDialogue() {
        return isDialogue;
    }
}
