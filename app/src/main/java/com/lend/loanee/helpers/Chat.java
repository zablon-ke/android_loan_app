package com.lend.loanee.helpers;

public class Chat {

    String last_message;
    String sender;
    String send_time;
    String image_url="";
    String status="";

    public Chat(String last_message, String sender, String send_time, String image_url, String status) {
        this.last_message = last_message;
        this.sender = sender;
        this.send_time = send_time;
        this.image_url = image_url;
        this.status = status;
    }
    public String getLast_message() {
        return last_message;
    }

    public String getSender() {
        return sender;
    }

    public String getSend_time() {
        return send_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStatus() {
        return status;
    }
}
