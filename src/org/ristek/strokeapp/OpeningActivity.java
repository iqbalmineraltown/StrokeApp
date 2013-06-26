package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

public class OpeningActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* remove title bar and notification bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_story);

        TextView tv = (TextView) findViewById(R.id.content_opening);
        //ScrollView sv = (ScrollView) findViewById(R.id.scroll_opening);

        Scroller s = new Scroller(this, new LinearInterpolator());
        tv.setScroller(s);
        s.startScroll(0, 0, 0, 600, 30000);

        Button button = (Button) findViewById(R.id.buttonSkip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpeningActivity.this,
                        LevelSelector.class);
                startActivity(intent);

            }
        });
        //sv.scrollTo(0, tv.getHeight());
        // tv.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
