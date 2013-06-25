package org.ristek.strokeapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.widget.Toast;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import java.util.Arrays;
import java.util.Collections;

public class LevelSelector extends BaseStrokeClinicActivity {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String[] textureName = {"map", "i-pos", "i-pos-off",
            "i-pass", "i-1", "i-2", "i-3", "i-4", "i-5", "i-6", "i-7", "i-8",
            "i-level1", "i-level2", "i-level3"};

    private static final String[] gestureName = {"Ga", "Pa", "Da", "Ca", "Ya",
            "Ta", "Ja", "Wa", "Ka", "Dha", "Ha", "Ma", "Sa", "Na", "Ra", "La"};

    private static final int[] LEVEL_X = {99, 97, 225, 198, 174, 363, 469,
            328, 672};
    private static final int[] LEVEL_Y = {357, 179, 84, 203, 295, 346, 252,
            251, 135};
    private static final int[] ROUTE_X = {122, 122, 180, 193, 209, 389, 353,
            353};
    private static final int[] ROUTE_Y = {204, 104, 109, 228, 320, 277, 271,
            155};

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

    protected Sprite[] arrPost;
    protected Sprite[] arrPostOff;
    protected Sprite[] arrPostWin;
    protected Sprite[] route;
    protected Scene mMainScene;
    protected Font mFont;
    private int gameState;
    private int gameLevel;
    private int trueAnswer;
    private int currentLevel;
    private long totalGestureScore;

    @Override
    protected void onCreateResources() {
        // TODO Auto-generated method stub
        this.mFont = FontFactory.create(this.getFontManager(),
                this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.ITALIC), 32);
        this.mFont.load();

