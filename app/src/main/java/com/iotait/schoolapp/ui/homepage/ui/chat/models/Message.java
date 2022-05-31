package com.iotait.schoolapp.ui.homepage.ui.chat.models;

import java.util.HashMap;
import java.util.List;

public class Message {
    public String MessageId;
    public String SentTime;
    public String Msg;
    public String MsgType;
    public String SenderName;
    public String SenderId;
    public String Status;
    public List<String> URLS;
    public String Profile;
    public String ReadStatus;

    public Message(String messageId,String sentTime,String msg,String msgType,String senderName,String senderId,
                   String status,List<String> urls,String profile, String readStatus){

        this.MessageId = messageId;
        this.SentTime  = sentTime;
        this.Msg = msg;
        this.MsgType  = msgType;
        this.SenderName = senderName;
        this.SenderId = senderId;
        this.Status    = status;
        this.URLS  = urls;
        this.Profile = profile;
        this.ReadStatus   = readStatus;
    }
}
