package org.ristek.strokeapp;

import android.graphics.Typeface;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;
import org.ristek.strokeapp.support.BaseStrokeClinicActivity;
import org.ristek.strokeapp.support.ClockTimer;
import org.ristek.strokeapp.support.SaveManager;


public class HighScoreScreen extends BaseStrokeClinicActivity {

    private final static String[] textureName = {"hiscore-bg"};

    private Scene mScene;
    private Font mFont;

    @Override
    protected void onCreateResources() {

        this.createTextureAtlas(1024, 1024);
        this.loadTexture(textureName);
        this.buildTextureAtlas();

        this.mFont = FontFactory.create(this.getFontManager(),
                this.getTextureManager(), 256, 256,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 20, true,
                Color.BLACK_ABGR_PACKED_INT);
        this.mFont.load();
    }

    @Override
    protected Scene onCreateScene() {

        mScene = new Scene();
        mScene.setBackground(new SpriteBackground(new Sprite(0, 0,
                getTR("hiscore-bg"), getVertexBufferObjectManager())));
        mScene.attachChild(new Text(220, 180, mFont, "Petualangan:",
                getVertexBufferObjectManager()));
        mScene.attachChild(new Text(420, 180, mFont, "Tantangan Waktu:",
                getVertexBufferObjectManager()));
        for (int i = 0; i < SaveManager.HIGHSCORE_COUNT; i++) {
            mScene.attachChild(new Text(220, 220 + i * 50, mFont, (i + 1) + ". " + SaveManager.getHighScore(i),
                    getVertexBufferObjectManager()));
        }

        for (int i = 0; i < SaveManager.HIGHSCORE_COUNT; i++) {
            mScene.attachChild(new Text(420, 220 + i * 50, mFont, (i + 1) + ". " +
                    ClockTimer.timeToString(SaveManager.getTimeHighScore(i), ClockTimer.HH_MM_SS),
                    getVertexBufferObjectManager()));
        }

        return mScene;
    }


}
