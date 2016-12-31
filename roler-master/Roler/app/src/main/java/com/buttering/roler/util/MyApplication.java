package com.buttering.roler.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.buttering.roler.R;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by kinamare on 2016-12-17.
 */

public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();
	private static MyApplication sInstance;
	private Tracker tracker;

	public static synchronized MyApplication getInstance() {
		if (sInstance != null)
			return sInstance;
		return null;
	}

	public Context getContext() {
		return this.getApplicationContext();
	}


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);

	}


	synchronized public Tracker getDefaultTracker() {
		if (tracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);


			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			//추적 아이디 적기
//			tracker = analytics.newTracker(R.xml.global_trakcer);
		}
		return tracker;
	}

	@Override
	public void onCreate() {
		super.onCreate();


		FacebookSdk.sdkInitialize(this);
		sInstance = this;


	}



}
