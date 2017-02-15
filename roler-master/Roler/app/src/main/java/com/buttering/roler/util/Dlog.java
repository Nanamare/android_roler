package com.buttering.roler.util;

import com.buttering.roler.net.baseservice.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Map;

/**
 * Created by kinamare on 2016-12-17.
 */

public class Dlog {

	static final String TAG = "Logger";


	/**
	 * Log Level Error
	 **/
	public static final void e(String message) {

//		if (TimelineService.DEBUG) Log.e(TAG, buildLogMsg(message));
	}

	/**
	 * Log Level Warning
	 **/
	public static final void w(String message) {
//		if (TimelineService.DEBUG) Log.w(TAG, buildLogMsg(message));
	}

	/**
	 * Log Level Information
	 **/
	public static final void i(String message) {
//		if (TimelineService.DEBUG) Log.i(TAG, buildLogMsg(message));
	}

	/**
	 * Log Level Debug
	 **/
	public static final void d(Map<String, String> map, String json) {

		if (UserService.DEBUG) {
			try {
				System.out.println(" ");
				System.out.println(" ");
				System.out.println("//---------------------------------------------------------------------------------------------------------------");
				System.out.println(buildLogMsg("start"));
				for (String key : map.keySet()) {
					System.out.println(String.format("%s :  %S", key, map.get(key)));
				}
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(json);
				String prettyJson = gson.toJson(je);
				System.out.println(prettyJson);

			} catch (Exception e) {
				System.out.println(e.toString());
			}
			System.out.println(" ");
			System.out.println("//---------------------------------------------------------------------------------------------------------------");
			System.out.println(" ");
			System.out.println(" ");
		}



	}

	/**
	 * Log Level Verbose
	 **/
	public static final void v(String message) {
//		if (TimelineService.DEBUG) Log.v(TAG, buildLogMsg(message));
	}


	public static String buildLogMsg(String message) {

		StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append(ste.getFileName().replace(".java", ""));
		sb.append("::");
		sb.append(ste.getMethodName());
		sb.append("]");
		sb.append(message);

		return sb.toString();

	}
}