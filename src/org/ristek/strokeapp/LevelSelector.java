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
	private static final String[] gestureName = { "Nga", "Ga", "Pa", "Da",
			"Ca", "Ba", "Tha", "Ya", "Ta", "Ja", "Wa", "Ka", "Dha", "Ha", "Ma",
			"Sa", "Na", "Nya", "Ra", "La" };

	private static final int QUESTION_ACTIVITY_REQUEST = 1;
	private static final int GESTURE_ACTIVITY_REQUEST = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Rectangle mMap;
	protected Rectangle[] arrRect;
	protected Text mStartText;
	protected Scene mMainScene;
	protected Text mText;
	protected Font mFont;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mBackTextureRegion;

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
		if (requestCode == GESTURE_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			Toast.makeText(
					this,
					"Hasil:" + (data.getBooleanExtra("gestureResult", false)?"Benar":"Salah"),
					Toast.LENGTH_SHORT).show();
		}
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
								Collections.shuffle(Arrays.asList(gestureName));
								Intent intent = new Intent(LevelSelector.this,
										GestureActivity.class);
								intent.putExtra("gestureName",
										gestureName[0]);
								startActivityForResult(intent,
										GESTURE_ACTIVITY_REQUEST);
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
					if(mStartText.contains(pX, pY)){
						if (selection < 5) {
							// Jika bagian pertanyaan
							Intent intent = new Intent(LevelSelector.this,
									Questions.class);
							startActivity(intent);
						} else {
							// Jika bagian gesture
							Collections.shuffle(Arrays.asList(gestureName));
							Intent intent = new Intent(LevelSelector.this,
									GestureActivity.class);
							intent.putExtra("gestureName",
									gestureName[0]);
							startActivityForResult(intent,
									GESTURE_ACTIVITY_REQUEST);
						}
					}
				}
				return super.onSceneTouchEvent(pSceneTouchEvent);
			}
		};
		mMainScene.setBackground(new SpriteBackground(new Sprite(0, 0,
				mBackTextureRegion, getVertexBufferObjectManager())));

		//mMap = new Rectangle(pX / 8, pY / 8, 3 * pX / 4, 3 * pY / 4,
//				getVertexBufferObjectManager());
		//mMap.setColor(100, 100, 100);

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

		mText = new Text(pX / 8, pY / 8, this.mFont, "Pilih Level",
				new TextOptions(HorizontalAlign.LEFT),
				getVertexBufferObjectManager());
		mStartText = new Text(400, 300, this.mFont, "Mulai", getVertexBufferObjectManager());
		//mMainScene.attachChild(mMap);
		mMainScene.attachChild(mText);
		mMainScene.attachChild(mStartText);
		for (int i = 0; i < arrRect.length; i++) {
			arrRect[i].setColor(0, 0, 0);
			mMainScene.attachChild(arrRect[i]);
			mMainScene.registerTouchArea(arrRect[i]);
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
