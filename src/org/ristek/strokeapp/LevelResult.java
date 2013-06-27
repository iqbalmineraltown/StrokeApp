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

public class LevelResult extends BaseStrokeClinicActivity {

    private Font mFont;
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

        Text resultText = new Text(300, 100, mFont, (isWin) ? "Berhasil" : "Gagal", getVertexBufferObjectManager());
        resultScene.attachChild(resultText);

        scoreText = new Text(300, 200, mFont, "Nilai: " + this.getIntent().getLongExtra("Score", 0),
                getVertexBufferObjectManager());
        resultScene.attachChild(scoreText);

        totalScoreText = new Text(300, 300, mFont, "Total Nilai: " + SaveManager.getTotalScore(),
                getVertexBufferObjectManager());
        resultScene.attachChild(totalScoreText);

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
