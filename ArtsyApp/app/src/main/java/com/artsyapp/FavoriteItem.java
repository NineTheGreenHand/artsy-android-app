package com.hw9.artsyapp;

public class FavoriteItem {

    private String favName;
    private String favNationality;
    private String favBirthday;
    private String favId;

    public FavoriteItem(String favName, String favNationality, String favBirthday, String favId) {
        this.favName = favName;
        this.favNationality = favNationality;
        this.favBirthday = favBirthday;
        this.favId = favId;
    }

    public String getFavName() {
        return favName;
    }

    public String getFavNationality() {
        return favNationality;
    }

    public String getFavBirthday() {
        return favBirthday;
    }

    public String getFavId() {
        return favId;
    }
}
