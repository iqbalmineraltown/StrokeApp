package org.ristek.strokeapp;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;




public class LevelSelector extends SimpleBaseGameActivity{

	
	// ===========================================================
	// Constants
	// ===========================================================
		
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
		
		
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	protected Rectangle mMap;
	protected Scene mMainScene;
	protected Text mText;
	protected Font mFont;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		this.mCamera  = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engine  = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new FillResolutionPolicy(), mCamera);
		
		engine.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		return engine;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(),
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.ITALIC), 32);
		
		this.mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		float pX = mCamera.getWidth();
		float pY = mCamera.getHeight();
		mMainScene = new Scene();
		mMainScene.setBackground(new Background(Color.BLUE, CAMERA_HEIGHT, CAMERA_WIDTH));
		

		mMap = new Rectangle(pX/8,pY/8, 3*pX/4, 3*pY/4, getVertexBufferObjectManager());
		mMap.setColor(100,100,100);
		
		
		mText = new Text(pX/8, pY/8, this.mFont, "Select Level",new TextOptions(HorizontalAlign.LEFT), getVertexBufferObjectManager());
		mMainScene.attachChild(mMap);
		mMainScene.attachChild(mText);
		return mMainScene;
	}

	
	
	
}