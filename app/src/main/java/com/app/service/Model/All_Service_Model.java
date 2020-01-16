package com.app.service.Model;

public class All_Service_Model {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private String thumbnail;

    public All_Service_Model() {
    }

    public All_Service_Model(String id, String name, String thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
