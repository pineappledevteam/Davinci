package com.pineapple.davinci.clubutils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pineapple.davinci.R;
import com.pineapple.davinci.resources.Constants;

import java.util.ArrayList;

public class ClubsDashboard_ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Club> clubs;
    private Integer clubsLength;
    private float mPixelDensity;

    public ClubsDashboard_ImageAdapter(Context c, ArrayList<Club> d, float mPixelDensity) {
        mContext = c;
        clubs = d;
        clubsLength = clubs.size();
        this.mPixelDensity = mPixelDensity;
    }

    public int getCount() {
        return clubs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.fragment_clubs_single, null);

            final TextView clubTextView = (TextView)convertView.findViewById(R.id.grid_text);
            final ImageView panelBackground = (ImageView)convertView.findViewById(R.id.grid_image);

            final ViewHolder viewHolder = new ViewHolder(clubTextView, panelBackground);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.clubTextView.setText(clubs.get(position).getNameString());
        int[] colors = Constants.defaultClubGradient.clone();
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        gradient.setCornerRadius((int)(15*mPixelDensity));
        viewHolder.panelBackground.setImageDrawable(gradient);
        return convertView;
    }

    // Your "view holder" that holds references to each subview
    private class ViewHolder {
        private final TextView clubTextView;
        private final ImageView panelBackground;

        public ViewHolder(TextView clubTextView, ImageView panelBackground) {
            this.clubTextView = clubTextView;
            this.panelBackground = panelBackground;
        }
    }

}
