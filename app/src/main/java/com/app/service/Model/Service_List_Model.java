package com.app.service.Model;

public class Service_List_Model {

    String servie_id;
    String service_name;

    public String getServie_id() {
        return servie_id;
    }

    public void setServie_id(String servie_id) {
        this.servie_id = servie_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_icon() {
        return service_icon;
    }

    public void setService_icon(String service_icon) {
        this.service_icon = service_icon;
    }

    String service_icon;

    public Service_List_Model() {
    }

    public Service_List_Model(String servie_id, String service_name, String service_icon) {

        this.servie_id = servie_id;
        this.service_name = service_name;
        this.service_icon = service_icon;
    }
}
