package com.biswa1045.vistaar;

public class event {

    String event_location;
    String event_name;
    String event_photo;
    String event_time;

    public event( ) {
    }
    public event(String event_location,String event_name,String event_photo,String event_time) {

        this.event_location = event_location;
        this.event_name = event_name;
        this.event_photo = event_photo;
        this.event_time = event_time;
    }



    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_photo() {
        return event_photo;
    }

    public void setEvent_photo(String event_photo) {
        this.event_photo = event_photo;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }
}
