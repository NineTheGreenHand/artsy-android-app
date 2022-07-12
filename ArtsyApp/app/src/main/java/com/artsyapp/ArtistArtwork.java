package com.hw9.artsyapp;

public class ArtistArtwork {

    private String artwork_id;
    private String artwork_name;
    private String artwork_date;
    private String artwork_picURL;

    public ArtistArtwork(String artwork_id, String artwork_name, String artwork_date, String artwork_picURL) {
        this.artwork_id = artwork_id;
        this.artwork_name = artwork_name;
        this.artwork_date = artwork_date;
        this.artwork_picURL = artwork_picURL;
    }

    public String getArtworkId() {
        return artwork_id;
    }

    public String getArtworkName() {
        return artwork_name;
    }

    public String getArtworkDate() {
        return artwork_date;
    }

    public String getArtworkPicURL() {
        return artwork_picURL;
    }
}