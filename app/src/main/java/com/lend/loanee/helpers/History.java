package com.lend.loanee.helpers;

public class History {

    String title;
    String content;


    public History(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
