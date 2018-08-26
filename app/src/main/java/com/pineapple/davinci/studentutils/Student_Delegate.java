package com.pineapple.davinci.studentutils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.resources.MyCallback;
import com.pineapple.davinci.resources.Singleton;
import com.pineapple.davinci.clubutils.Club;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mudit on 6/11/2018.
 */

public class Student_Delegate {

    public static Student readFromHashMap(HashMap<String,Object> studentDataMap) {
        Student student = new Student();


        String userID = (String) studentDataMap.get(Constants.DB_Cols_UserID);
        String firstName = (String) studentDataMap.get(Constants.DB_Cols_FirstName);
        String lastName = (String) studentDataMap.get(Constants.DB_Cols_LastName);
        int age = (int) studentDataMap.get(Constants.DB_Cols_Age);
        int grade = (int) studentDataMap.get(Constants.DB_Cols_Grade);
        String studID = (String) studentDataMap.get(Constants.DB_Cols_StudentID);
        String desc = (String) studentDataMap.get(Constants.DB_Cols_Description);
        String club = (String) studentDataMap.get(Constants.DB_Cols_Clubs);
        ArrayList<Club> clubList = new ArrayList<>();
        if(!club.isEmpty()) {
            while (club.contains(",")) {
                String clubName = club.substring(0, club.indexOf(","));
                club = club.substring(club.indexOf(",") + 1);
                clubList.add(new Club(clubName));
            }
            clubList.add(new Club(club));
        }
        String relStat = (String) studentDataMap.get(Constants.DB_Cols_Relationship);



        student.setMyUserID(userID);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setAge(age);
        student.setGrade(grade);
        student.setStudentID(studID);
        student.setDescription(desc);
        student.setClubList(clubList);
        student.setRelationshipStatus(relStat);

        return student;
    }

    public static void getStudentFromFirebase(final String userID, final MyCallback<Student> callback) {
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        final DatabaseReference studentDatabase = dbRef.child(Constants.DB_Tables_Students);
        final ValueEventListener checkForStudent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    Log.d("retrieve student","child exists");
                    studentDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Student student = dataSnapshot.getValue(Student.class);
                            if(student != null)
                                Log.d("retrieve student","student found and sent");
                            callback.accept(student);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {  }
                    });
                } else {
                    callback.accept(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { callback.accept(null); }
        };
        studentDatabase.addListenerForSingleValueEvent(checkForStudent);
    }

    public static void writeStudentToDB() {
        Student student = Singleton.getInstance().getCurrStudent();
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference studentDatabase = dbRef.child(Constants.DB_Tables_Students);
        studentDatabase.child(student.getMyUserID()).setValue(student);
    }

    public static void writeNewStudent() {
        Student student = Singleton.getInstance().getCurrStudent();
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference studentDatabase = dbRef.child(Constants.DB_Tables_Students);
        studentDatabase.child(student.getMyUserID()).setValue(student);
    }

}
