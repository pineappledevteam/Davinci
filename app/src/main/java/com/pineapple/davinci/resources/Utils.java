package com.pineapple.davinci.resources; /**
 * Created by Mudit on 5/30/2018.
 */


import android.content.Context;
import android.os.Handler;


public class Utils {

    // Delay mechanism

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}
