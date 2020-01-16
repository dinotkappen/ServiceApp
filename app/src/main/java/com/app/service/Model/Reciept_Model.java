package com.app.service.Model;

public class Reciept_Model {
    String reciept_id;
    String reciept_date;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    String country_id;

    public String getReieptReference() {
        return reieptReference;
    }

    public void setReieptReference(String reieptReference) {
        this.reieptReference = reieptReference;
    }

    String reieptReference;

    public String getReciept_id() {
        return reciept_id;
    }

    public void setReciept_id(String reciept_id) {
        this.reciept_id = reciept_id;
    }

    public String getReciept_date() {
        return reciept_date;
    }

    public void setReciept_date(String reciept_date) {
        this.reciept_date = reciept_date;
    }

    public String getReceipt_price() {
        return receipt_price;
    }

    public void setReceipt_price(String receipt_price) {
        this.receipt_price = receipt_price;
    }

    public String getReciept_status() {
        return reciept_status;
    }

    public void setReciept_status(String reciept_status) {
        this.reciept_status = reciept_status;
    }

    String receipt_price;
    String reciept_status;

    public Reciept_Model(String reciept_id,String reieptReference, String reciept_date,String receipt_price,String reciept_status,String country_id) {

        this.reciept_id = reciept_id;
        this.reieptReference=reieptReference;
        this.reciept_date = reciept_date;
        this.receipt_price = receipt_price;
        this.reciept_status = reciept_status;
        this.country_id=country_id;
    }
}
