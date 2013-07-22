package org.ristek.strokeapp;

import android.content.Intent;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.ristek.strokeapp.support.BaseStrokeClinicActivity;

import java.util.Arrays;
import java.util.Collections;

public class ExerciseActivity extends BaseStrokeClinicActivity {

    private final int[] num = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private AnimatedSprite hand3;
    private AnimatedSprite hand4;

    @Override
    protected void onCreateResources() {
        this.createTextureAtlas(4096, 4096);
        this.loadTexture("bg-exercise");
        this.loadTiledTexture(4, 3, "hand3");
        this.loadTiledTexture(8, 4, "hand4");
        this.buildTextureAtlas();
    }

    @Override
    protected Scene onCreateScene() {
        Collections.shuffle(Arrays.asList(num));
        Scene mScene = new Scene();
        mScene.setBackground(new SpriteBackground(new Sprite(0, 0,
                getTR("bg-exercise"), getVertexBufferObjectManager())));

        hand3 = new AnimatedSprite(455, 85, (ITiledTextureRegion) getTR("hand3"), this.getVertexBufferObjectManager());
        hand4 = new AnimatedSprite(455, 85, (ITiledTextureRegion) getTR("hand4"), this.getVertexBufferObjectManager());
        mScene.attachChild(hand3);
        mScene.attachChild(hand4);
        animate(0);
        mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                Intent intent = new Intent(ExerciseActivity.this,
                        LevelSelector.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });
        return mScene;
    }

    private void animate(final int x) {
        AnimatedSprite.IAnimationListener nextStep = new AnimatedSprite.IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite animatedSprite, int i) {
            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite animatedSprite, int i, int i2) {
            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite animatedSprite, int i, int i2) {
            }

            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                if (x < 4) animate(x + 1);
                else {
                    Intent intent = new Intent(ExerciseActivity.this,
                            LevelSelector.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        int idx = num[x];
        if (idx < 8) {
            hand4.setVisible(true);
            hand3.setVisible(false);
            long[] frameDuration = {800, 800, 800, 3000};
            hand4.animate(frameDuration, idx * 4, (idx * 4) + 3, 2, nextStep);
        } else {
            idx -= 8;
            hand4.setVisible(false);
            hand3.setVisible(true);
            long[] frameDuration = {800, 800, 3000};
            hand4.animate(frameDuration, idx * 3, (idx * 3) + 2, 2, nextStep);
        }
    }
}
