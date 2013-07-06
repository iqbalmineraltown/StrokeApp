package org.ristek.strokeapp;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.WindowManager;
import com.jayway.android.robotium.solo.Solo;
import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.SaveManager;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.ristek.strokeapp.MainActivityTest \
 * org.ristek.strokeapp.tests/android.test.InstrumentationTestRunner
 */
public class NormalModeTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    float wr;
    float hr;

    public NormalModeTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        solo.waitForActivity(MainActivity.class);
        SaveManager.reset();
        Display d = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        wr = d.getWidth() / 800.0f;
        hr = d.getHeight() / 480.0f;
        LevelGesture.onTest = true;

    }

    private void clickOnScreen(int x, int y) {
        solo.clickOnScreen(wr * x, wr * y);
    }

    /**
     * Test Mode petualangan
     *
     * @throws Exception
     */
    public void testNormal() throws Exception {
        this.clickOnScreen(250, 285); // klik ke petualangan
        solo.assertCurrentActivity("tidak masuk ke opening", OpeningActivity.class);
        solo.clickOnButton("Skip");
        solo.assertCurrentActivity("tidak masuk ke map", LevelSelector.class);
        assertEquals("tidak berada dalam mode normal", GameMode.NORMAL, SaveManager.getMode());
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
                solo.assertCurrentActivity("tidak masuk ke main menu terakhir", MainActivity.class);
            }
        }

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
