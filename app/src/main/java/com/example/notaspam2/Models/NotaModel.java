package com.example.notaspam2.Models;

public class NotaModel {

    private int _id;
    private String title;
    private String description;
    private String fbId;

    public NotaModel() {
    }

    public NotaModel(int _id, String title, String description, String fbId) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.fbId = fbId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }
}