        this.createTextureAtlas(2048, 1024);
        this.loadTexture(textureName);
        this.buildTextureAtlas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (gameState == STATE_QUESTION_LEVEL) {
            if (requestCode == QUESTION_ACTIVITY_REQUEST
                    && resultCode == RESULT_OK) {
                if (gameLevel < 5) {
                    gameLevel++;
                    if (data.getBooleanExtra("QuestionResult", false))
                        trueAnswer++;
                    createQuestionLevel(gameLevel - 1);
                } else {
                    if (data.getBooleanExtra("QuestionResult", false))
                        trueAnswer++;
                    Toast.makeText(
                            this,
                            "Benar: " + trueAnswer + ", Salah: "
                                    + (5 - trueAnswer) + ", Score Akhir: "
                                    + (trueAnswer * 25), Toast.LENGTH_SHORT)
                            .show();
                    gameState = STATE_LEVEL_SELECT;
                    gameLevel = 0;
                    if (trueAnswer == 5)
                        updateLevel(currentLevel + 1);
                }
            } else {
                Toast.makeText(this, "Hasil : Salah", Toast.LENGTH_SHORT)
                        .show();
                gameState = STATE_LEVEL_SELECT;
                gameLevel = 0;
            }
        }
        if (gameState == STATE_GESTURE_LEVEL) {
            if (requestCode == GESTURE_ACTIVITY_REQUEST
                    && resultCode == RESULT_OK
                    && data.getBooleanExtra("gestureResult", false)) {
                long currentScore = (long) (data.getDoubleExtra("gestureScore",
                        0) * 10);
                totalGestureScore += currentScore;

                if (gameLevel < 3) {
                    Toast.makeText(this,
                            "Hasil : Benar, Score:" + currentScore,
                            Toast.LENGTH_SHORT).show();
                    gameLevel++;
                    createGestureLevel(gameLevel);
                } else {
                    Toast.makeText(this,
                            "Hasil : Benar, Total Score:" + totalGestureScore,
                            Toast.LENGTH_SHORT).show();
                    gameState = STATE_LEVEL_SELECT;
                    gameLevel = 0;
                    updateLevel(currentLevel + 1);
                }
            } else {
                Toast.makeText(
                        this,
                        "Hasil : Salah, Score Akhir:"
                                + totalGestureScore,
                        Toast.LENGTH_SHORT).show();
                gameState = STATE_LEVEL_SELECT;
                gameLevel = 0;
            }
        } else if (gameState == STATE_FINAL_LEVEL) {
            if (resultCode == RESULT_OK) {
                if (gameLevel <= 3) {
                    gameLevel++;
                    if (data.getBooleanExtra("QuestionResult", false))
                        trueAnswer++;
                    if (gameLevel <= 3)
                        createQuestionLevel(gameLevel - 1);
                    else
                        createGestureLevel(gameLevel);
                } else {
                    long currentScore = (long) (data.getDoubleExtra(
                            "gestureScore", 0) * 10);
                    if (data.getBooleanExtra("gestureResult", false)) {
                        if (gameLevel < 6) {
                            Toast.makeText(this,
                                    "Hasil : Benar, Score:" + currentScore,
                                    Toast.LENGTH_SHORT).show();
                            gameLevel++;
                            createGestureLevel(gameLevel);
                        } else {
                            Toast.makeText(
                                    this,
                                    "Hasil : Benar, Total Score:"
                                            + (totalGestureScore + (trueAnswer * 25)),
                                    Toast.LENGTH_SHORT).show();
                            gameState = STATE_LEVEL_SELECT;
                            gameLevel = 0;
                        }
                    } else {
                        Toast.makeText(
                                this,
                                "Hasil : Salah, Score:"
                                        + (long) (data.getDoubleExtra(
                                        "gestureScore", 0) * 10),
                                Toast.LENGTH_SHORT).show();
                        gameState = STATE_LEVEL_SELECT;
                        gameLevel = 0;
                    }
                }
            } else {
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

    protected void createQuestionLevel(int questionIdx) {
        Intent intent = new Intent(LevelSelector.this, Questions.class);
        intent.putExtra("QuestionIndex", questionIdx);
        startActivityForResult(intent, QUESTION_ACTIVITY_REQUEST);
    }

    @Override
    protected Scene onCreateScene() {
        // TODO Auto-generated method stub
        gameState = STATE_LEVEL_SELECT;
        currentLevel = SaveManager.getCurrentLevel();
        this.mEngine.registerUpdateHandler(new FPSLogger());

        mMainScene = new Scene() {
            int selection = -1;

            @Override
            public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
                float pX = pSceneTouchEvent.getX();
                float pY = pSceneTouchEvent.getY();
                if (pSceneTouchEvent.isActionDown()) {
                    for (int i = 0; i < arrPost.length; i++) {
                        if (arrPost[i].isVisible()
                                && arrPost[i].contains(pX, pY)) {
                            selection = i;
                        }
                    }
                } else if (pSceneTouchEvent.isActionUp()) {
                    for (int i = 0; i < arrPost.length; i++) {
                        if (selection == i) {
                            selection = -1;
                            if (i < 5) {
                                // Jika bagian pertanyaan
                                gameState = STATE_QUESTION_LEVEL;
                                gameLevel = 1;
                                trueAnswer = 0;
                                createQuestionLevel(gameLevel - 1);
                            } else if (i < 8) {
                                // Jika bagian gesture
                                Collections.shuffle(Arrays.asList(gestureName));
                                gameState = STATE_GESTURE_LEVEL;
                                gameLevel = 1;
                                totalGestureScore = 0;
                                createGestureLevel(gameLevel);
                            } else {
                                Collections.shuffle(Arrays.asList(gestureName));
                                gameState = STATE_FINAL_LEVEL;
                                gameLevel = 1;
                                totalGestureScore = 0;
                                trueAnswer = 0;
                                createQuestionLevel(gameLevel - 1);
                            }

                        }
                    }
                }
                updateLevel(currentLevel);
                if (selection >= 0) {
                    arrPost[selection].setVisible(false);
                    arrPostWin[selection].setVisible(true);
                }
                return super.onSceneTouchEvent(pSceneTouchEvent);
            }
        };
        mMainScene.setBackground(new SpriteBackground(new Sprite(0, 0,
                getTR("map"), getVertexBufferObjectManager())));

        route = new Sprite[POST_SUM - 1];
        for (int i = 0; i < route.length; i++) {
            route[i] = new Sprite(ROUTE_X[i], ROUTE_Y[i],
                    getTR("i-" + (i + 1)), getVertexBufferObjectManager());
            mMainScene.attachChild(route[i]);
        }

        arrPost = new Sprite[POST_SUM];
        for (int i = 0; i < arrPost.length; i++) {
            arrPost[i] = new Sprite(LEVEL_X[i], LEVEL_Y[i], getTR("i-pos"),
                    getVertexBufferObjectManager());
            mMainScene.attachChild(arrPost[i]);
            mMainScene.registerTouchArea(arrPost[i]);
        }
        arrPostWin = new Sprite[POST_SUM];
        for (int i = 0; i < arrPostWin.length; i++) {
            arrPostWin[i] = new Sprite(LEVEL_X[i], LEVEL_Y[i], getTR("i-pass"),
                    getVertexBufferObjectManager());
            mMainScene.attachChild(arrPostWin[i]);
            mMainScene.registerTouchArea(arrPostWin[i]);
        }
        arrPostOff = new Sprite[POST_SUM];
        for (int i = 0; i < arrPostOff.length; i++) {
            arrPostOff[i] = new Sprite(LEVEL_X[i], LEVEL_Y[i],
                    getTR("i-pos-off"), getVertexBufferObjectManager());
            mMainScene.attachChild(arrPostOff[i]);
        }
        updateLevel(currentLevel);
        return mMainScene;
    }

    public void updateLevel(int curr) {
        currentLevel = curr;
        SaveManager.setCurrentLevel(curr);
        for (int i = 0; i < arrPost.length; i++) {
            arrPost[i].setVisible(i == currentLevel - 1);
            arrPostWin[i].setVisible(i < currentLevel - 1);
            arrPostOff[i].setVisible(i > currentLevel - 1);
        }
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
