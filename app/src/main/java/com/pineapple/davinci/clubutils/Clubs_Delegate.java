package com.pineapple.davinci.clubutils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.resources.MyCallback;
import com.pineapple.davinci.resources.Singleton;

import java.util.ArrayList;

/**
 * Created by Mudit on 8/1/2018.
 */

public class Clubs_Delegate {

    public static void getClub(final String clubName, final MyCallback<Club> callback) {
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference clubDatabase = dbRef.child(Constants.DB_Tables_Clubs).child(clubName);
        clubDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Club club = snapshot.getValue(Club.class);
                if(club != null)
                    Log.d("retrieve club",clubName + " found and sent");
                callback.accept(club);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { callback.accept((null)); }
        });
    }

    public static void getClubList(final MyCallback<ArrayList<String>> callback) {
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference clubDatabase = dbRef.child(Constants.DB_Tables_Clubs);
        clubDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> clubNames = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    clubNames.add(ds.getKey());
                }
                callback.accept(clubNames);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });
    }

    public static void getClubColors(final String clubName, final MyCallback<int[]> callback) {
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference clubDatabase = dbRef.child(Constants.DB_Tables_Clubs).child(clubName);
        clubDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Club club = new Club(clubName);//snapshot.getValue(Club.class);
                //if(club.getClubColors() != null) {
                if(false) {
                    Log.d("club color", clubName + " found");
                    String[] clubColors = club.getClubColors();
                    int[] colors = {Color.parseColor(clubColors[0]),Color.parseColor(clubColors[1])};
                    callback.accept(colors);
                } else {
                    Log.d("club color","club error - using default");
                    int[] colors = Constants.defaultClubGradient.clone();
                    String[] clubColors = new String[2];
                    for(int i = 0; i < 2; i++) {
                        int A = (colors[i] >> 24) & 0xff; // or color >>> 24
                        int R = (colors[i] >> 16) & 0xff;
                        int G = (colors[i] >>  8) & 0xff;
                        int B = (colors[i]      ) & 0xff;
                        String a = "" + Integer.toHexString(A);
                        String r = "" + Integer.toHexString(R);
                        String g = "" + Integer.toHexString(G);
                        String b = "" + Integer.toHexString(B);
                        clubColors[i] = "#" + a+r+g+b;
                    }
                    club.setClubColors(clubColors);
                    callback.accept(Constants.defaultClubGradient.clone());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { callback.accept(Constants.defaultClubGradient.clone()); }
        });
    }

    public static void writeNewClub(Club club) {
        DatabaseReference dbRef = Singleton.getInstance().getDatabaseRef();
        DatabaseReference clubDatabase = dbRef.child(Constants.DB_Tables_Clubs);
        clubDatabase.child(club.getNameString()).setValue(club);
    }

}
