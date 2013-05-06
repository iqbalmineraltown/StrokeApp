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
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

public class LevelSelector extends SimpleBaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	private static final String[] gestureName = {"Ha","Na","Ca","Ra","Ka"};
	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Rectangle mMap;
	protected Rectangle[] arrRect;
	protected Scene mMainScene;
	protected Text mText;
	protected Font mFont;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engine = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		engine.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		return engine;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.ITALIC), 32);

		this.mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());

		float pX = mCamera.getWidth();
		float pY = mCamera.getHeight();
		mMainScene = new Scene() {
			int selection = -1;

			@Override
			public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
				float pX = pSceneTouchEvent.getX();
				float pY = pSceneTouchEvent.getY();
				if (pSceneTouchEvent.isActionDown()) {
					for (int i = 0; i < arrRect.length; i++) {
						if (arrRect[i].contains(pX, pY) && selection == i) {
							if (i < 5) {
								// Jika bagian pertanyaan
								Intent intent = new Intent(LevelSelector.this,
										Questions.class);
								startActivity(intent);
							} else {
								// Jika bagian gesture
								Intent intent = new Intent(LevelSelector.this,
										GestureActivity.class);
								intent.putExtra("gestureName", gestureName[i-5]);
								startActivity(intent);
							}
						}
						if (arrRect[i].contains(pX, pY)) {
							selection = i;
						}
					}
					for (int i = 0; i < arrRect.length; i++) {
						if (i == selection) {
							arrRect[i].setColor(1, 0, 0);
						} else {
							arrRect[i].setColor(0, 0, 0);
						}
					}
				}
				return super.onSceneTouchEvent(pSceneTouchEvent);
			}
		};
		mMainScene.setBackground(new Background(Color.BLUE, CAMERA_HEIGHT,
				CAMERA_WIDTH));

		mMap = new Rectangle(pX / 8, pY / 8, 3 * pX / 4, 3 * pY / 4,
				getVertexBufferObjectManager());
		mMap.setColor(100, 100, 100);

		arrRect = new Rectangle[9];
		arrRect[0] = new Rectangle(100, 100, 50, 50,
				getVertexBufferObjectManager());
		arrRect[1] = new Rectangle(100, 200, 50, 50,
				getVertexBufferObjectManager());
		arrRect[2] = new Rectangle(100, 300, 50, 50,
				getVertexBufferObjectManager());
		arrRect[3] = new Rectangle(200, 100, 50, 50,
				getVertexBufferObjectManager());
		arrRect[4] = new Rectangle(200, 200, 50, 50,
				getVertexBufferObjectManager());
		arrRect[5] = new Rectangle(200, 300, 50, 50,
				getVertexBufferObjectManager());
		arrRect[6] = new Rectangle(300, 100, 50, 50,
				getVertexBufferObjectManager());
		arrRect[7] = new Rectangle(300, 200, 50, 50,
				getVertexBufferObjectManager());
		arrRect[8] = new Rectangle(300, 300, 50, 50,
				getVertexBufferObjectManager());

		mText = new Text(pX / 8, pY / 8, this.mFont, "Select Level",
				new TextOptions(HorizontalAlign.LEFT),
				getVertexBufferObjectManager());
		mMainScene.attachChild(mMap);
		mMainScene.attachChild(mText);
		for (int i = 0; i < arrRect.length; i++) {
			arrRect[i].setColor(0, 0, 0);
			mMainScene.attachChild(arrRect[i]);
			mMainScene.registerTouchArea(arrRect[i]);
		}
		return mMainScene;
	}

}
