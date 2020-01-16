package com.app.service.Model;

public class SearchModel {

    private String searchID;
    private String searchNameme;
    private String searchImage;

    public SearchModel() {
    }

    public String getSearchID() {
        return searchID;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }

    public String getSearchNameme() {
        return searchNameme;
    }

    public void setSearchNameme(String searchNameme) {
        this.searchNameme = searchNameme;
    }

    public String getSearchImage() {
        return searchImage;
    }

    public void setSearchImage(String searchImage) {
        this.searchImage = searchImage;
    }

    public SearchModel(String searchID, String searchNameme, String searchImage) {
        this.searchID = searchID;
        this.searchNameme = searchNameme;
        this.searchImage=searchImage;
    }
}
