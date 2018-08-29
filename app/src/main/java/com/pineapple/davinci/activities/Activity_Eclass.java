package com.pineapple.davinci.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import com.pineapple.davinci.R;

public class Activity_Eclass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eclass);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.navigation_dashboard:
                        Intent startIntent1 = new Intent(getApplicationContext(), Activity_Dashboard.class);
                        startActivity(startIntent1);
                        break;
                    case R.id.navigation_calendar:
                        Intent startIntent2 = new Intent(getApplicationContext(), Activity_Calendar.class);
                        startActivity(startIntent2);
                        break;
                    case R.id.navigation_eclass:
                        Intent startIntent3 = new Intent(getApplicationContext(), Activity_Eclass.class);
                        startActivity(startIntent3);
                        break;
                    case R.id.navigation_clubs:
                        Intent startIntent4 = new Intent(getApplicationContext(), Activity_Eclass.class);
                        startActivity(startIntent4);
                        break;
                    case R.id.navigation_Settings:
                        Intent startIntent5 = new Intent(getApplicationContext(), Activity_Profile.class);
                        startActivity(startIntent5);
                        break;
                }
                return true;
            }
        });
    }
}
