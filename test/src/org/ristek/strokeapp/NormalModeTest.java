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
public class NormalModeTest extends StrokeClinicTestBase {

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
        this.playUntilEnding();

    }
}
