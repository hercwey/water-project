package com.learnbind.ai.model.iot.deviceDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Location_ {

    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("accuracy")
    private String accuracy;
    @JsonProperty("time")
    private String time;
    @JsonProperty("description")
    private String description;
    @JsonProperty("region")
    private String region;
    @JsonProperty("heading")
    private String heading;
    @JsonProperty("vehicleSpeed")
    private String vehicleSpeed;
    @JsonProperty("crashInformation")
    private String crashInformation;
    @JsonProperty("numberOfPassengers")
    private String numberOfPassengers;
    @JsonProperty("language")
    private String language;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(String vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public String getCrashInformation() {
        return crashInformation;
    }

    public void setCrashInformation(String crashInformation) {
        this.crashInformation = crashInformation;
    }

    public String getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(String numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}