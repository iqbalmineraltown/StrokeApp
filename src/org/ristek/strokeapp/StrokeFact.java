package org.ristek.strokeapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StrokeFact extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fact_page);
		MyPagerAdapter adapter = new MyPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

class MyPagerAdapter extends PagerAdapter {
    public int getCount() {
        return 4;
    }
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        switch (position) {
        case 0:
            resId = R.layout.story1;
            break;
        case 1:
            resId = R.layout.story2;
            break;
        case 2:
            resId = R.layout.story3;
            break;
        case 3:
            resId = R.layout.story4;
            break;
        }
        View view = inflater.inflate(resId, null);
        
        ((ViewPager) collection).addView(view, 0);
        return view;
    }
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
}
