package org.ristek.strokeapp;

import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.SaveManager;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.ristek.strokeapp.MainMenuTest \
 * org.ristek.strokeapp.tests/android.test.InstrumentationTestRunner
 */
public class TimeTrialModeTest extends StrokeClinicTestBase {

    /**
     * Test Mode time trial
     *
     * @throws Exception
     */
    public void testTimeTrial() throws Exception {
        this.clickOnScreen(467, 290); // klik ke time trial
        solo.assertCurrentActivity("tidak masuk ke opening", OpeningActivity.class);
        solo.clickOnButton("Skip");
        solo.assertCurrentActivity("tidak masuk ke map", LevelSelector.class);
        assertEquals("tidak berada dalam mode time trial", GameMode.TIME_TRIAL, SaveManager.getMode());
        this.playUntilEnding();
    }
}
