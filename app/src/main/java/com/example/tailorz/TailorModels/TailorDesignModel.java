package com.example.tailorz.TailorModels;

public class TailorDesignModel {

    String Design_url;
    String Design_name;
    String Design_id;
    String Tailor_username;


    public TailorDesignModel() {
    }

    public TailorDesignModel(String design_url, String design_name, String design_id, String tailor_username) {
        Design_url = design_url;
        Design_name = design_name;
        Design_id = design_id;
        Tailor_username = tailor_username;
    }

    public String getTailor_username() {
        return Tailor_username;
    }

    public void setTailor_username(String tailor_username) {
        Tailor_username = tailor_username;
    }

    public String getDesign_url() {
        return Design_url;
    }

    public void setDesign_url(String design_url) {
        Design_url = design_url;
    }

    public String getDesign_name() {
        return Design_name;
    }

    public void setDesign_name(String design_name) {
        Design_name = design_name;
    }

    public String getDesign_id() {
        return Design_id;
    }

    public void setDesign_id(String design_id) {
        Design_id = design_id;
    }
}
