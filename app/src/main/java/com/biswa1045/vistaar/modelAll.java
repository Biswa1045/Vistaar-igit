package com.biswa1045.vistaar;

public class modelAll {
    String event_team_captain;
    String event_team_branch;
    String event_team_mobile;
    String event_team_rollno;
    String event_team;
    String event_email;


    public modelAll() {
    }

    public modelAll(String event_team_captain, String event_team_branch,String event_team_mobile,String event_team_rollno,String event_team,String event_email) {
        this.event_team_captain = event_team_captain;
        this.event_team_branch = event_team_branch;
        this.event_team_mobile = event_team_mobile;
        this.event_team_rollno = event_team_rollno;
        this.event_team = event_team;
        this.event_email = event_email;
    }

    public String getEvent_email() {
        return event_email;
    }

    public void setEvent_email(String event_email) {
        this.event_email = event_email;
    }

    public String getEvent_team_captain() {
        return event_team_captain;
    }

    public void setEvent_team_captain(String event_team_captain) {
        this.event_team_captain = event_team_captain;
    }

    public String getEvent_team_branch() {
        return event_team_branch;
    }

    public void setEvent_team_branch(String event_team_branch) {
        this.event_team_branch = event_team_branch;
    }

    public String getEvent_team_mobile() {
        return event_team_mobile;
    }

    public void setEvent_team_mobile(String event_team_mobile) {
        this.event_team_mobile = event_team_mobile;
    }

    public String getEvent_team_rollno() {
        return event_team_rollno;
    }

    public void setEvent_team_rollno(String event_team_rollno) {
        this.event_team_rollno = event_team_rollno;
    }

    public String getEvent_team() {
        return event_team;
    }

    public void setEvent_team(String event_team) {
        this.event_team = event_team;
    }
}
