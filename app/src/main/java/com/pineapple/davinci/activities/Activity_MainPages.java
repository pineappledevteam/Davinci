package com.pineapple.davinci.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.BottomNavigationViewHelper;
import com.pineapple.davinci.resources.Constants;

import java.util.HashMap;

public class Activity_MainPages extends AppCompatActivity
        implements Fragment_Dashboard.OnFragmentInteractionListener,
        Fragment_Calendar.OnFragmentInteractionListener,
        Fragment_eClass.OnFragmentInteractionListener,
        Fragment_Clubs.OnFragmentInteractionListener,
        Fragment_Profile.OnFragmentInteractionListener,
        Fragment_ClubPage.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigationView;
    Fragment dashboardDisplayFragment;
    Fragment calendarDisplayFragment;
    Fragment eClassDisplayFragment;
    Fragment clubsDisplayFragment;
    Fragment profileDisplayFragment;


    Fragment_Dashboard dashboardFragment;
    Fragment_Calendar calendarFragment;
    Fragment_eClass eClassFragment;
    Fragment_Clubs clubsFragment;
    HashMap<String,Fragment_ClubPage> clubPageFragmentList = new HashMap<>();
    String currClub;
    Fragment_Profile profileFragment;

    final FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pages);
        //initialize fragments
        //dashboard fragments
        dashboardFragment = new Fragment_Dashboard();
        //calendar fragments
        calendarFragment = new Fragment_Calendar();
        //eclass fragments
        eClassFragment = new Fragment_eClass();
        //clubs fragments
        clubsFragment = new Fragment_Clubs();
        //clubPageFragmentList = new Fragment_ClubPage();
        //profile fragments
        profileFragment = new Fragment_Profile();
        //end fragments

        //set display fragments
        dashboardDisplayFragment = dashboardFragment;
        calendarDisplayFragment = calendarFragment;
        eClassDisplayFragment = eClassFragment;
        clubsDisplayFragment = clubsFragment;
        profileDisplayFragment = profileFragment;


        fragmentManager.beginTransaction().add(R.id.fragMainContainer, calendarDisplayFragment, "calendar fragment").hide(calendarDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, eClassDisplayFragment, "eClass fragment").hide(eClassDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, clubsDisplayFragment, "clubs fragment").hide(clubsDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, profileDisplayFragment, "profile fragment").hide(profileDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, dashboardDisplayFragment, "dashboard fragment").show(dashboardDisplayFragment).commit();



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(2);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, dashboardDisplayFragment).show(dashboardDisplayFragment).addToBackStack("dashboard fragment").commit();
                        return true;
                    case R.id.navigation_calendar:
                        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, calendarDisplayFragment).show(calendarDisplayFragment).addToBackStack("calendar fragment").commit();
                        return true;
                    case R.id.navigation_eclass:
                        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, eClassDisplayFragment).show(eClassDisplayFragment).addToBackStack("eClass fragment").commit();
                        return true;
                    case R.id.navigation_clubs:
                        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, clubsDisplayFragment).show(clubsDisplayFragment).addToBackStack("clubs fragment").commit();
                        return true;
                    case R.id.navigation_profile:
                        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, profileDisplayFragment).show(profileDisplayFragment).addToBackStack("profile fragment").commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void updateNavBar(String fragType) {
        switch (fragType) {
            case Constants.FRAG_DASHBOARD:
                dashboardDisplayFragment = dashboardFragment;
                updateNavBar(R.id.navigation_dashboard);
                break;
            case Constants.FRAG_CALENDAR:
                calendarDisplayFragment = calendarFragment;
                updateNavBar(R.id.navigation_calendar);
                break;
            case Constants.FRAG_ECLASS:
                eClassDisplayFragment = eClassFragment;
                updateNavBar(R.id.navigation_eclass);
                break;
            case Constants.FRAG_CLUB_PAGE:
                clubsDisplayFragment = clubPageFragmentList.get(currClub);
                updateNavBar(R.id.navigation_clubs);
                break;
            case Constants.FRAG_CLUBS:
                clubsDisplayFragment = clubsFragment;
                updateNavBar(R.id.navigation_clubs);
                break;
            case Constants.FRAG_PROFILE:
                profileDisplayFragment = profileFragment;
                updateNavBar(R.id.navigation_profile);
                break;
        }
    }

    @Override
    public void goToClub(String clubName) {
        selectClub(clubName);
        fragmentManager.beginTransaction().replace(R.id.fragMainContainer, clubsDisplayFragment).show(clubsDisplayFragment).addToBackStack("clubs fragment").commit();
    }

    @Override
    public void selectClub(String clubName) {
        Log.d("select club",clubPageFragmentList.size()+"");
        if (!clubPageFragmentList.keySet().contains(clubName)) {
            Fragment_ClubPage newClubFragment = Fragment_ClubPage.newInstance(clubName);
            clubPageFragmentList.put(clubName, newClubFragment);
        }
        clubsDisplayFragment = clubPageFragmentList.get(clubName);
        currClub = clubName;
    }


    public void updateNavBar(@IdRes int itemId) {
        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
    }
}
