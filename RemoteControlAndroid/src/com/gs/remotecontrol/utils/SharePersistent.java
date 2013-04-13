package com.gs.remotecontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePersistent {
	private static final String PREFS_NAME = "RCKeyBoard";
	public static String getPerference(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(key, "");
	}
	
	public static void savePerference(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
