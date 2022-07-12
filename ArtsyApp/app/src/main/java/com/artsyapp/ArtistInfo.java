package com.hw9.artsyapp;

public class ArtistInfo {

    private String name;
    private String birthday;
    private String deathday;
    private String nationality;
    private String bio;

    public ArtistInfo(String name, String birthday, String deathday, String nationality, String bio) {
        this.name = name;
        this.birthday = birthday;
        this.deathday = deathday;
        this.nationality = nationality;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getNationality() {
        return nationality;
    }

    public String getBio() {
        return bio;
    }
}