package com.pineapple.davinci.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import com.pineapple.davinci.R;

public class Activity_StartLogo extends AppCompatActivity {

    View gradientRect;
    TextView gsmstTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_logo);

        Display screen = getWindowManager().getDefaultDisplay();


        gradientRect = (View) findViewById(R.id.gradientRectangle);

        gsmstTextView = (TextView) findViewById(R.id.gsmstTextView);
        gsmstTextView.setVisibility(View.INVISIBLE);


        final Animation alphaAnim_text = new AlphaAnimation(0.00f, 1.00f);
        alphaAnim_text.setDuration(600);
        alphaAnim_text.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                getStarted();
            }
            public void onAnimationRepeat(Animation animation) {}
        });

        final Animation transAnim_rect = new TranslateAnimation(0,0,screen.getHeight(),0);
        transAnim_rect.setDuration(300);
        transAnim_rect.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                gsmstTextView.setVisibility(View.VISIBLE);
                gsmstTextView.startAnimation(alphaAnim_text);
            }
            public void onAnimationRepeat(Animation animation) {}
        });

        gradientRect.startAnimation(transAnim_rect);

    }

    private void getStarted() {
        Intent intent = new Intent(this, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


}
