package com.biswa1045.vistaar;

public class retrieveModelevent {

    String event_photo;
    String event_name;


    public retrieveModelevent() {
    }

    public retrieveModelevent(String event_photo,String event_name) {
        this.event_photo = event_photo;
        this.event_name = event_name;
    }

    public String getEvent_photo() {
        return event_photo;
    }

    public void setEvent_photo(String event_photo) {
        this.event_photo = event_photo;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}
