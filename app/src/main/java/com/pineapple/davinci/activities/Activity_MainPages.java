package com.pineapple.davinci.activities;

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

        Log.d("check active", "" + (active == dashboardFragment));

        fragmentManager.beginTransaction().add(R.id.fragMainContainer, calendarDisplayFragment, Constants.FRAG_CALENDAR).hide(calendarDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, eClassDisplayFragment, Constants.FRAG_ECLASS).hide(eClassDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, clubsDisplayFragment, Constants.FRAG_CLUBS).hide(clubsDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, profileDisplayFragment, Constants.FRAG_PROFILE).hide(profileDisplayFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragMainContainer, dashboardDisplayFragment, Constants.FRAG_DASHBOARD).show(dashboardDisplayFragment).commit();



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
                        fragmentManager.beginTransaction().hide(active).show(dashboardDisplayFragment).addToBackStack(Constants.FRAG_DASHBOARD).commit();
                        active = dashboardDisplayFragment;
                        return true;
                    case R.id.navigation_calendar:
                        fragmentManager.beginTransaction().hide(active).show(calendarDisplayFragment).addToBackStack(Constants.FRAG_CALENDAR).commit();
                        active = calendarDisplayFragment;
                        return true;
                    case R.id.navigation_eclass:
                        fragmentManager.beginTransaction().hide(active).show(eClassDisplayFragment).addToBackStack(Constants.FRAG_ECLASS).commit();
                        active = eClassDisplayFragment;
                        return true;
                    case R.id.navigation_clubs:
                        fragmentManager.beginTransaction().hide(active).show(clubsDisplayFragment).addToBackStack(Constants.FRAG_CLUBS).commit();
                        active = clubsDisplayFragment;
                        return true;
                    case R.id.navigation_profile:
                        fragmentManager.beginTransaction().hide(active).show(profileDisplayFragment).addToBackStack(Constants.FRAG_PROFILE).commit();
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
        StringBuilder toPrint = new StringBuilder("");
        for(int i = 0; i < numActivities; i++) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            toPrint.append(backStackEntry.getName()).append(", ");
        }
        if(toPrint.toString().contains(","))
            toPrint = new StringBuilder(toPrint.substring(0, toPrint.lastIndexOf(",")));
        else
            toPrint = new StringBuilder("no activities");
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
        Log.d("active fragment compare","THIS "+active.getClass().getName()+" SHOULD BE "+fragType);
        if(!active.getClass().getName().equals(fragType))
            fragmentManager.beginTransaction().hide(active).commit();
    }

    @Override
    public void goToClub(String clubName) {
        FragmentTransaction ft = fragmentManager.beginTransaction().hide(active);
        if (!clubPageFragmentList.keySet().contains(clubName)) {
            Fragment_ClubPage newClubFragment = Fragment_ClubPage.newInstance(clubName);
            clubPageFragmentList.put(clubName, newClubFragment);
            clubsDisplayFragment = clubPageFragmentList.get(clubName);
            ft.add(R.id.fragMainContainer, clubsDisplayFragment,Constants.FRAG_CLUB_PAGE+":"+clubName);
        } else {
            clubsDisplayFragment = clubPageFragmentList.get(clubName);
        }
        currClub = clubName;
        ft.show(clubsDisplayFragment).addToBackStack(Constants.FRAG_CLUB_PAGE+":"+clubName).commit();
        active = clubsDisplayFragment;
    }

    @Override
    public void selectClub(String clubName, Fragment_ClubPage fragment) {
        Log.d("list size",clubPageFragmentList.size()+"");
        Log.d("club opened",clubName);
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
        super.onBackPressed();
    }
}
