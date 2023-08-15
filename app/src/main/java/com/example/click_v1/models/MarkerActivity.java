package com.example.click_v1.models;

import java.util.Date;

public class MarkerActivity {
    public String creatorId, id, topic, title, description, creatorName;
    public int imagePoster, lat, lng;
    public Date dateTime;

    public User user;

    public MarkerActivity(String creatorId, String id, String topic, String title, String description, int lat, int lng, Date dateTime, User user) {
        this.creatorId = creatorId;
        this.id = id;
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.dateTime = dateTime;
        this.user = user;
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

    public int getLat() {
        return lat;
    }

    public int getLng() {
        return lng;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public User getUer() {
        return user;
    }

}
