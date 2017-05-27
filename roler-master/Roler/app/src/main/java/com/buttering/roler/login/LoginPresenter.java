package com.buttering.roler.login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.buttering.roler.net.basepresenter.BasePresenter;
import com.buttering.roler.net.baseservice.UserService;

import java.util.Calendar;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class LoginPresenter extends BasePresenter implements ILoginPresenter {

	public static final String TOKEN_REFRESH = "com.buttering.roler.TOKEN_REFRESH";


	private UserService userService;
	private ILoginView view;
	private Context context;

	public LoginPresenter(ILoginView view, Context context) {
		this.view = view;
		this.userService = new UserService();
		this.context = context;
	}

	public LoginPresenter(Context context) {
		this.userService = new UserService();
		this.context = context;
	}


	@Override
	public Observable<String> signIn(String email, String pwd) {
		view.showLoadingBar();

		registerRefreshToken();

		return userService
				.signIn(email, pwd)
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Observable<Void> registerToken(String token, String email) {

		return userService
				.registerToken(token, email)
				.observeOn(AndroidSchedulers.mainThread());
	}

	private void registerRefreshToken() {

		Intent intent = new Intent(TOKEN_REFRESH);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//		long triggerTime = setTriggerTime();
		long triggerTime = Calendar.getInstance().getTimeInMillis();

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 1000 * 60 * 15  , pendingIntent);

	}
	private long setTriggerTime()
	{
		// current Time
		long atime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		int nowMinute = calendar.get(Calendar.MINUTE);
		int nowYear = calendar.get(Calendar.YEAR);
		int nowDay = calendar.get(Calendar.HOUR_OF_DAY);

		// timepicker
		Calendar curTime = Calendar.getInstance();
		curTime.set(Calendar.YEAR, nowYear);
		curTime.set(Calendar.HOUR_OF_DAY, nowDay);
		curTime.set(Calendar.MINUTE, nowMinute);
		curTime.set(Calendar.SECOND, 0);
		curTime.set(Calendar.MILLISECOND, 0);

		long btime = curTime.getTimeInMillis();
		long triggerTime = btime;
		if (atime > btime)
			triggerTime += 1000 * 60 * 15;

		return triggerTime;
	}
}
