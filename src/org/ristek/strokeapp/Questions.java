package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Questions extends Activity {

	private Button rb;
	private RadioGroup rg;
	private RadioButton rbb;
	private ViewPager myPager;
	private MyQPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_page);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		adapter = new MyQPagerAdapter();
		myPager = (ViewPager) findViewById(R.id.soalsoal);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);
	    
		addListenerOnButton();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void addListenerOnButton() {
		
		rg = (RadioGroup) findViewById(R.id.RadioGroup1);
		// TODO: Masih belum nemu (null)
		rb = (Button) findViewById(R.id.button1);
		
		
		OnClickListener clicked = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		};
		
		//rb.setOnClickListener(clicked);
		
	}
	
}

class MyQPagerAdapter extends PagerAdapter{
	
	public int getCount() {
        return 1;
    }
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        switch (position) {
        case 0:
            resId = R.layout.question1;
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
