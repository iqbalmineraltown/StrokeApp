package org.ristek.strokeapp;

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
public class MainMenuTest extends StrokeClinicTestBase {

    /**
     * Test menu untuk opsi, cerita, dan high score
     *
     * @throws Exception
     */
    public void testMenu() throws Exception {
        final int[] BUTTON_X = {150, 310, 550};
        final int[] BUTTON_Y = {360, 350, 360};

        this.clickOnScreen(BUTTON_X[0] + 20, BUTTON_Y[0] + 20);
        assertTrue("tidak masuk ke opsi", getActivity().getmMenuScene().hasChildScene());
        solo.goBack();

        this.clickOnScreen(BUTTON_X[1] + 20, BUTTON_Y[1] + 20);
        solo.assertCurrentActivity("tidak masuk ke kisah", StrokeFact.class);
        solo.goBack();

        this.clickOnScreen(BUTTON_X[2] + 20, BUTTON_Y[2] + 20);
        solo.assertCurrentActivity("tidak masuk ke highscore", HighScoreScreen.class);
        solo.goBack();
    }

}
