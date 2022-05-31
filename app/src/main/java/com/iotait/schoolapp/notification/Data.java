package com.iotait.schoolapp.notification;

import java.util.List;

public class Data {

    //this model use for fetching api data
    private String sender;
    private String receiver;
    private int icon;
    private String body;
    private String title;
    private String type;
    private String senderName;
    private String senderProfile;
    private String messageType;

    public Data() {
    }


    public Data(String sender, String receiver, int icon, String body, String title, String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.type = type;
    }

    public Data(String sender, String receiver, String senderName, String senderProfile, int icon, String body,
                String title, String type, String messageType) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderName = senderName;
        this.senderProfile = senderProfile;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.type = type;
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(String senderProfile) {
        this.senderProfile = senderProfile;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

}
