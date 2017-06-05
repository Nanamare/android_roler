package com.buttering.roler.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kinamare on 2016-12-18.
 */

public class SharePrefUtil {

	private static Context context = MyApplication.getInstance().getContext();

	/**
	 * <pre>
	 * String 데이터를 저장합니다.
	 * </pre>
	 *
	 * @param key   키
	 * @param value 값
	 */
	public static void putSharedPreference(String key, String value) {
		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * <pre>
	 * Boolean 데이터를 저장합니다.
	 * </pre>
	 *
	 * @param key   키
	 * @param value 값
	 */
	public static void putSharedPreference
	(String key, boolean value) {
		SharedPreferences prefs =

				PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * <pre>
	 * Integer 데이터를 저장합니다.
	 * </pre>
	 *
	 * @param key   키
	 * @param value 값
	 */
	public static void putSharedPreference(String key, int value) {
		SharedPreferences prefs =

				PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * <pre>
	 * String 데이터를 읽어옵니다.
	 * </pre>
	 *
	 * @param key 키
	 * @return 읽어온 값, 값이 없을 경우 null이 반환된다.
	 */
	public static String getSharedPreference(String key) {
		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		return prefs.getString(key, null);
	}

	/**
	 * <pre>
	 * Boolean 데이터를 읽어옵니다.
	 * </pre>
	 *
	 * @param key 키
	 * @return 읽어온 값, 값이 없을 경우 false가 반환된다.
	 */
	public static boolean getBooleanSharedPreference
	(String key) {
		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		return prefs.getBoolean(key, false);
	}

	/**
	 * <pre>
	 * Int 데이터를 읽어옵니다.
	 * </pre>
	 *
	 * @param key 키
	 * @return 읽어온 값, 값이 없을 경우 0이 반환된다.
	 */
	public static int getIntSharedPreference
	(String key) {
		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		return prefs.getInt(key, 0);
	}

	public static void delSharePreference(String key) {

		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		prefs.edit().remove(key).commit();

	}


}

