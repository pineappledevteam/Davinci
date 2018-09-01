package com.pineapple.davinci.clubutils;

import java.util.ArrayList;

/**
 * Created by Mudit on 8/10/2018.
 */

public class Club {

    private String nameString;
    private ArrayList<String> clubColors;
    private int imageResource;
    private boolean isFavorite = false;

    private ArrayList<String> sponsors;
    private ArrayList<ClubMember> members;
    private ArrayList<Event> events;
    //private ArrayList<Polls> polls; TODO: make polls
    //teams
    ////dynamic teams controlled by officer


    public Club() {  }
    public Club(String name) {
        nameString = name;
    }

    public String getNameString() { return nameString; }
    void setNameString(String nameString) { this.nameString = nameString; }

    public ArrayList<String> getClubColors() { return clubColors; }
    void setClubColors(ArrayList<String> clubColors) { this.clubColors = clubColors; }

    public ArrayList<ClubMember> getMembers() { return members; }
    void setMembers(ArrayList<ClubMember> members) { this.members = members; }

    public ArrayList<Event> getEvents() { return events; }
    void setEvents(ArrayList<Event> events) { this.events = events; }

    public ArrayList<String> getSponsors() {
        return sponsors;
    }
    void setSponsors(ArrayList<String> sponsors) {
        this.sponsors = sponsors;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    void toggleFavorite() {
        isFavorite = !isFavorite;
    }
}
