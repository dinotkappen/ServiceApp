package com.app.service.Model;

public class Banner_Img_Model {
String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;

    public Banner_Img_Model() {
    }

    public Banner_Img_Model(String id, String thumbnail) {

        this.thumbnail = thumbnail;
        this.id = id;
    }
}



