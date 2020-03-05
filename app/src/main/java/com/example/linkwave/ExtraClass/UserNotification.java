package com.example.linkwave.ExtraClass;

public class UserNotification {
    String callNotifaction;
    String messageNotifaction;
    String messageNotifactionSender = "";

    public UserNotification(){
        callNotifaction = "0";
        messageNotifaction = "0";
        messageNotifactionSender = "";
    }

    public UserNotification(String callNotifaction, String messageNotifaction, String messageNotifactionSender) {
        this.callNotifaction = callNotifaction;
        this.messageNotifaction = messageNotifaction;
        this.messageNotifactionSender = messageNotifactionSender;
    }

    public String getCallNotifaction() {
        return callNotifaction;
    }

    public void setCallNotifaction(String callNotifaction) {
        this.callNotifaction = callNotifaction;
    }

    public String getMessageNotifaction() {
        return messageNotifaction;
    }

    public void setMessageNotifaction(String messageNotifaction) {
        this.messageNotifaction = messageNotifaction;
    }

    public String getMessageNotifactionSender() {
        return messageNotifactionSender;
    }

    public void setMessageNotifactionSender(String messageNotifactionSender) {
        this.messageNotifactionSender = messageNotifactionSender;
    }
}
