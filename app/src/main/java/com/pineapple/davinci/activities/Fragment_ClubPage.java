package com.pineapple.davinci.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pineapple.davinci.R;
import com.pineapple.davinci.clubutils.Club;
import com.pineapple.davinci.clubutils.Clubs_Delegate;
import com.pineapple.davinci.resources.Constants;
import com.pineapple.davinci.resources.MyCallback;
import com.pineapple.davinci.resources.MyException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_ClubPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ClubPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ClubPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLUB_NAME = "clubName";

    // TODO: Rename and change types of parameters
    private String clubName;

    private OnFragmentInteractionListener mListener;

    private Club mClub;

    public Fragment_ClubPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clubName club to make fragment for.
     * @return A new instance of fragment Fragment_ClubPage.
     */
    public static Fragment_ClubPage newInstance(String clubName) {
        Fragment_ClubPage fragment = new Fragment_ClubPage();
        Bundle args = new Bundle();
        args.putString(ARG_CLUB_NAME, clubName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clubName = getArguments().getString(ARG_CLUB_NAME);
        } else {
            throw new MyException("invalid club fragment created -- need club");
        }
        Clubs_Delegate.getClub(clubName, new MyCallback<Club>() {
            @Override
            public void accept(Club club) {
                mClub = club;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_club_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: THIS IS ONCREATE FOR FRAGMENTS
        TextView textView = view.findViewById(R.id.oofd);
        textView.setText(clubName);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        mListener.updateNavBar(Constants.FRAG_CLUB_PAGE);
        mListener.selectClub(clubName);
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
        void selectClub(String clubName);
    }
}
