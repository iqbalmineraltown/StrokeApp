package org.ristek.strokeapp;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.WindowManager;
import com.jayway.android.robotium.solo.Solo;
import org.ristek.strokeapp.support.SaveManager;

public abstract class StrokeClinicTestBase extends ActivityInstrumentationTestCase2<MainActivity> {

    protected Solo solo;
    private float wr;
    private float hr;

    public StrokeClinicTestBase() {
        super(MainActivity.class);
    }

    protected void clickOnScreen(int x, int y) {
        solo.clickOnScreen(wr * x, hr * y);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        solo.waitForActivity(MainActivity.class);
        SaveManager.reset();
        LevelGesture.onTest = true;
        Display d = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        wr = d.getWidth() / 800.0f;
        hr = d.getHeight() / 480.0f;
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void playUntilEnding() {
        final int[] LEVEL_X = {99, 97, 225, 198, 174, 363, 469,
                328, 672};
        final int[] LEVEL_Y = {357, 179, 84, 203, 295, 346, 252,
                251, 135};
        for (int i = 0; i < 9; i++) {
            this.clickOnScreen(LEVEL_X[i] + 25, LEVEL_Y[i] + 25);
            if (i < 5) {
                for (int j = 0; j < 5; j++) {
                    solo.assertCurrentActivity("tidak masuk ke pertanyaan", LevelQuestion.class);
                    LevelQuestion level = (LevelQuestion) solo.getCurrentActivity();
                    solo.clickOnRadioButton(LevelQuestion.questionList[level.questionId].trueAnswer);
                    solo.clickOnButton(5);
                }
                solo.assertCurrentActivity("tidak masuk ke result", LevelResult.class);
                assertTrue("tidak menang pada level " + (i + 1)
                        , solo.getCurrentActivity().getIntent().getBooleanExtra("Win", false));
                this.clickOnScreen(400, 240);
                solo.assertCurrentActivity("tidak masuk ke map lagi", LevelSelector.class);
            } else if (i < 8) {
                for (int j = 0; j < 3; j++) {
                    solo.assertCurrentActivity("tidak masuk ke gesture", LevelGesture.class);
                    solo.clickOnButton(1);
                }
                solo.assertCurrentActivity("tidak masuk ke result", LevelResult.class);
                assertTrue("tidak menang pada level " + (i + 1)
                        , solo.getCurrentActivity().getIntent().getBooleanExtra("Win", false));
                this.clickOnScreen(400, 240);
                solo.assertCurrentActivity("tidak masuk ke map lagi", LevelSelector.class);
            } else {
                for (int j = 0; j < 3; j++) {
                    solo.assertCurrentActivity("tidak masuk ke pertanyaan", LevelQuestion.class);
                    LevelQuestion level = (LevelQuestion) solo.getCurrentActivity();
                    solo.clickOnRadioButton(LevelQuestion.questionList[level.questionId].trueAnswer);
                    solo.clickOnButton(5);
                }
                for (int j = 0; j < 3; j++) {
                    solo.assertCurrentActivity("tidak masuk ke gesture", LevelGesture.class);
                    solo.clickOnButton(1);
                }
                solo.assertCurrentActivity("tidak masuk ke result", LevelResult.class);
                assertTrue("tidak menang pada level " + (i + 1)
                        , solo.getCurrentActivity().getIntent().getBooleanExtra("Win", false));
                this.clickOnScreen(400, 240);
                solo.assertCurrentActivity("tidak masuk ke ending", EndingActivity.class);
                solo.waitForActivity(MainActivity.class, 40000);
                solo.assertCurrentActivity("tidak masuk ke menu utama lagi", MainActivity.class);
            }
        }
    }
}
