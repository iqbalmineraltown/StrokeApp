package org.ristek.strokeapp;

import java.util.Arrays;
import java.util.Collections;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.widget.Toast;

public class LevelSelector extends SimpleBaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	private static final String[] gestureName = { "Ga", "Pa", "Da", "Ca", "Ya",
			"Ta", "Ja", "Wa", "Ka", "Dha", "Ha", "Ma", "Sa", "Na", "Ra", "La" };
	int gestureInt = 0;
	private static final int QUESTION_ACTIVITY_REQUEST = 1;
	private static final int GESTURE_ACTIVITY_REQUEST = 2;

	private static final int POST_SUM = 9;

	private static final int STATE_LEVEL_SELECT = 0;
	private static final int STATE_QUESTION_LEVEL = 1;
	private static final int STATE_GESTURE_LEVEL = 2;
	private static final int STATE_FINAL_LEVEL = 3;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Rectangle mMap;
	protected Sprite[] arrPost;
	protected Sprite[] arrPostPressed;
	protected Scene mMainScene;
	protected Font mFont;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mBackTextureRegion;
	private TextureRegion mPostTextureRegion;
	private TextureRegion mPostPressedTextureRegion;
	private int gameState;
	private int gameLevel;

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
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"images/map.png");
		this.mPostTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"images/i-pos.png");
		this.mPostPressedTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"images/i-pass.png");
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.ITALIC), 32);

		this.mFont.load();

		try {
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (gameState == STATE_GESTURE_LEVEL) {
			if (requestCode == GESTURE_ACTIVITY_REQUEST
					&& resultCode == RESULT_OK
					&& data.getBooleanExtra("gestureResult", false)) {
				Toast.makeText(this, "Hasil : Benar", Toast.LENGTH_SHORT)
						.show();
				if (gameLevel < 3) {
					gameLevel++;
					createGestureLevel(gameLevel);
				} else {
					Toast.makeText(
							this,
							"Hasil : "
									+ (data.getBooleanExtra("gestureResult",
											false) ? "Benar" : "Salah"),
							Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this, "Hasil : Salah", Toast.LENGTH_SHORT)
				.show();
				gameState = STATE_LEVEL_SELECT;
				gameLevel = 0;
			}
		}
	}

	protected void createGestureLevel(int gestureNum) {
		Intent intent = new Intent(LevelSelector.this, GestureActivity.class);
		intent.putExtra("gestureName", gestureName[gestureNum]);
		startActivityForResult(intent, GESTURE_ACTIVITY_REQUEST);
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		gameState = 0;
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
					for (int i = 0; i < arrPost.length; i++) {
						if (arrPost[i].contains(pX, pY)) {
							selection = i;
						}
					}
				} else if (pSceneTouchEvent.isActionUp()) {
					for (int i = 0; i < arrPost.length; i++) {
						if (arrPost[i].contains(pX, pY) && selection == i) {
							selection = -1;
							if (i < 5) {
								// Jika bagian pertanyaan
								Intent intent = new Intent(LevelSelector.this,
										Questions.class);
								intent.putExtra("QuestionIndex", i);
								startActivity(intent);
							} else {
								// Jika bagian gesture
								Collections.shuffle(Arrays.asList(gestureName));
								gameState = STATE_GESTURE_LEVEL;
								gameLevel = 1;
								createGestureLevel(gameLevel);
							}

						}
					}
				}
				for (int i = 0; i < arrPost.length; i++) {
					arrPost[i].setVisible(i != selection);
					arrPostPressed[i].setVisible(i == selection);
				}
				return super.onSceneTouchEvent(pSceneTouchEvent);
			}
		};
		mMainScene.setBackground(new SpriteBackground(new Sprite(0, 0,
				mBackTextureRegion, getVertexBufferObjectManager())));

		// mMap = new Rectangle(pX / 8, pY / 8, 3 * pX / 4, 3 * pY / 4,
		// getVertexBufferObjectManager());
		// mMap.setColor(100, 100, 100);

		arrPost = new Sprite[POST_SUM];
		for (int i = 0; i < arrPost.length; i++) {
			arrPost[i] = new Sprite(100 + (i % 3) * 100, 100 + (i / 3) * 100,
					mPostTextureRegion, getVertexBufferObjectManager());
			mMainScene.attachChild(arrPost[i]);
			mMainScene.registerTouchArea(arrPost[i]);
		}
		arrPostPressed = new Sprite[POST_SUM];
		for (int i = 0; i < arrPostPressed.length; i++) {
			arrPostPressed[i] = new Sprite(100 + (i % 3) * 100,
					100 + (i / 3) * 100, mPostPressedTextureRegion,
					getVertexBufferObjectManager());
			arrPostPressed[i].setVisible(false);
			mMainScene.attachChild(arrPostPressed[i]);
			mMainScene.registerTouchArea(arrPostPressed[i]);
		}
		return mMainScene;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
