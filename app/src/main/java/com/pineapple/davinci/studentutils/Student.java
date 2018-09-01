package com.pineapple.davinci.studentutils;

import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.clubutils.Club;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mudit on 6/10/2018.
 */

public class Student {

    private String myUserID;
    private String firstName;
    private String lastName;
    private int age;
    private int grade;
    private String studentID;
    private String description;
    private ArrayList<Club> clubList;
    private String relationshipStatus;

    public Student() {
    }

    public String toString() {
        return "Firebase Uid:" + myUserID + ", name: " + firstName + " " + lastName + ", studentID: " + studentID + ", age: " + age + ", grade: " + grade;
    }

    public String getFirstName() {
        return firstName;
    }
    void setFirstName(String firstName) { //only writable by DB access
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    void setLastName(String lastName) { //only writable by DB access
        this.lastName = lastName;
    }

    public String getMyUserID() {
        return myUserID;
    }
    void setMyUserID(String myUserID) { //only writable by DB access
        this.myUserID = myUserID;
    }

    public int getAge() { return age; }
    void setAge(int age) { this.age = age; }

    public int getGrade() { return grade; }
    void setGrade(int grade) { this.grade = grade; }

    public String getStudentID() { return studentID; }
    void setStudentID(String studentID) { this.studentID = studentID; }

    public String getDescription() { return description; }
    void setDescription(String description) { this.description = description; }

    public String getRelationshipStatus() { return relationshipStatus; }
    void setRelationshipStatus(String relationshipStatus) { this.relationshipStatus = relationshipStatus; }

    public ArrayList<Club> getClubList() { return clubList; }
    public void setClubList(ArrayList<Club> clubList) { this.clubList = clubList; }

    HashMap<String, Object> getDataMapForDB() {
        HashMap<String, Object> data = new HashMap<>();
        //data.put(Constants.DB_Cols_UserID,myUserID);
        data.put(Constants.DB_Cols_FirstName,firstName);
        data.put(Constants.DB_Cols_LastName,lastName);
        data.put(Constants.DB_Cols_Age,age);
        data.put(Constants.DB_Cols_Grade,grade);
        data.put(Constants.DB_Cols_StudentID,studentID);
        data.put(Constants.DB_Cols_Description,description);
        String clubListString = "";
        for(Club club: clubList) {
            clubListString += club.getNameString() + ",";
        }
        if(clubListString.contains(",")) {
            clubListString = clubListString.substring(0, clubListString.lastIndexOf(","));
        }
        data.put(Constants.DB_Cols_Clubs,clubListString);
        data.put(Constants.DB_Cols_Relationship,relationshipStatus);

        return data;
    }
}
