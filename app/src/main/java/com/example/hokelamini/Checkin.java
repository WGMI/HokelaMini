package com.example.hokelamini;

public class Checkin {

    long id;
    long campaign_id;
    String location;

    public Checkin(){}

    public Checkin(long id, long campaign_id, String location) {
        this.id = id;
        this.campaign_id = campaign_id;
        this.location = location;
    }

    public Checkin(long campaign_id, String location) {
        this.campaign_id = campaign_id;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(long campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
