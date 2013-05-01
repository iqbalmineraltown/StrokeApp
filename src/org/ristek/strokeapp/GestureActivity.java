package org.ristek.strokeapp;

import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class GestureActivity extends Activity {

	GestureLibrary gestureLib;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		gestureLib.load();
		
		setContentView(R.layout.activity_gesture);
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		Bitmap test = gestureLib.getGestures("Ha_bugis").get(0)
				.toBitmap(800, 480, 20,40, Color.RED);
		image.setImageBitmap(test);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture, menu);
		return true;
	}

}
