package org.ristek.strokeapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class OpeningActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opening_story);
		
	   TextView tv = (TextView) findViewById(R.id.content_opening);
	   //ScrollView sv = (ScrollView) findViewById(R.id.scroll_opening);
	   
	   Scroller s = new Scroller(this,  new LinearInterpolator());
	   tv.setScroller(s);
	   s.startScroll(0, 0, 0,600, 30000);
	  
	   Button button= (Button) findViewById(R.id.buttonSkip);
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
