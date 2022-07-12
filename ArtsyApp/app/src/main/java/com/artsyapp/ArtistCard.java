package com.hw9.artsyapp;

public class ArtistCard {

    private String name;
    private String id;
    private String picURL;

    public ArtistCard(String name, String id, String picURL) {
        this.name = name;
        this.id = id;
        this.picURL = picURL;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPicURL() {
        return picURL;
    }
}
