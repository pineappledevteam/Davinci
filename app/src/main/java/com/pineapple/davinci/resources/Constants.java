package com.pineapple.davinci.resources;

import android.graphics.Color;
import android.support.annotation.Dimension;

/**
 * Created by Mudit on 6/11/2018.
 */

public class Constants {

    public static final String server_client_id = "415892562291-mrb5obdmrcm1jruvi9apveef5onuo2v6.apps.googleusercontent.com";

    public static final int ActivityRequestCode_RC_SIGN_IN = 10;
    public static final int ActivityRequestCode_Create_Profile = 100;

    public static final String DB_Strings_URL = "192.168.10.129:3306";
    public static final String DB_Strings_name = "db_trial1";
    public static final String DB_Strings_user = "gsmstAppUser";
    public static final String DB_Strings_pass = "pineappleJJAM_334";

    public static final String DB_Tables_Students = "Students";
    public static final String DB_Tables_Clubs = "Clubs";

    public static final String DB_Cols_UserID = "user_ID";
    public static final String DB_Cols_FirstName = "first_name";
    public static final String DB_Cols_LastName = "last_name";
    public static final String DB_Cols_Age = "age";
    public static final String DB_Cols_Grade = "grade";
    public static final String DB_Cols_StudentID = "student_id";
    public static final String DB_Cols_Description = "description";
    public static final String DB_Cols_Clubs = "club_list";
    public static final String DB_Cols_Relationship = "relationship_status";

    public static final int[] defaultClubGradient = {Color.parseColor("#006DFF"),Color.parseColor("#62A5FF")};

    public static final String FRAG_DASHBOARD = "dashboard";
    public static final String FRAG_CALENDAR = "calendar";
    public static final String FRAG_ECLASS = "eclass";
    public static final String FRAG_CLUBS = "clubs";
    public static final String FRAG_CLUB_PAGE = "club page";
    public static final String FRAG_PROFILE = "profile";

    public static String student = "student";
}
