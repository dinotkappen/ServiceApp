package com.app.service.Model;

public class Sub_Menu_Model {

    String sub_item_id;

    String sub_item_thumbnail;

    public Sub_Menu_Model() {
    }

    public Sub_Menu_Model(String sub_item_id, String sub_item_thumbnail, String tile_main,  String descr) {
        this.sub_item_id = sub_item_id;
        this.sub_item_thumbnail = sub_item_thumbnail;
        this.tile_main = tile_main;
        this.descr = descr;


    }
    public String getSub_item_id() {
        return sub_item_id;
    }

    public void setSub_item_id(String sub_item_id) {
        this.sub_item_id = sub_item_id;
    }

    public String getSub_item_thumbnail() {
        return sub_item_thumbnail;
    }

    public void setSub_item_thumbnail(String sub_item_thumbnail) {
        this.sub_item_thumbnail = sub_item_thumbnail;
    }

    public String getTile_main() {
        return tile_main;
    }

    public void setTile_main(String tile_main) {
        this.tile_main = tile_main;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    String tile_main;
    String descr;
}
