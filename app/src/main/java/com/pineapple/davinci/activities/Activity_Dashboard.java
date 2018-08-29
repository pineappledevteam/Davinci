package com.pineapple.davinci.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.Singleton;
import com.pineapple.davinci.studentutils.Student;

public class Activity_Dashboard extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    Button signout;

    Student mStudent;

    String TAG = "sign out";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
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



        setupFirebaseListener();

        signout = (Button) findViewById(R.id.button_signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Onclick: attempting to sign out user");
                FirebaseAuth.getInstance().signOut();
                //Auth.GoogleSignInApi.signOut(Singleton.getInstance().getGapiClient());
                Singleton.getInstance().getGsiClient().signOut();
            }
        });

    }

    private void setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up auth state listener");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in " + user.getUid());
                }
                else{
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    Toast.makeText(Activity_Dashboard.this, "Signed out", Toast.LENGTH_SHORT).show();
                    Singleton.getInstance().signOut();
                    Intent startIntent = new Intent(getApplicationContext(), Activity_Login.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(startIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
}
