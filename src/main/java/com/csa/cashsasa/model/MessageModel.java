package com.csa.cashsasa.model;

public class MessageModel
{
    private String number;
    private String body;
    private String id;
    private String date;

    public void setBody(String body) {
        this.body = body;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public String getNumber() {
        return number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}