package com.example.click_v1.models;

import java.io.Serializable;
import java.util.Date;

public class MarkerActivity implements Serializable {
    public String creatorId, id, topic, title, description, creatorName, distance, city,
            name, creatorImage, token, selfIntroduction, location, country, gender, email, dateTime;
    public double lat, lng;
    public Date dateObject;


    public MarkerActivity() {
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getName() {
        return name;
    }

    public String getCreatorImage() {
        return creatorImage;
    }

    public String getToken() {
        return token;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setDateObject(Date dateTime) {
        this.dateObject = dateObject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatorImage(String creatorImage) {
        this.creatorImage = creatorImage;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}