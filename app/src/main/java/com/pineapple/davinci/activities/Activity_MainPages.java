package com.pineapple.davinci.activities;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.BottomNavigationViewHelper;
import com.pineapple.davinci.resources.Constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    Fragment active;

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

        active = dashboardDisplayFragment;

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
                        fragmentManager.beginTransaction().hide(active).show(dashboardDisplayFragment).addToBackStack("dashboard fragment").commit();
                        active = dashboardDisplayFragment;
                        return true;
                    case R.id.navigation_calendar:
                        fragmentManager.beginTransaction().hide(active).show(calendarDisplayFragment).addToBackStack("calendar fragment").commit();
                        active = calendarDisplayFragment;
                        return true;
                    case R.id.navigation_eclass:
                        fragmentManager.beginTransaction().hide(active).show(eClassDisplayFragment).addToBackStack("eClass fragment").commit();
                        active = eClassDisplayFragment;
                        return true;
                    case R.id.navigation_clubs:
                        fragmentManager.beginTransaction().hide(active).show(clubsDisplayFragment).addToBackStack("clubs fragment").commit();
                        active = clubsDisplayFragment;
                        return true;
                    case R.id.navigation_profile:
                        fragmentManager.beginTransaction().hide(active).show(profileDisplayFragment).addToBackStack("profile fragment").commit();
                        active = profileDisplayFragment;
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void updateNavBar(String fragType) {
        compareActive(active,fragType);
        int numActivities = fragmentManager.getBackStackEntryCount();
        StringBuilder toPrint = new StringBuilder("Fragment stack: ");
        for(int i = 0; i < numActivities; i++) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            toPrint.append(backStackEntry.getName()).append(", ");
        }
        if(toPrint.toString().contains(","))
            toPrint = new StringBuilder(toPrint.substring(0, toPrint.lastIndexOf(",")));
        else
            toPrint = new StringBuilder("Fragment stack: no activities");
        Log.d("fragment stack", toPrint.toString());

        switch (fragType) {
            case Constants.FRAG_DASHBOARD:
                dashboardDisplayFragment = dashboardFragment;
                active = dashboardDisplayFragment;
                updateNavBar(R.id.navigation_dashboard);
                break;
            case Constants.FRAG_CALENDAR:
                calendarDisplayFragment = calendarFragment;
                active = calendarDisplayFragment;
                updateNavBar(R.id.navigation_calendar);
                break;
            case Constants.FRAG_ECLASS:
                eClassDisplayFragment = eClassFragment;
                active = eClassDisplayFragment;
                updateNavBar(R.id.navigation_eclass);
                break;
            case Constants.FRAG_CLUB_PAGE:
                clubsDisplayFragment = clubPageFragmentList.get(currClub);
                active = clubsDisplayFragment;
                updateNavBar(R.id.navigation_clubs);
                break;
            case Constants.FRAG_CLUBS:
                clubsDisplayFragment = clubsFragment;
                active = clubsDisplayFragment;
                updateNavBar(R.id.navigation_clubs);
                break;
            case Constants.FRAG_PROFILE:
                profileDisplayFragment = profileFragment;
                active = profileDisplayFragment;
                updateNavBar(R.id.navigation_profile);
                break;
        }
    }

    private void compareActive(Fragment active, String fragType) {
        if(!active.getClass().getName().equals(fragType))
            fragmentManager.beginTransaction().hide(active).commit();
    }

    @Override
    public void goToClub(String clubName) {
        FragmentTransaction ft = fragmentManager.beginTransaction().hide(active);
        if (!clubPageFragmentList.keySet().contains(clubName)) {
            Fragment_ClubPage newClubFragment = Fragment_ClubPage.newInstance(clubName);
            clubPageFragmentList.put(clubName, newClubFragment);
            clubsDisplayFragment = newClubFragment;
            ft.add(R.id.fragMainContainer, clubsDisplayFragment,"clubPage: "+clubName);
        } else {
            clubsDisplayFragment = clubPageFragmentList.get(clubName);
        }
        currClub = clubName;
        ft.show(clubsDisplayFragment).addToBackStack("clubs fragment: "+clubName).commit();
        active = clubsDisplayFragment;
    }

    @Override
    public void selectClub(String clubName, Fragment_ClubPage fragment) {
        Log.d("select club",clubPageFragmentList.size()+"");
//        if (!clubPageFragmentList.keySet().contains(clubName)) {
//            clubPageFragmentList.put(clubName, fragment);
//        }
//        currClub = clubName;
    }


    public void updateNavBar(@IdRes int itemId) {
        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
    }

    @Override
    public void onBackPressed() {

    }
}
