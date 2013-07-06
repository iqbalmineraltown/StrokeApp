package org.ristek.strokeapp;

import android.graphics.Typeface;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;
import org.ristek.strokeapp.support.BaseStrokeClinicActivity;
import org.ristek.strokeapp.support.ClockTimer;
import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.SaveManager;

public class LevelResult extends BaseStrokeClinicActivity {

    private Font mFont;
    private Font mFontBig;
    private Scene resultScene;
    private Text scoreText;
    private Text totalScoreText;

    @Override
    protected void onCreateResources() {
        this.createTextureAtlas(2048, 1024);
        this.loadTexture("map", "win", "lose");
        this.buildTextureAtlas();

        this.mFont = FontFactory.create(this.getFontManager(),
                this.getTextureManager(), 256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32, true,
                Color.BLACK_ABGR_PACKED_INT);
        this.mFont.load();
        this.mFontBig = FontFactory.create(this.getFontManager(),
                this.getTextureManager(), 256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 40, true,
                Color.BLACK_ABGR_PACKED_INT);
        this.mFontBig.load();
    }

    @Override
    protected Scene onCreateScene() {
        resultScene = new Scene();
        resultScene.setBackground(new SpriteBackground(new Sprite(0, 0,
                getTR("map"), getVertexBufferObjectManager())));


        boolean isWin = this.getIntent().getBooleanExtra("Win", false);
        if (isWin) {
            resultScene.attachChild(new Sprite(0, 0, getTR("win"), getVertexBufferObjectManager()));
        } else {
            resultScene.attachChild(new Sprite(0, 0, getTR("lose"), getVertexBufferObjectManager()));
        }

        Text resultText = new Text(230, 100, mFontBig, (isWin) ? "Berhasil" : "Gagal", getVertexBufferObjectManager());
        resultScene.attachChild(resultText);
        if (SaveManager.getMode() == GameMode.NORMAL) {
            scoreText = new Text(220, 220, mFont, "Poin: " + this.getIntent().getLongExtra("Score", 0),
                    getVertexBufferObjectManager());
            if (isWin) {
                totalScoreText = new Text(220, 300, mFont, "Total Poin Terkumpul: " + SaveManager.getTotalScore(),
                        getVertexBufferObjectManager());

                resultScene.attachChild(totalScoreText);
            }
        } else {
            scoreText = new Text(220, 220, mFont, "Waktu: " +
                    ClockTimer.timeToString(this.getIntent().getLongExtra("Score", 0), ClockTimer.MM_SS),
                    getVertexBufferObjectManager());
            if (isWin) {
                totalScoreText = new Text(220, 300, mFont, "Total Waktu: " +
                        ClockTimer.timeToString(SaveManager.getTotalTimeScore(), ClockTimer.HH_MM_SS),
                        getVertexBufferObjectManager());

                resultScene.attachChild(totalScoreText);
            }

        }
        resultScene.attachChild(scoreText);

        resultScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                if (touchEvent.isActionUp()) finish();
                return true;
            }
        });

        return resultScene;
    }
}
