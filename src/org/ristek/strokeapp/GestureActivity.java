package org.ristek.strokeapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class GestureActivity extends Activity {

	GestureLibrary gestureLib;
	String gestureName;

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
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		Bitmap gestureImage = gestureLib.getGestures(gestureName).get(0)
				.toBitmap(800, 480, 20, 100, Color.RED);
		image.setImageBitmap(gestureImage);

		final GestureOverlayView gestureOverlay = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);

		Button submitButton = (Button) findViewById(R.id.buttonSubmit);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Gesture input = gestureOverlay.getGesture();
				Gesture emptyGesture = new Gesture();
				gestureOverlay.setGesture(emptyGesture);
				ArrayList<Prediction> pres = null;
				if(input != null && !input.equals(emptyGesture) && input.getStrokesCount() > 0){
					pres = gestureLib.recognize(input);
				}
				boolean result = false;
				if (pres != null) for(Prediction pre : pres){
					if(pre.name.equals(gestureName)){
						System.out.println("Gesture Score: " +pre.score);
						if(pre.score >= 3.0)
						  result = true;
					}
				}
				
			}
		});
		
		Button clearButton = (Button) findViewById(R.id.buttonClear);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gestureOverlay.setGesture(new Gesture());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture, menu);
		return true;
	}

}
