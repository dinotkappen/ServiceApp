package com.app.service.Model;

public class Notification_Model {
    String title;
    String msg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public Notification_Model(String title, String msg,String date) {

        this.title = title;
        this.msg = msg;
        this.date = date;
    }
}
