package com.pineapple.davinci.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.resources.Singleton;
import com.pineapple.davinci.studentutils.Student;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CALL = 1;
    private static Student student;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //TODO: no UI init here!!
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        student = Singleton.getInstance().getCurrStudent();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createEvents(view, savedInstanceState);
        loadData(view, savedInstanceState);
    }
    private void createEvents(View view, Bundle savedInstanceState){//creates all the action listeners for the buttons

        //SETTINGS BUTTON
        ImageButton settingsBtn = view.findViewById(R.id.imgBtn_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {//add listener for setting button
            @Override
            public void onClick(View v) {
                //TODO SWITCH FRAGMENTS
                /*
                Intent startIntent = new Intent(getApplicationContext(), Activity_Settings.class);
                startIntent.putExtra("com.pineapple.apps.yes"," Hello World!!");
                startActivity(startIntent);
                */
            }
        });
        //EDIT PROFILE BUTTON
        ImageButton editProfileBtn = view.findViewById(R.id.imgBtn_editProfile);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {//add listener for setting button
            @Override
            public void onClick(View v) {
                //TODO switch fragments
                /*
                Intent startIntent = new Intent(getApplicationContext(), Activity_EditProfile.class);
                startIntent.putExtra("com.pineapple.apps.yes"," Hello World!!");
                startActivity(startIntent);
                */
            }
        });
        //EMAIL BUTTON
        Button btnEmail = view.findViewById(R.id.btn_email);
        btnEmail.setOnClickListener(new View.OnClickListener() {//add listener for setting button
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        Button btnCall = view.findViewById(R.id.btn_phoneNumber);
        btnCall.setOnClickListener(new View.OnClickListener() {//add listener for setting button
            @Override
            public void onClick(View v) {

                makePhoneCall();

            }
        });
    }
    private void loadData(View view, Bundle savedInstanceState){//fills all text and pictures with the correct data from the Student

        //set profile name
        ((TextView)view.findViewById(R.id.txt_profileName)).setText(student.getFirstName() + " " + student.getLastName());
        //set grade
        ((TextView)view.findViewById(R.id.txt_numGrade)).setText(Integer.toString(student.getGrade()));
        //set description
        ((TextView)view.findViewById(R.id.txt_desc)).setText(student.getDescription());
    }
    private void makePhoneCall() {
        final String number = "6783337936";
        if (number.trim().length() > 0) {
            //TODO: fix this perms stuff
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                callNumber(number);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        } //first if phone number is not empty
        else {
            Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    private void callNumber(String number){//calls the number passed in
        String dial = "tel:" + number;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"here@there.ie"});
        try {
            startActivity(Intent.createChooser(intent, "How to send mail?"));
        } catch (android.content.ActivityNotFoundException ex) {
            //do something else
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.updateNavBar(Constants.FRAG_PROFILE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void updateNavBar(String fragType);
    }
}
