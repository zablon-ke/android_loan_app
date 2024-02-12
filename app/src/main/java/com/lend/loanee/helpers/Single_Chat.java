package com.lend.loanee.helpers;

public class Single_Chat {

    String sender_id="";
    String receiver_id="";
    String message="";
    String send_time="";
    String status="";

    public Single_Chat(String sender_id, String receiver_id, String message, String send_time, String status) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.send_time = send_time;
        this.status = status;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public String getSend_time() {
        return send_time;
    }

    public String getStatus() {
        return status;
    }
}
