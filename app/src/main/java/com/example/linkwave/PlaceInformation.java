package com.example.linkwave;

public class PlaceInformation {
    private String id = "";
    private String placeid = "";
    private String name = "";
    private String lat = "";
    private String lng = "";
    private String rating = "";
    private String vicinity = "";
    private String reference = "";
    private String image_reference = "";
    private String calculateDistence = "";
    public  PlaceInformation(){


    }

    @Override
    public String toString() {
        return "PlaceInformation{" +
                "id='" + id + '\'' +
                ", placeid='" + placeid + '\'' +
                ", name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", rating='" + rating + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", reference='" + reference + '\'' +
                ", image_reference='" + image_reference + '\'' +
                ", calculateDistence='" + calculateDistence + '\'' +
                '}';
    }

    public PlaceInformation(String id, String placeid, String name, String lat, String lng, String rating, String vicinity, String reference, String image_reference) {
        this.id = id;
        this.placeid = placeid;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.vicinity = vicinity;
        this.reference = reference;
        this.image_reference = image_reference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImage_reference() {
        return image_reference;
    }

    public void setImage_reference(String image_reference) {
        this.image_reference = image_reference;
    }
    public String getCalculateDistence() {
        return calculateDistence;
    }

    public void setCalculateDistence(String calculateDistence) {
        this.calculateDistence = calculateDistence;
    }
}
