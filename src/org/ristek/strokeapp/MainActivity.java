package org.ristek.strokeapp;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class MainActivity extends SimpleBaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_START = 0;
	protected static final int MENU_TIME_TRIAL = 1;
	protected static final int MENU_OPTIONS = 2;
	protected static final int MENU_STROKE_STORY = 3;
	protected static final int MENU_HIGH_SCORE = 4;
	protected static final int MENU_SUM = 5;
	protected static final String[] MENU_FILE = { "b-start.png", "b-time.png",
		"b-opsi.png", "b-cerita.png","b-hiscore.png"  };

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Scene mMenuScene;

	private Font mFont;

	//private Music bgMusic;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mBackTextureRegion;
	private ITextureRegion mOptionTextureRegion;
	private ITiledTextureRegion[] mButtonTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final FillResolutionPolicy resolutionPolicy = new FillResolutionPolicy();

		EngineOptions engineOption = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, resolutionPolicy, mCamera);
		engineOption.getAudioOptions().setNeedsMusic(true);

		return engineOption;
	}

	@Override
	protected void onCreateResources() {
		// Load Font
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32,true,Color.WHITE_ARGB_PACKED_INT);
		this.mFont.load();

		// Load Images
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"images/l-bg.png");
		this.mOptionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "images/opsi.png");
		this.mButtonTextureRegion = new ITiledTextureRegion[MENU_SUM];
		for (int i = 0; i < mButtonTextureRegion.length; i++) {
			mButtonTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(this.mBitmapTextureAtlas, this,
							"images/" + MENU_FILE[i], 2, 1);
		}

		try {
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		// Load Music
		//try {
//			bgMusic = MusicFactory.createMusicFromAsset(
//					mEngine.getMusicManager(), this, "music/test-music.ogg");
//			bgMusic.setLooping(true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMenuScene = new MainMenuScene();
//		if (getSharedPreferences("StrokeAppOptions", MODE_PRIVATE).getBoolean(
//				"SoundOn", true))
//			bgMusic.play();
		return mMenuScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	class MainMenuScene extends Scene {
		final int[] BUTTON_X = { 230, 447, 150, 310, 550 };
		final int[] BUTTON_Y = { 265, 270, 360, 350, 360 };
		final TiledSprite[] menuButtons;
		final OptionsScene optionScene;

		public MainMenuScene() {
			this.setBackground(new SpriteBackground(new Sprite(0, 0,
					mBackTextureRegion, getVertexBufferObjectManager())));

			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			menuButtons = new TiledSprite[MENU_SUM];

			for (int i = 0; i < menuButtons.length; i++) {
				menuButtons[i] = new TiledSprite(BUTTON_X[i], BUTTON_Y[i],
						mButtonTextureRegion[i], VBOManager);

				this.attachChild(menuButtons[i]);
				this.registerTouchArea(menuButtons[i]);
			}
			optionScene = new OptionsScene();
		}

		@Override
		public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
			if (hasChildScene()) {
				optionScene.onSceneTouchEvent(pSceneTouchEvent);
			} else if (pSceneTouchEvent.isActionDown()) {
				for (int i = 0; i < menuButtons.length; i++) {
					if (menuButtons[i].contains(pSceneTouchEvent.getX(),
							pSceneTouchEvent.getY())) {
						menuButtons[i].setCurrentTileIndex(1);
					}
				}
			} else if (pSceneTouchEvent.isActionUp()) {
				for (int i = 0; i < menuButtons.length; i++) {
					if (menuButtons[i].contains(pSceneTouchEvent.getX(),
							pSceneTouchEvent.getY()) && menuButtons[i].getCurrentTileIndex() == 1) {
						
						if (i == MENU_START) {
							Intent intent = new Intent(MainActivity.this,
									OpeningActivity.class);
							startActivity(intent);
						} else if (i == MENU_TIME_TRIAL) {
							// TODO Time Trial
						} else if (i == MENU_HIGH_SCORE) {
							Intent intent = new Intent(MainActivity.this,
									HighScoreScreen.class);
							startActivity(intent);
						} else if (i == MENU_STROKE_STORY) {
							Intent intent = new Intent(MainActivity.this,
									StrokeFact.class);
							startActivity(intent);
						} else if (i == MENU_OPTIONS) {
							this.setChildScene(optionScene);
						}
					}
					menuButtons[i].setCurrentTileIndex(0);
				}
			}

			return true;
		}

	}

	class OptionsScene extends Scene {
		final Text soundOption;
		final Text backButton;

		public OptionsScene() {
			this.setBackgroundEnabled(false);
			this.attachChild(new Sprite(0, 0,
					mOptionTextureRegion, getVertexBufferObjectManager()));
			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			soundOption = new Text(30, 200, mFont, "Suara : -----",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);
			soundOption.setText(("Suara : " + ((getSharedPreferences(
					"StrokeAppOptions", MODE_PRIVATE).getBoolean("SoundOn",
					true)) ? "Hidup" : "Mati")));

			backButton = new Text(30, 280, mFont, "Kembali",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);

			this.attachChild(soundOption);
			this.attachChild(backButton);
			this.registerTouchArea(soundOption);
			this.registerTouchArea(backButton);
		}

		@Override
		public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
			if (pSceneTouchEvent.isActionDown()) {
				if (soundOption.contains(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY())) {
					boolean isSoundOn = getSharedPreferences(
							"StrokeAppOptions", MODE_PRIVATE).getBoolean(
							"SoundOn", true);
					Editor edit = getSharedPreferences("StrokeAppOptions",
							MODE_PRIVATE).edit();
					edit.putBoolean("SoundOn", !isSoundOn);
					edit.commit();
					if (getSharedPreferences("StrokeAppOptions", MODE_PRIVATE)
							.getBoolean("SoundOn", true)) {
						soundOption.setText("Sound : ON");
//						bgMusic.play();
					} else {
						soundOption.setText("Sound : OFF");
//						bgMusic.pause();
					}

				}
				if (backButton.contains(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY())) {
					this.back();
				}
			}
			return true;
		}
	}

}