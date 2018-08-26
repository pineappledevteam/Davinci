package com.pineapple.davinci.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.FirebaseDatabase;
import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.resources.MyCallback;
import com.pineapple.davinci.resources.Singleton;
import com.pineapple.davinci.studentutils.Student;
import com.pineapple.davinci.studentutils.Student_Delegate;

import java.util.HashMap;

//import org.apache.http.annotation.ThreadSafe;

public class Activity_Login extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount account;
    private FirebaseAuth mAuth;
    SignInButton signInButton;
    FirebaseUser fireUser;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        mContext = this;

//        if(Singleton.getInstance().isSignedInSuccess()) {
//            account = Singleton.getInstance().getGsiAccount();
//            updateUI(account);
//        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.server_client_id)
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Singleton.getInstance().setGsiClient(mGoogleSignInClient);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("conn fail", connectionResult.getErrorMessage());
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Singleton.getInstance().setGapiClient(mGoogleApiClient);

        // Set the dimensions of the sign-in button.
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setVisibility(View.INVISIBLE);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
//        account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//        mGoogleSignInClient.silentSignIn()
//                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
//                    @Override
//                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//                        handleSignInResult(task);
//                    }
//                });

        updateUI(fireUser);
    }

    @Override
    protected void onStart () {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fireUser = mAuth.getCurrentUser();
        updateUI(fireUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.ActivityRequestCode_RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null) {                   //valid account already signed in
            if(!Singleton.getInstance().isSignedInSuccess()) {
                Log.d("singleton","save fireUser");
                Singleton.getInstance().setFireUser(firebaseUser);
                Singleton.getInstance().setDatabaseRef(FirebaseDatabase.getInstance().getReference());
            }
            //check our database for existing profile for user
            String userID = firebaseUser.getUid();
            Log.d("fireUser",userID);

            Student_Delegate.getStudentFromFirebase(userID, new MyCallback<Student>() {
                @Override
                public void accept(Student student) {
                    if(student != null) {
                        if(!Singleton.getInstance().isSignedInSuccess()) {
                            Log.d("singleton","save student");
                            Singleton.getInstance().setCurrStudent(student);
                        }
                        Intent toDashBoardIntent = new Intent(getApplicationContext(),Activity_Dashboard.class);
                        Singleton.getInstance().setSignedInSuccess(true);
                        startActivity(toDashBoardIntent);
                    } else {
                        Log.w("login","logged - need profile");
                        Intent createProfileIntent = new Intent(getApplicationContext(),Activity_CreateProfile.class);
                        startActivityForResult(createProfileIntent,Constants.ActivityRequestCode_Create_Profile);
                    }
                }
            });



        } else {//display sign/log in options
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.ActivityRequestCode_RC_SIGN_IN) {
                    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                if(!Singleton.getInstance().isSignedInSuccess()) {
                    Singleton.getInstance().setGsiAccount(account);
                }
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("FAIL", "Google sign in failed", e);
                // ...
            }
            //handleSignInResult(task);
        } else if(requestCode == Constants.ActivityRequestCode_Create_Profile) {
                    // Result from create profile intent
            HashMap<String,Object> studentData = (HashMap<String, Object>) data.getExtras().get(Constants.student);
            handleCreateProfileResult(studentData);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final String TAG = "SIGNIN";
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            fireUser = mAuth.getCurrentUser();
                            updateUI(fireUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    /*
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(getResources().getString(R.string.loginFailTAG), "signInResult:failed code=" + e.getStatusCode());
            for(StackTraceElement a : e.getStackTrace()) {
                Log.w("log fail",a.toString());
            }
            e.printStackTrace();
            updateUI(null);
        }
    }
    */

    private void handleCreateProfileResult(HashMap<String,Object> profileData) {
        Student profile = Student_Delegate.readFromHashMap(profileData);
        Singleton.getInstance().setCurrStudent(profile);
        Student_Delegate.writeNewStudent();

        Intent toDashBoardIntent = new Intent(this,Activity_Dashboard.class);
        Singleton.getInstance().setSignedInSuccess(true);
        startActivity(toDashBoardIntent);
    }


}
