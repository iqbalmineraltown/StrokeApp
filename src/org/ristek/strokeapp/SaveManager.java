package org.ristek.strokeapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static SharedPreferences preference = null;

    public static final int HIGHSCORE_COUNT = 5;

    public static void loadPreferences(SharedPreferences preference) {
        SaveManager.preference = preference;
    }

    public static boolean getOption(String optionName) {
        return preference.getBoolean(optionName, true);
    }

    public static void setOption(String optionName, boolean value) {
        Editor edit = preference.edit();
        edit.putBoolean(optionName, value);
        edit.commit();
    }

    public static int getCurrentLevel() {
        return preference.getInt("currentLevel", 1);
    }

    public static void setCurrentLevel(int value) {
        Editor edit = preference.edit();
        edit.putInt("currentLevel", value);
        edit.commit();
    }

    public static long getTotalScore() {
        return preference.getLong("currentTotalScore", 0);
    }

    public static void setTotalScore(long value) {
        Editor edit = preference.edit();
        edit.putLong("currentTotalScore", value);
        edit.commit();
    }

    public static long getHighScore(int highScoreIdx) {
        return preference.getLong("highScore" + highScoreIdx, 0);
    }

    public static void setHighScore(int highScoreIdx, long score) {
        Editor edit = preference.edit();
        edit.putLong("highScore" + highScoreIdx, score);
        edit.commit();
    }

    public static void addTotalScoreToHighScore() {
        addScoreToHighScore(getTotalScore());
    }

    public static void addScoreToHighScore(long score) {
        List<Long> arr = new ArrayList<Long>(HIGHSCORE_COUNT);
        for (int i = 0; i < HIGHSCORE_COUNT; i++) {
            arr.add(getHighScore(i));
        }
        int x = 0;
        while (x < HIGHSCORE_COUNT && arr.get(x) >= score) {
            x++;
        }
        long temp = arr.get(x);
        arr.set(x, score);
        for (int i = x + 1; i < HIGHSCORE_COUNT; i++) {
            long temp2 = arr.get(i);
            arr.set(i, temp);
            temp = temp2;
        }
        for (int i = 0; i < HIGHSCORE_COUNT; i++) {
            setHighScore(i, arr.get(i));
        }
    }

    public static int getMode(){
        return preference.getInt("currentMode", GameMode.NORMAL);
    }

    public static void setMode(int value) {
        Editor edit = preference.edit();
        edit.putLong("currentMode", value);
        edit.commit();
    }
    public static void reset() {
        setCurrentLevel(1);
        setTotalScore(0);
        setOption("Story", true);
    }
}
