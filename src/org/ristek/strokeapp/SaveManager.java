package org.ristek.strokeapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveManager {
	private static SharedPreferences preference = null;
	
	public static void loadPreferences(SharedPreferences preference){
		SaveManager.preference = preference;
	}
	
	public static boolean getOption(String optionName){
		return preference.getBoolean(optionName, true);
	}
	
	public static void setOption(String optionName, boolean value){
		Editor edit = preference.edit();
		edit.putBoolean(optionName, value);
		edit.commit();
	}
	
	public static int getCurrentLevel(){
		return preference.getInt("currentLevel", 1);
	}
	
	public static void setCurrentLevel(int value){
		Editor edit = preference.edit();
		edit.putInt("currentLevel", value);
		edit.commit();
	}
	
	public static double getTotalScore(){
		return preference.getLong("currentTotalScore", 0);
	}
	
	public static void setTotalScore(long value){
		Editor edit = preference.edit();
		edit.putLong("currentTotalScore", value);
		edit.commit();
	}
	
	public static long getHighScore(int highScoreIdx){
		return preference.getLong("highScore"+highScoreIdx, 0);
	}
	
	public static void setHighScore(int highScoreIdx, long score){
		Editor edit = preference.edit();
		edit.putLong("highScore"+highScoreIdx, score);
		edit.commit();
	}
	
	public static void reset(){
		setCurrentLevel(1);
		setTotalScore(0);
		setOption("Story", true);
	}
}
