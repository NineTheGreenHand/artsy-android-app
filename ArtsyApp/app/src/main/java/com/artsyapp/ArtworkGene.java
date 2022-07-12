package com.hw9.artsyapp;

public class ArtworkGene {

    private String gene_name;
    private String gene_picURL;
    private String gene_description;

    public ArtworkGene(String gene_name, String gene_picURL, String gene_description) {
        this.gene_name = gene_name;
        this.gene_picURL = gene_picURL;
        this.gene_description = gene_description;
    }

    public String getGeneName() {
        return gene_name;
    }

    public String getGenePicURL() {
        return gene_picURL;
    }

    public String getGeneDescription() {
        return gene_description;
    }
}