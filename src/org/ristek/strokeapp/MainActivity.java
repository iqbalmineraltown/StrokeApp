package org.ristek.strokeapp;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

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
	protected static final int MENU_HIGH_SCORE = 2;
	protected static final int MENU_STROKE_FACT = 3;
	protected static final int MENU_OPTIONS = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Scene mMenuScene;

	private Font mFont;

	private Music bgMusic;

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
		final RatioResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(
				CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions engineOption = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, resolutionPolicy, mCamera);
		engineOption.getAudioOptions().setNeedsMusic(true);

		return engineOption;
	}

	@Override
	protected void onCreateResources() {
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		try {
			bgMusic = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "music/test-music.ogg");
			bgMusic.setLooping(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mMenuScene = new MainMenuScene();
		if (getSharedPreferences("StrokeAppOptions", MODE_PRIVATE).getBoolean(
				"SoundOn", true))
			bgMusic.play();
		return mMenuScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	class MainMenuScene extends Scene {
		final Text[] menuItem;
		final OptionsScene optionScene;

		public MainMenuScene() {
			this.setBackground(new Background(Color.WHITE));

			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			final Text titleText = new Text(100, 40, mFont, "Stroke App",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);

			final String[] menuText = { "Start", "Time Trial", "High Score",
					"Stroke Fact", "Options" };
			menuItem = new Text[menuText.length];

			for (int i = 0; i < menuItem.length; i++) {
				menuItem[i] = new Text(100, 100 + (i * 40), mFont, menuText[i],
						VBOManager);

				this.attachChild(menuItem[i]);
				this.registerTouchArea(menuItem[i]);
			}
			this.attachChild(titleText);

			optionScene = new OptionsScene();
		}

		@Override
		public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
			if (hasChildScene()) {
				getChildScene().onSceneTouchEvent(pSceneTouchEvent);
			} else if (pSceneTouchEvent.isActionDown()) {
				for (int i = 0; i < menuItem.length; i++) {
					if (menuItem[i].contains(pSceneTouchEvent.getX(),
							pSceneTouchEvent.getY())) {
						if (i == MENU_START) {
							Intent intent = new Intent(MainActivity.this,
									LevelSelector.class);
							startActivity(intent);
						} else if (i == MENU_TIME_TRIAL) {
							// TODO Time Trial
						} else if (i == MENU_HIGH_SCORE) {
							Intent intent = new Intent(MainActivity.this,
									HighScoreScreen.class);
							startActivity(intent);
						} else if (i == MENU_STROKE_FACT) {
							Intent intent = new Intent(MainActivity.this,
									StrokeFact.class);
							startActivity(intent);
						} else if (i == MENU_OPTIONS) {
							this.setChildScene(optionScene);
						}
					}
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

			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			final Rectangle bg = new Rectangle(50, 100, 500, 280, VBOManager);
			bg.setColor(0.7f, 0.7f, 0.7f, 0.95f);

			final Text titleText = new Text(100, 140, mFont, "Options",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);

			soundOption = new Text(100, 200, mFont, "Sound : ---",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);
			soundOption.setText(("Sound : " + ((getSharedPreferences(
					"StrokeAppOptions", MODE_PRIVATE).getBoolean("SoundOn",
					true)) ? "ON" : "OFF")));

			backButton = new Text(100, 260, mFont, "Back to Menu",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);

			this.attachChild(bg);
			this.attachChild(titleText);
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
					if(getSharedPreferences(
							"StrokeAppOptions", MODE_PRIVATE).getBoolean(
							"SoundOn", true)){
						soundOption.setText("Sound : ON");
						bgMusic.play();
					}
					else{
						soundOption.setText("Sound : OFF");
						bgMusic.stop();
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