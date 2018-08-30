package com.pineapple.davinci.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.BottomNavigationViewHelper;
import com.pineapple.davinci.resources.Singleton;

public class Activity_MainPages extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    final Fragment dashboardFragment = new Fragment_Dashboard();
    final Fragment calendarFragment = new Fragment_Calendar();
    final Fragment eClassFragment = new Fragment_eClass();
    final Fragment clubsFragment = new Fragment_Clubs();
    final Fragment profileFragment = new Fragment_Profile();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment previous;
    Fragment active = dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pages);

        fragmentManager.beginTransaction().add(R.id.fragMainContainer, calendarFragment, "calendar fragment").hide(calendarFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, eClassFragment, "eClass fragment").hide(eClassFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, clubsFragment, "clubs fragment").hide(clubsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, profileFragment, "profile fragment").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, dashboardFragment, "dashboard fragment").commit();




        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_dashboard:
                        fragmentManager.beginTransaction().hide(active).show(dashboardFragment).commit();
                        active = dashboardFragment;
                        return true;
                    case R.id.navigation_calendar:
                        fragmentManager.beginTransaction().hide(active).show(calendarFragment).commit();
                        active = calendarFragment;
                        return true;
                    case R.id.navigation_eclass:
                        fragmentManager.beginTransaction().hide(active).show(eClassFragment).commit();
                        active = eClassFragment;
                        return true;
                    case R.id.navigation_clubs:
                        fragmentManager.beginTransaction().hide(active).show(clubsFragment).commit();
                        active = clubsFragment;
                        return true;
                    case R.id.navigation_profile:
                        fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                        active = profileFragment;
                        return true;
                }
            return false;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

}
