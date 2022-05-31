package com.iotait.schoolapp.ui.homepage.ui.chat.models;

import java.util.List;

public class PublicRooms {
    private String roomId;
    private String roomName;
    private String roomCoverImage;
    private List<String> roomMembers;
    private List<String> moderators;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomCoverImage() {
        return roomCoverImage;
    }

    public void setRoomCoverImage(String roomCoverImage) {
        this.roomCoverImage = roomCoverImage;
    }

    public List<String> getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(List<String> roomMembers) {
        this.roomMembers = roomMembers;
    }

    public List<String> getModerators() {
        return moderators;
    }

    public void setModerators(List<String> moderators) {
        this.moderators = moderators;
    }
}
