package org.ristek.strokeapp;

import org.andengine.engine.camera.Camera;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Intent;
import android.graphics.Typeface;

public class MainActivity extends BaseStrokeClinicActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int MENU_START = 0;
	protected static final int MENU_TIME_TRIAL = 1;
	protected static final int MENU_OPTIONS = 2;
	protected static final int MENU_STROKE_STORY = 3;
	protected static final int MENU_HIGH_SCORE = 4;
	protected static final int MENU_SUM = 5;
	protected static final String[] tiledTextureName = { "b-start", "b-time",
			"b-opsi", "b-cerita", "b-hiscore" };
	protected static final String[] textureName = { "l-bg", "opsi" };

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;
	protected Scene mMenuScene;

	private Font mFont;

	// private Music bgMusic;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainActivity() {
		super();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onCreateResources() {
		// Load Font
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true,
				Color.WHITE_ARGB_PACKED_INT);
		this.mFont.load();

		this.createTextureAtlas(2048, 1024);
		this.loadTexture(textureName);
		this.loadTiledTexture(tiledTextureName, 2, 1);
		this.buildTextureAtlas();

		// Load Music
		// try {
		// bgMusic = MusicFactory.createMusicFromAsset(
		// mEngine.getMusicManager(), this, "music/test-music.ogg");
		// bgMusic.setLooping(true);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		SaveManager.loadPreferences(getSharedPreferences("StrokeApp",
				MODE_PRIVATE));

		mMenuScene = new MainMenuScene();
		// if (SaveManager.getOption("Sound"))
		// bgMusic.play();
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

			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			this.setBackground(new SpriteBackground(new Sprite(0, 0,
					getTR("l-bg"), VBOManager)));

			menuButtons = new TiledSprite[MENU_SUM];

			for (int i = 0; i < menuButtons.length; i++) {
				menuButtons[i] = new TiledSprite(BUTTON_X[i], BUTTON_Y[i],
						(TiledTextureRegion) getTR(tiledTextureName[i]),
						VBOManager);

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
							pSceneTouchEvent.getY())
							&& menuButtons[i].getCurrentTileIndex() == 1) {

						if (i == MENU_START) {
							if (SaveManager.getOption("Story")) {
								SaveManager.setOption("Story", false);
								Intent intent = new Intent(MainActivity.this,
										OpeningActivity.class);
								startActivity(intent);
							} else {
								Intent intent = new Intent(MainActivity.this,
										LevelSelector.class);
								startActivity(intent);
							}
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
		final Text resetText;
		final Text backButton;

		public OptionsScene() {
			this.setBackgroundEnabled(false);
			this.attachChild(new Sprite(0, 0, getTR("opsi"),
					getVertexBufferObjectManager()));
			final VertexBufferObjectManager VBOManager = MainActivity.this
					.getVertexBufferObjectManager();

			soundOption = new Text(30, 200, mFont, "Suara : -----",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);
			soundOption
					.setText(("Suara : " + (SaveManager.getOption("Sound") ? "Hidup"
							: "Mati")));

			resetText = new Text(30, 240, mFont, "Reset        ",
					new TextOptions(HorizontalAlign.LEFT), VBOManager);

			backButton = new Text(30, 320, mFont, "Kembali", new TextOptions(
					HorizontalAlign.LEFT), VBOManager);

			this.attachChild(soundOption);
			this.attachChild(backButton);
			this.attachChild(resetText);
			this.registerTouchArea(soundOption);
			this.registerTouchArea(backButton);
			this.registerTouchArea(resetText);
		}

		@Override
		public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
			float pX = pSceneTouchEvent.getX();
			float pY = pSceneTouchEvent.getY();
			if (pSceneTouchEvent.isActionDown()) {
				if (soundOption.contains(pX, pY)) {
					SaveManager.setOption("Sound",
							!SaveManager.getOption("Sound"));
					if (SaveManager.getOption("Sound")) {
						soundOption.setText("Suara : Hidup");
						// bgMusic.play();
					} else {
						soundOption.setText("Suara : Mati");
						// bgMusic.pause();
					}

				}
				if (backButton.contains(pX, pY)) {
					resetText.setText("Reset");
					this.back();
				}
				if (resetText.contains(pX, pY)) {
					SaveManager.reset();
					resetText.setText("Reset selesai");
				}
			}
			return true;
		}
	}

}