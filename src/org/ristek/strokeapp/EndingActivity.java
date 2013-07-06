package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

public class EndingActivity extends Activity {

    Handler mHandler;
    Runnable goToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* remove title bar and notification bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* end remove title bar and notification bar */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ending_story);
        TextView tv = (TextView) findViewById(R.id.content_ending);

        Scroller s = new Scroller(this, new LinearInterpolator());
        tv.setScroller(s);
        s.startScroll(0, 0, 0, 600, 30000);

        mHandler = new Handler();
        goToMap = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EndingActivity.this,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        mHandler.postDelayed(goToMap, 30000);

        Button button = (Button) findViewById(R.id.buttonSkipEnding);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(goToMap);
                goToMap.run();

            }
        });
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(goToMap);
        goToMap.run();
    }
}
