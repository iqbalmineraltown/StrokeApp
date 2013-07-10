package org.ristek.strokeapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
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
import org.ristek.strokeapp.support.BaseStrokeClinicActivity;
import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.ResetDialogFragment;
import org.ristek.strokeapp.support.SaveManager;

import java.io.IOException;

public class MainActivity extends BaseStrokeClinicActivity implements ResetDialogFragment.ResetDialogListener {

    // ===========================================================
    // Constants
    // ===========================================================

    protected static final int MENU_START = 0;
    protected static final int MENU_TIME_TRIAL = 1;
    protected static final int MENU_OPTIONS = 2;
    protected static final int MENU_STROKE_STORY = 3;
    protected static final int MENU_HIGH_SCORE = 4;
    protected static final int MENU_SUM = 5;

    protected static final int RESET_OPTIONS = 0;
    protected static final int RESET_NORMAL = 1;
    protected static final int RESET_TIME = 2;

    protected static final String[] tiledTextureName = {"b-start", "b-time",
            "b-opsi", "b-cerita", "b-hiscore"};
    protected static final String[] textureName = {"l-bg", "opsi"};

    // ===========================================================
    // Fields
    // ===========================================================

    public Scene getmMenuScene() {
        return mMenuScene;
    }

    protected Scene mMenuScene;

    private Font mFont;
    private Music bgMusic;
    private int resetStatus;
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
        this.loadTiledTexture(1, 2, tiledTextureName);
        this.buildTextureAtlas();

        // Load Music
        try {
            bgMusic = MusicFactory.createMusicFromAsset(
                    mEngine.getMusicManager(), this, "music/bg.mp3");
            bgMusic.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        SaveManager.loadPreferences(getSharedPreferences("StrokeApp",
                MODE_PRIVATE));
        if (SaveManager.getOption("Sound")) bgMusic.play();
        mMenuScene = new MainMenuScene();
        return mMenuScene;
    }

    @Override
    public void onResetDone() {
        if (resetStatus == RESET_OPTIONS)
            ((MainMenuScene) mMenuScene).resetGame();
        else {
            bgMusic.seekTo(0);
            bgMusic.pause();
            SaveManager.reset();
            SaveManager.setMode(resetStatus - 1);
            SaveManager.setOption("Story", false);
            Intent intent = new Intent(MainActivity.this,
                    OpeningActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        if (bgMusic != null && SaveManager.getOption("Sound")) {
            bgMusic.play();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMenuScene.hasChildScene()) mMenuScene.getChildScene().back();
        else super.onBackPressed();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    class MainMenuScene extends Scene {
        final int[] BUTTON_X = {230, 447, 150, 310, 550};
        final int[] BUTTON_Y = {265, 270, 360, 350, 360};
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
                for (TiledSprite menuButton : menuButtons) {
                    if (menuButton.contains(pSceneTouchEvent.getX(),
                            pSceneTouchEvent.getY())) {
                        menuButton.setCurrentTileIndex(1);
                    }
                }
            } else if (pSceneTouchEvent.isActionUp()) {
                for (int i = 0; i < menuButtons.length; i++) {
                    if (menuButtons[i].contains(pSceneTouchEvent.getX(),
                            pSceneTouchEvent.getY())
                            && menuButtons[i].getCurrentTileIndex() == 1) {

                        if (i == MENU_START) {
                            if (SaveManager.getOption("Story")) {
                                bgMusic.seekTo(0);
                                bgMusic.pause();
                                SaveManager.setMode(GameMode.NORMAL);
                                SaveManager.setOption("Story", false);
                                Intent intent = new Intent(MainActivity.this,
                                        OpeningActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            } else if (SaveManager.getMode() == GameMode.TIME_TRIAL) {
                                resetStatus = RESET_NORMAL;
                                DialogFragment dialog = new ResetDialogFragment();
                                dialog.show(getSupportFragmentManager(), "reset");
                            } else {
                                bgMusic.seekTo(0);
                                bgMusic.pause();
                                Intent intent = new Intent(MainActivity.this,
                                        ExerciseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        } else if (i == MENU_TIME_TRIAL) {
                            if (SaveManager.getOption("Story")) {
                                bgMusic.seekTo(0);
                                bgMusic.pause();
                                SaveManager.setMode(GameMode.TIME_TRIAL);
                                SaveManager.setOption("Story", false);
                                Intent intent = new Intent(MainActivity.this,
                                        OpeningActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            } else if (SaveManager.getMode() == GameMode.NORMAL) {
                                resetStatus = RESET_TIME;
                                DialogFragment dialog = new ResetDialogFragment();
                                dialog.show(getSupportFragmentManager(), "reset");
                            } else {
                                bgMusic.seekTo(0);
                                bgMusic.pause();
                                Intent intent = new Intent(MainActivity.this,
                                        ExerciseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
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

        public void resetGame() {
            if (hasChildScene()) {
                ((OptionsScene) getChildScene()).resetComplete();
            }
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
                        bgMusic.seekTo(0);
                        bgMusic.play();
                    } else {
                        soundOption.setText("Suara : Mati");
                        bgMusic.pause();
                    }

                }
                if (backButton.contains(pX, pY)) {
                    resetText.setText("Reset");
                    this.back();
                }
                if (resetText.contains(pX, pY)) {
                    resetStatus = RESET_OPTIONS;
                    DialogFragment dialog = new ResetDialogFragment();
                    dialog.show(getSupportFragmentManager(), "reset");
                }
            }
            return true;
        }

        public void resetComplete() {
            resetText.setText("Reset Selesai");
        }
    }

}