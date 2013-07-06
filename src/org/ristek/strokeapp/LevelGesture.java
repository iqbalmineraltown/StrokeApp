package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Intent;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.ristek.strokeapp.support.ClockTimer;
import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.SaveManager;

import java.util.ArrayList;

public class LevelGesture extends Activity {

    private static final double MINIMAL_GESTURE_SCORE = 4.0;
    private static final long GESTURE_TIME = 60000;

    public static boolean onTest = false;
    GestureLibrary gestureLib;
    GestureOverlayView gestureOverlay;
    String gestureName;
    ClockTimer timer;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* remove title bar and notification bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* end remove title bar and notification bar */


        gestureName = getIntent().getStringExtra("gestureName");
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.aksara);
        gestureLib.load();

        setContentView(R.layout.activity_gesture);
        gestureOverlay = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        Bitmap gestureImage = gestureLib.getGestures(gestureName).get(0)
                .toBitmap(400, 240, 20, 100, Color.RED);
        image.setImageBitmap(gestureImage);

        Button submitButton = (Button) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(onClickSubmit);

        timeText = (TextView) findViewById(R.id.timeTextGesture);
        if (SaveManager.getMode() == GameMode.NORMAL) timeText.setVisibility(View.INVISIBLE);
        else {
            timer = new ClockTimer(onTimerUpdate);
            timer.setTimeLeft(GESTURE_TIME);
            timeText.setText(ClockTimer.timeToString(GESTURE_TIME, ClockTimer.MM_SS));
            timer.start();
        }

        Button clearButton = (Button) findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureOverlayView gestureOverlay = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
                gestureOverlay.setGesture(new Gesture());
            }
        });
    }

    private View.OnClickListener onClickSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GestureOverlayView gestureOverlay = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
            Gesture input = gestureOverlay.getGesture();
            Gesture emptyGesture = new Gesture();
            ArrayList<Prediction> pres = null;
            if (input != null && !input.equals(emptyGesture) && input.getStrokesCount() > 0) {
                pres = gestureLib.recognize(input);
            }
            boolean result = false;
            double score = 0;
            if (pres != null) for (Prediction pre : pres) {
                if (pre.name.equals(gestureName)) {
                    if (pre.score >= MINIMAL_GESTURE_SCORE) {
                        result = true;
                        score = pre.score;
                    }
                }
            }
            if (onTest) result = true;

            Intent returnIntent = new Intent();
            returnIntent.putExtra("gestureResult", result);
            returnIntent.putExtra("gestureScore", score);
            if (SaveManager.getMode() == GameMode.TIME_TRIAL)
                returnIntent.putExtra("time", GESTURE_TIME - timer.getTimeLeft());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };

    private ClockTimer.TimerListener onTimerUpdate = new ClockTimer.TimerListener() {
        @Override
        public void onTimerUpdate(long timeLeft) {
            timeText.setText(ClockTimer.timeToString(timeLeft, ClockTimer.MM_SS));
            if (timeLeft == 0) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("gestureResult", false);
                returnIntent.putExtra("gestureScore", 0.0);
                returnIntent.putExtra("time", GESTURE_TIME);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveManager.getMode() == GameMode.TIME_TRIAL) {
            timer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SaveManager.getMode() == GameMode.TIME_TRIAL) {
            timer.stop();
        }
    }
}
