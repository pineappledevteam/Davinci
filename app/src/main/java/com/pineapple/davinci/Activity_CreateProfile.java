package com.pineapple.davinci;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.pineapple.davinci.clubutils.Clubs_Delegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Activity_CreateProfile extends AppCompatActivity {

    private float mPixelDensity;

    private int MIN_AGE;
    private int MAX_AGE;

    private LinearLayout linearLayout;

    private FirebaseUser mFireUser;

    private String accountID;
    private String name;

    EditText editTextGrade;
    EditText editTextAge;
    EditText editTextStudID;
    EditText editTextDescription;
    AutoCompleteTextView editTextClubs;
    Button addClub;
    EditText editTextRelStat;

    FlexboxLayout flexBox;
    ViewSwitcher switchClubTextButton;

    ArrayList<String> joinedClubs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mPixelDensity = this.getResources().getDisplayMetrics().density;

        MIN_AGE = getResources().getInteger(R.integer.minAge);
        MAX_AGE = getResources().getInteger(R.integer.maxAge);


        mFireUser = Singleton.getInstance().getFireUser();
        accountID = mFireUser.getUid();
        name = mFireUser.getDisplayName();
        name = name.substring(0,name.indexOf(" "));

        TextView welcomeText = (TextView) findViewById(R.id.welcome);
        String newWelcomeStr = welcomeText.getText()+" "+name;
        welcomeText.setText(newWelcomeStr);
        
        editTextGrade = (EditText) findViewById(R.id.gradeEdit);
        editTextGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2) {
                    int val = Integer.parseInt(s.toString());
                    if(val < 9) {
                        s.clear();
                        s.append("9");
                    } else if(val > 12) {
                        s.clear();
                        s.append("12");
                    }
                }
            }
        });

        editTextAge = (EditText) findViewById(R.id.ageEdit);
        editTextAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2) {
                    int val = Integer.parseInt(s.toString());
                    if(val < MIN_AGE) {
                        s.clear();
                        s.append(""+MIN_AGE);
                    } else if(val > MAX_AGE) {
                        s.clear();
                        s.append(""+MAX_AGE);
                    }
                }
            }
        });

        editTextStudID = (EditText) findViewById(R.id.studIDEdit);

        editTextDescription = (EditText) findViewById(R.id.descEdit);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        flexBox = (FlexboxLayout) findViewById(R.id.clubsFlex);


        switchClubTextButton = (ViewSwitcher) findViewById(R.id.addButtonAndText);
        addClub = (Button) findViewById(R.id.addClubButton);
        editTextClubs = (AutoCompleteTextView) findViewById(R.id.clubName);

        editTextClubs.setThreshold(1);
        Clubs_Delegate.getClubList(new MyCallback<ArrayList<String>>() {
            @Override
            public void accept(final ArrayList<String> clubList) {
                Log.d("detected clubs",clubList.toString());
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        Activity_CreateProfile.this, android.R.layout.simple_list_item_1, clubList);
                editTextClubs.setAdapter(adapter);
                String longest = editTextClubs.getHint().toString();
                Rect bounds = new Rect();
                Paint p = new Paint();
                for(String s : clubList) {
                    p.getTextBounds(s,0,s.length(),bounds);
                    int sLen = bounds.width();

                    p.getTextBounds(longest,0,longest.length(),bounds);
                    int longLen = bounds.width();

                    if(sLen > longLen) {
                        longest = s;
                    }
                }
                float width = flexBox.getWidth()-editTextClubs.getTranslationX();//p.measureText(longest)*4;
                Log.d("longest club",editTextClubs.getWidth() + " " + longest +" " + width);
                editTextClubs.setWidth((int)width);
                editTextClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final String name = ((TextView)view).getText().toString();
                        Log.d("club selected",name);
                        if(clubList.contains(name)) {
                            Log.d("club selected","joined success");
                            joinedClubs.add(name);
                            final TextView nextClub = new TextView(Activity_CreateProfile.this);
                            nextClub.setText(name);
                            nextClub.setTextColor(Color.WHITE);
                            nextClub.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            nextClub.setPadding((int)(13*mPixelDensity),(int)(8*mPixelDensity),(int)(13*mPixelDensity),(int)(8*mPixelDensity));
                            nextClub.setElevation((int)(11*mPixelDensity));
                            nextClub.setShadowLayer((int)(11*mPixelDensity),(int)(1*mPixelDensity),(int)(1*mPixelDensity), Color.parseColor("#15000000"));
                            Clubs_Delegate.getClubColors(name, new MyCallback<int[]>() {
                                @Override
                                public void accept(int[] colors) {
                                    GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
                                    gradient.setCornerRadius((int)(15*mPixelDensity));
                                    nextClub.setBackground(gradient);
                                    int numChildren = flexBox.getChildCount();
                                    if(numChildren == 1) {
                                        flexBox.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
                                        editTextClubs.setHint("");
                                    }
                                    flexBox.addView(nextClub, numChildren-1);
                                    clubList.remove(name);
                                    adapter.remove(name);
                                    //editTextClubs.setAdapter(adapter);
                                    editTextClubs.setText("");
                                    editTextClubs.clearFocus();
                                    flexBox.requestFocus();
                                    switchClubTextButton.showNext();
                                    editTextClubs.setWidth(addClub.getWidth());
                                    //switchClubTextButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                }
                            });
                        } else {
                            editTextClubs.clearListSelection();
                        }
                    }
                });

                addClub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchClubTextButton.showNext();
                        String longest = clubList.get(0);
                        Rect bounds = new Rect();
                        Paint p = new Paint();
                        for(String s : clubList) {
                            p.getTextBounds(s,0,s.length(),bounds);
                            int sLen = bounds.width();

                            p.getTextBounds(longest,0,longest.length(),bounds);
                            int longLen = bounds.width();

                            if(sLen > longLen) {
                                longest = s;
                            }
                        }
                        float width = flexBox.getWidth()-editTextClubs.getTranslationX();//p.measureText(longest)*4;
                        Log.d("longest club",editTextClubs.getWidth() + " " + longest +" " + width);
                        editTextClubs.setWidth((int)width);
                        //switchClubTextButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        editTextClubs.requestFocus();
                    }
                });
            }
        });


        editTextRelStat = (EditText) findViewById(R.id.relStatEdit);


        Button ButtonLetsGo = (Button) findViewById(R.id.letsGoButton);
        ButtonLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allInputsOkay()) {
                    Log.d("lets go","good");
                    buildStudentAndReturn();
                } else {
                    Log.d("lets go","fail");
                    displayErrors();
                }
            }
        });

    }

    private boolean allInputsOkay() {
        if(editTextGrade.getText().toString().isEmpty() || editTextAge.getText().toString().isEmpty()) return false;
        int grade = Integer.parseInt(editTextGrade.getText().toString());
        int age = Integer.parseInt(editTextAge.getText().toString());
        String studID = editTextStudID.getText().toString();
        String desc = editTextDescription.getText().toString();
        String relStat = editTextRelStat.getText().toString();
        if(grade < 9 || grade > 12) return false;
        if(age < MIN_AGE || age > MAX_AGE) return false;
        if(studID.trim().length() != 9) return false;
        if(desc.length() < 1) return false;
        if(relStat.indexOf(" ") != relStat.lastIndexOf(" ")) return false;
        return true;
    }

    private void displayErrors() {
        int grade;
        if(editTextGrade.getText().toString().isEmpty()) grade = 0;
        else grade = Integer.parseInt(editTextGrade.getText().toString());
        int age;
        if(editTextAge.getText().toString().isEmpty()) age = 0;
        else age = Integer.parseInt(editTextAge.getText().toString());
        String studID = editTextStudID.getText().toString();
        String desc = editTextDescription.getText().toString();
        String relStat = editTextRelStat.getText().toString();
        if(grade < 9 || grade > 12) {
            editTextGrade.setError("Must be from 9 to 12!");
        }
        if(age < MIN_AGE || age > MAX_AGE) {
            editTextAge.setError("Must range from " + MIN_AGE + " to " + MAX_AGE + "!");
        }
        if(studID.trim().length() != 9) {
            editTextStudID.setError("Must be 9 digits!");
        }
        if(desc.length() < 1) {
            editTextDescription.setError("must provide some description!");
        }
        if(relStat.indexOf(" ") != relStat.lastIndexOf(" ")) {
            editTextRelStat.setError("Must be 1 word!");
        }
    }

    private void buildStudentAndReturn() {
        HashMap<String,Object> data = new HashMap<>();
        Log.d("let's go!","results almost complete");

        data.put(Constants.DB_Cols_UserID,mFireUser.getUid());
        String displayName = Singleton.getInstance().getFireUser().getDisplayName();
        data.put(Constants.DB_Cols_FirstName,displayName.substring(0,displayName.indexOf(" ")));
        data.put(Constants.DB_Cols_LastName,displayName.substring(displayName.indexOf(" ")+1));
        data.put(Constants.DB_Cols_Age,Integer.parseInt(editTextAge.getText().toString()));
        data.put(Constants.DB_Cols_Grade,Integer.parseInt(editTextGrade.getText().toString()));
        data.put(Constants.DB_Cols_StudentID,editTextStudID.getText().toString());
        data.put(Constants.DB_Cols_Description,editTextDescription.getText().toString());
        String clubListString = "";
        for(String club: joinedClubs) {
            clubListString += club + ",";
        }
        if(!joinedClubs.isEmpty()) clubListString = clubListString.substring(0,clubListString.lastIndexOf(","));
        data.put(Constants.DB_Cols_Clubs,clubListString);
        data.put(Constants.DB_Cols_Relationship,editTextRelStat.getText().toString());

        Intent result = new Intent();
        result.putExtra(Constants.student, data);
        setResult(0,result);
        Log.d("let's go!","results complete, need to return");
        finish();
    }

}
